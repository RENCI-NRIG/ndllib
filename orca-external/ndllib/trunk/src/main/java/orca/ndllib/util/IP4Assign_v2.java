package orca.ndllib.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Iterator;

import com.google.common.net.InetAddresses;

import orca.ndllib.resources.request.*;
import orca.ndllib.util.*;
import orca.ndl.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.BitSet;

/**
 * Class to manage ip address assignments
 * 
 * @author pruth
 * 
 */
public class IP4Assign_v2 {
	/*
	private TreeMap<Integer, Integer> managedSubnetTreeMap; // for searching if
															// subnet overlapps
															// with any managed
															// network
	private TreeMap<Integer, Integer> allocatedSubnetTreeMap; // for searching
																// for conficts

	private HashMap<Object, IP4Subnet> allocatedSubnetMap; // for getting subnet
															// of specific
															// link/crossconnect

	private ArrayList<ArrayList<IP4Subnet>> availableSubnets;

	private static Logger logger;

	public IP4Assign_v2() {
		if (logger == null)	{
			logger = Logger.getLogger(IP4Assign_v2.class.getCanonicalName());
			logger.setLevel(Level.DEBUG);
		}
		
		allocatedSubnetTreeMap = new TreeMap<Integer, Integer>();
		allocatedSubnetMap = new HashMap<Object, IP4Subnet>();

		// create a list of IP4Subnet for each mask length
		availableSubnets = new ArrayList<ArrayList<IP4Subnet>>(32);
		for (int i = 0; i < 32; i++) {
			availableSubnets.add(i, new ArrayList<IP4Subnet>());
		}

		// Hardcoded pool of available subnets
		try {
			// 172.16.0.0/16
			availableSubnets.get(16).add(
					new IP4Subnet((Inet4Address) InetAddress
							.getByName("172.16.0.0"), 16));
		} catch (UnknownHostException e) {
			System.out.println("Exception: can not add subnet 172.16.0.0/16 to free list");
		}
		// 192.168.0.0/16
		// availableSubnets.get(16).add(new IP4Subnet("192.168.0.0",16));

		// 10.0.0.0/8
		// availableSubnets.get(16).add(new IP4Subnet("10.0.0.0",8));

	}

	private IP4Subnet getSubnetContainingSubnet(Inet4Address ip, int mask_length) {
		// Crappy data structure for this, but will work for now.
		for (int i = 0; i < 32; i++) {
			for (IP4Subnet s : availableSubnets.get(i)) {
				if (s.isInSubnet(ip)
						&& s.isInSubnet((Inet4Address) InetAddresses
								.fromInteger(InetAddresses.coerceToInteger(ip)
										+ s.getSizeFromMask(mask_length) - 1))) {
					availableSubnets.get(i).remove(s);
					return s;
				}

			}
		}
		return null;
	}

	private ArrayList<IP4Subnet> getAllOverlappingSubnets(Inet4Address ip,
			int mask_length) {
		// Crappy data structure for this, but will work for now.
		ArrayList<IP4Subnet> subnets = new ArrayList<IP4Subnet>();

		for (int i = 0; i < 32; i++) {
			ArrayList<IP4Subnet> remove_list = new ArrayList<IP4Subnet>();
			Iterator<IP4Subnet> iter = availableSubnets.get(i).iterator();
			while (iter.hasNext()) {
				IP4Subnet s = iter.next();
				if (s.doesOverlap(ip, mask_length)) {
					// availableSubnets.get(i).remove(s);
					remove_list.add(s);
					subnets.add(s);
				}

			}
			// remove from main list
			for (IP4Subnet s : remove_list) {
				availableSubnets.get(i).remove(s);
			}

		}

		return subnets;
	}

	private IP4Subnet getSubnet(Inet4Address ip, int mask_length) {
		logger.debug("getSubnet 1: " + ip + ", mask_lengh: " + mask_length);
		if (!isAvailable(ip, mask_length)) {
			// Should throw exception
			return null;
		}

		ArrayList<IP4Subnet> overlapping_subnets = getAllOverlappingSubnets(ip,
				mask_length);

		for (IP4Subnet s : overlapping_subnets) {
			logger.debug("getSubnet 50: removing " + s);
			availableSubnets.get(s.getMaskLength()).remove(s);

			if (!s.isInSubnet(ip))
				continue;

			// if partial overlap then find free portion and replace.
			while (s.getMaskLength() < mask_length) {
				logger.debug("getSubnet 200: " + s.getMaskLength());
				IP4Subnet s_split = s.split();
				if (s.isInSubnet(ip)) {
					logger.debug("getSubnet 210: ");
					availableSubnets.get(s_split.getMaskLength()).add(s_split);
				} else {
					logger.debug("getSubnet 220: ");
					availableSubnets.get(s.getMaskLength()).add(s);
					s = s_split;
				}
			}

		}

		return new IP4Subnet(ip, mask_length);

	}

	private IP4Subnet getAvailableSubnet(int count) {
		int mask_length = IP4Subnet.getMaskFromSize(count);

		// find smallest subnet bigger than count
		int i = mask_length;
		while (i > 0 && availableSubnets.get(i).isEmpty())
			i--;

		if (i <= 0) {
			logger.info("unable to getAvailableSubnet: i == " + i);
			return null;
		}

		// get subnet
		IP4Subnet s = availableSubnets.get(i).remove(0);

		// split subnet until is correct size
		while (i != mask_length) {
			availableSubnets.get(++i).add(s.split());
		}

		return s;

	}

	private void allocateSubnet(OrcaCrossconnect cc, int count) {
		IP4Subnet s = getAvailableSubnet(count);
		logger.debug("allocateSubnet(OrcaCrossconnect cc, int count): " + s);

		allocatedSubnetMap.put(cc, s);
		allocatedSubnetTreeMap.put(
				InetAddresses.coerceToInteger(s.getStartIP()), s.getSize());
	}

	private void allocateSubnet(OrcaCrossconnect cc, Inet4Address ip,
			int mask_length) {

		if (!isAvailable(ip, mask_length)) {
			// Should throw execption
			return;
		}

		IP4Subnet s = getSubnet(ip, mask_length);
		allocatedSubnetMap.put(cc, s);
		allocatedSubnetTreeMap.put(
				InetAddresses.coerceToInteger(s.getStartIP()), s.getSize());
	}

	private void allocateSubnet(OrcaLink l, int count) {
		IP4Subnet s = getAvailableSubnet(count);
		logger.debug("allocateSubnet(OrcaCrossconnect l, int count): " + s);

		allocatedSubnetMap.put(l, s);
		allocatedSubnetTreeMap.put(
				InetAddresses.coerceToInteger(s.getStartIP()), s.getSize());

	}

	private void allocateSubnet(OrcaLink l, Inet4Address ip, int mask_length) {
		if (!isAvailable(ip, mask_length))
			return;

		IP4Subnet s = getSubnet(ip, mask_length);
		allocatedSubnetMap.put(l, s);
		allocatedSubnetTreeMap.put(
				InetAddresses.coerceToInteger(s.getStartIP()), s.getSize());
	}

	public void allocateSubnet(OrcaResource r, int size) {
		if (r instanceof OrcaCrossconnect)
			allocateSubnet((OrcaCrossconnect) r, size);
		else if (r instanceof OrcaLink)
			allocateSubnet((OrcaLink) r, size);
		else
			logger.error("Cannot allocate subnet to OrcaResource of type "
					+ r.getClass().getName());

	}

	public void allocateSubnet(OrcaResource r, Inet4Address ip, int mask_length) {
		if (!isAvailable(ip, mask_length)) {
			logger.error("Cannot allocate subnet subnet allocated: " + ip + ", "
					+ mask_length);
		}

		if (r instanceof OrcaCrossconnect)
			allocateSubnet((OrcaCrossconnect) r, ip, mask_length);
		else if (r instanceof OrcaLink)
			allocateSubnet((OrcaLink) r, ip, mask_length);
		else
			logger.error("Cannot allocate subnet to OrcaResource of type "
					+ r.getClass().getName());

	}

	public void allocateSubnet(OrcaResource r, IP4Subnet s) {
		if (!isAvailable(s.getStartIP(), s.getMaskLength())) {
			// Should throw execption
			return;
		}

		allocatedSubnetMap.put(r, s);
		allocatedSubnetTreeMap.put(
				InetAddresses.coerceToInteger(s.getStartIP()), s.getSize());

	}

	public IP4Subnet addResouceToSubnet(OrcaResource new_resource,
			OrcaResource existing_resource) {
		IP4Subnet s = allocatedSubnetMap.get(existing_resource);

		allocatedSubnetMap.put(new_resource, s);

		return s;
	}

	public IP4Subnet getSubnetByResource(OrcaResource r) {
		return allocatedSubnetMap.get(r);
	}

	public Inet4Address getFreeIP(OrcaResource resource) {
		IP4Subnet s = allocatedSubnetMap.get(resource);

		if (s == null) {
			logger.error("Cannot getFreeIP (cannot find subnet for resource). "
					+ resource);
			return null;
		}

		return s.getFreeIP();
	}

	public int getSubnetMask(OrcaResource resource) {
		IP4Subnet s = allocatedSubnetMap.get(resource);

		if (s == null) {
			logger.error("Cannot getSubnetMask (cannot find subnet for resource). "
					+ resource);
			return 0;
		}

		return s.getMaskLength();
	}

	public void markIPsUsed(OrcaResource resource, Inet4Address ip, int size) {
		IP4Subnet s = allocatedSubnetMap.get(resource);

		if (s == null) {
			logger.error("Cannot mark IP used (cannot find subnet for resource). "
					+ ip + ", " + resource);
			return;
		}

		if (!s.isInSubnet(ip)
				|| !s.isInSubnet((Inet4Address) InetAddresses
						.fromInteger(InetAddresses.coerceToInteger(ip) + size))) {
			logger.error("Cannot mark IP used (IP not in subnet). " + ip + ", "
					+ s + ", size " + size);
			return;
		}

		s.markIPsUsed(ip, size);
	}

	public void markIPUsed(OrcaResource resource, Inet4Address ip) {
		IP4Subnet s = allocatedSubnetMap.get(resource);

		if (s == null) {
			logger.error("Cannot mark IP used (cannot find subnet for resource). "
					+ ip + ", " + resource);
			return;
		}

		if (!s.isInSubnet(ip)) {
			logger.error("Cannot mark IP used (IP not in subnet). " + ip + ", "
					+ s);
			return;
		}

		s.markIPUsed(ip);
	}

	public boolean isAvailable(Inet4Address ip, int mask_length) {
		int size = IP4Subnet.getSizeFromMask(mask_length);
		int ip_int = InetAddresses.coerceToInteger(ip);

		Entry<Integer, Integer> prev = allocatedSubnetTreeMap
				.floorEntry(ip_int);
		Entry<Integer, Integer> next = allocatedSubnetTreeMap
				.higherEntry(ip_int);

		logger.info("prev = " + prev);
		logger.info("next = " + next);
		logger.info("ip_int = " + ip_int + ", size = " + size);

		if (prev != null && prev.getKey() + prev.getValue() > ip_int) {
			logger.debug("prev != null && prev.getKey()+prev.getValue() >= ip_int");
			return false;
		}

		if (next != null && next.getKey() < ip_int + size) {
			logger.debug("next !=  null &&  next.getKey() <= ip_int + size");
			return false;
		}

		return true;
	}

	// is a managed subnet part of the proposed subnet?
	public boolean isManagedSubnet(Inet4Address ip, int mask_length) {
		int ip_int = InetAddresses.coerceToInteger(ip);

		Entry<Integer, Integer> prev = managedSubnetTreeMap.floorEntry(ip_int);
		Entry<Integer, Integer> next = managedSubnetTreeMap.higherEntry(ip_int);

		if (prev.getKey() + prev.getValue() < ip_int
				&& next.getKey() > ip_int
						+ IP4Subnet.getSizeFromMask(mask_length))
			return false;

		return true;
	}

	public String toString() {
		String rtnStr = "";

		rtnStr += "allocatedSubnetTreeMap: \n";
		for (Entry<Integer, Integer> e : allocatedSubnetTreeMap.entrySet()) {
			rtnStr += e + "\n";
		}

		rtnStr += "allocatedSubnetMap: \n";
		for (Entry<java.lang.Object, IP4Subnet> s : allocatedSubnetMap
				.entrySet()) {
			rtnStr += s + "\n";
		}

		rtnStr += "availableSubnets: \n";
		for (int i = 0; i < 32; i++) {
			rtnStr += "size " + i + ": \n";
			for (IP4Subnet s : availableSubnets.get(i)) {

				rtnStr += "\t" + s + "\n";
			}
		}
		return rtnStr;
	}

	public static void main(String[] argv) {

		IP4Assign_v2 ipassign = new IP4Assign_v2();

		System.out.println(ipassign);
	}

	public String getSubnetJsonString(OrcaResource n) {
		// GsonBuilder gson_builder = new GsonBuilder();
		// gson_builder.registerTypeAdapter(IP4Subnet.class, new
		// IP4SubnetSerializer());
		// Gson gson = gson_builder.create();
		Gson gson = new Gson();
		return gson.toJson(allocatedSubnetMap.get(n));
	}

	public static IP4Subnet createSubnetFromJsonString(String json) {
		System.out.println("IP4Subnet createSubnetFromJsonString(String json): json = " + json);
		
		GsonBuilder gson_builder = new GsonBuilder();
		gson_builder.registerTypeAdapter(IP4Subnet.class, new IP4SubnetDeserializer());
		Gson gson = gson_builder.create();
		// Gson gson = new Gson();
		return gson.fromJson(json, IP4Subnet.class);
	}

	private static class IP4SubnetDeserializer implements
			JsonDeserializer<IP4Subnet> {
		public IP4Subnet deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {

			System.out.println(json);

			JsonObject json_obj = json.getAsJsonObject();
			JsonObject json_subnet = json_obj.getAsJsonObject("subnet");

			System.out.println("json_subnet = " + json_subnet);
			String ip_str = json_subnet.getAsJsonPrimitive("ip").getAsString();
			int mask_length = json_subnet.getAsJsonPrimitive("mask_length")
					.getAsInt();

			JsonArray json_ips = json_subnet.getAsJsonArray("ip_avail");

			System.out.println("ip: " + ip_str);
			System.out.println("mask_length: " + mask_length);
			System.out.println("json_ips: " + json_ips);

			IP4Subnet s = null;
			try {
				s = new IP4Subnet((Inet4Address) InetAddress.getByName(ip_str),
						mask_length);
				s.markIPsUsed((Inet4Address) InetAddress.getByName(ip_str),
						IP4Subnet.getSizeFromMask(mask_length));

				Iterator<JsonElement> i = json_ips.iterator();
				while (i.hasNext()) {
					JsonPrimitive p = i.next().getAsJsonPrimitive();
					String[] ip = p.getAsString().split("/");
					System.out.println("ip[0]: " + ip[0]);
					System.out.println("ip[1]: " + ip[1]);
					System.out.println("IP: " + p.getAsString());

					s.markIPsFree((Inet4Address) InetAddress.getByName(ip[0]),
							IP4Subnet.getSizeFromMask(Integer.parseInt(ip[1])));
				}

			} catch (UnknownHostException e) {
				System.out.println("Caught UnknownHostException");
				return null;
			}

			return s;

		}
	}

	private static class IP4SubnetSerializer implements
			JsonSerializer<IP4Subnet> {
		public JsonElement serialize(IP4Subnet src, Type typeOfSrc,
				JsonSerializationContext context) {
			System.out.println("****** " + src.toJsonString());
			return new JsonPrimitive(src.toJsonString());

		}
	}*/

}