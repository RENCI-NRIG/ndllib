package orca.ndllib.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import orca.ndllib.ndl.RequestSaver;

import com.google.common.net.InetAddresses;

/**
 * Class to manage ip address assignments
 * @author ibaldin
 *
 */
public class IP4Assign {
	private ArrayList<IP4Subnet> managedSubnets;
	private ArrayList<IP4Subnet> allocatedSubnets;
	private ArrayList<IP4Subnet> availableSubnets;


	/**
	 * 
	 */
	public IP4Assign() {
		managedSubnets   = new ArrayList<IP4Subnet>();
		allocatedSubnets = new ArrayList<IP4Subnet>();
		availableSubnets = new ArrayList<IP4Subnet>();
		
		init();
	}
	
	private void init() {
		try {
			//hardcode available networks
			availableSubnets.add(new IP4Subnet((Inet4Address)Inet4Address.getByName("172.16.0.0"),20));
			
			managedSubnets.add(new IP4Subnet((Inet4Address)Inet4Address.getByName("172.16.0.0"),20));
		} catch (UnknownHostException e) {
			;
		}
	}

	/******************* API methods ******************/
	
	public IP4Subnet allocateSubnet(int size){
		return null;
	}

	public void markSubnetUsed(IP4Subnet subnet){
		
	}
	
	public void freeSubnet(IP4Subnet subnet){
		
	}
	
	
	/******************* Helper methods **************/
	
	
	
	/******************* OLD ***************************/
	
	/**
	 * Issues two unique addresses for p-to-p link
	 * Use getPPmask() to get the netmask for p-to-p links
	 * @return - array of 2 elements with addresses or null if no addresses are available
	 */
/*	public String[] getPPAddresses() {
		String[] ret = new String[2];

		if (InetAddresses.coerceToInteger(ppCurrent) == mpStartInt)
			return null;

		ret[0] = ppCurrent.getHostName();
		InetAddress tmp = InetAddresses.increment(ppCurrent);
		ret[1] = tmp.getHostName();

		int ppCurrentInt = InetAddresses.coerceToInteger(ppCurrent);
		ppCurrentInt += 1L << (32 - ppMaskSize);

		ppCurrent = InetAddresses.fromInteger(ppCurrentInt);

		return ret;
	}*/

	/**
	 * Uses the setting of MP mask to issue. 
	 * Use getMPMask() to get the netmask for mp links 
	 * @param ct - number of addresses needed 
	 * @return - array of addresses or null if no addresses are available
	 */
/*	public String[] getMPAddresses(int ct) {
		assert(ct > 0);

		if (ct > 1L << 32 - mpMaskSize)
			return null;

		String[] ret = new String[ct];

		InetAddress tmp = mpCurrent;
		for(int i = 0; ct > 0; ct--, i++) {
			ret[i] = tmp.getHostName();
			tmp = InetAddresses.increment(tmp);
		}

		int mpCurrentInt = InetAddresses.coerceToInteger(mpCurrent);
		mpCurrentInt += 1L << (32 - mpMaskSize);
		mpCurrent = InetAddresses.fromInteger(mpCurrentInt);

		return ret;
	}*/


}
