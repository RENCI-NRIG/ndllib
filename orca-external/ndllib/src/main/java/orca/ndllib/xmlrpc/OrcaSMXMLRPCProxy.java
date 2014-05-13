package orca.ndllib.xmlrpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import orca.ndllib.NDLLIB;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;


public class OrcaSMXMLRPCProxy extends OrcaXMLRPCBase {
	private static final String RET_RET_FIELD = "ret";
	private static final String MSG_RET_FIELD = "msg";
	private static final String ERR_RET_FIELD = "err";
	private static final String GET_VERSION = "orca.getVersion";
	private static final String SLICE_STATUS = "orca.sliceStatus";
	private static final String CREATE_SLICE = "orca.createSlice";
	private static final String DELETE_SLICE = "orca.deleteSlice";
	private static final String MODIFY_SLICE = "orca.modifySlice";
	private static final String RENEW_SLICE = "orca.renewSlice";
	private static final String LIST_SLICES = "orca.listSlices";
	private static final String LIST_RESOURCES = "orca.listResources";
	private static final String SSH_DSA_PUBKEY_FILE = "id_dsa.pub";
	private static final String SSH_RSA_PUBKEY_FILE = "id_rsa.pub";

	OrcaSMXMLRPCProxy() {
		;
	}

	private static OrcaSMXMLRPCProxy instance = new OrcaSMXMLRPCProxy();

	public static OrcaSMXMLRPCProxy getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getVersion() throws Exception {
		Map<String, Object> versionMap = null;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// get verbose list of the AMs
			versionMap = (Map<String, Object>)client.execute(GET_VERSION, new Object[]{});
		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());
		}
		return versionMap;
	}

	/** submit an ndl request to create a slice, using explicitly specified users array
	 * 
	 * @param sliceId
	 * @param resReq
	 * @param users
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String createSlice(String sliceId, String resReq, List<Map<String, ?>> users) throws Exception {
		assert(sliceId != null);
		assert(resReq != null);

		String result = null;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		Map<String, Object> rr = null;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// create sliver
			rr = (Map<String, Object>)client.execute(CREATE_SLICE, new Object[]{ sliceId, new Object[]{}, resReq, users});
		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			return "Unable to submit slice to SM:  " + NDLLIB.getInstance().getSelectedController() + " due to " + e;
		}

		if (rr == null)
                        throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());

		if ((Boolean)rr.get(ERR_RET_FIELD))
			throw new Exception("Unable to create slice: " + (String)rr.get(MSG_RET_FIELD));

		result = (String)rr.get(RET_RET_FIELD);
		return result;
	}

	/** submit an ndl request to create a slice, using explicitly specified users array
	 * 
	 * @param sliceId
	 * @param resReq
	 * @param users
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean renewSlice(String sliceId, Date newDate) throws Exception {
		assert(sliceId != null);

		Boolean result = false;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		Map<String, Object> rr = null;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// create sliver
			Calendar ecal = Calendar.getInstance();
			ecal.setTime(newDate);
			String endDateString = DatatypeConverter.printDateTime(ecal); // RFC3339/ISO8601
			rr = (Map<String, Object>)client.execute(RENEW_SLICE, new Object[]{ sliceId, new Object[]{}, endDateString});
		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());
		}

		if (rr == null)
                        throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());

		if ((Boolean)rr.get(ERR_RET_FIELD))
			throw new Exception("Unable to renew slice: " + (String)rr.get(MSG_RET_FIELD));

		result = (Boolean)rr.get(RET_RET_FIELD);
		return result;
	}

	/**
	 * submit an ndl request to create a slice using this user's credentials
	 * @param sliceId
	 * @param resReq
	 * @param users
	 * @return
	 */
	public String createSlice(String sliceId, String resReq) throws Exception {
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		// collect user credentials from $HOME/.ssh

		// create an array
		List<Map<String, ?>> users = new ArrayList<Map<String, ?>>();
		String keyPathStr = NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.SSH_PUBKEY);
		File keyPath;
		if (keyPathStr.startsWith("~/")) {
			keyPathStr = keyPathStr.replaceAll("~/", "/");
			keyPath = new File(System.getProperty("user.home"), keyPathStr);
		}
		else {
			keyPath = new File(keyPathStr);
		}

		String userKey = getUserKeyFile(keyPath);

		if (userKey == null) 
			throw new Exception("Unable to load user public ssh key " + keyPath);

		Map<String, Object> userEntry = new HashMap<String, Object>();

		userEntry.put("login", "root");
		List<String> keys = new ArrayList<String>();
		keys.add(userKey);

		// any additional keys?
		keyPathStr = NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.SSH_OTHER_PUBKEY);
		if (keyPathStr.startsWith("~/")) {
			keyPathStr = keyPathStr.replaceAll("~/", "/");
			keyPath = new File(System.getProperty("user.home"), keyPathStr);
		}
		else {
			keyPath = new File(keyPathStr);
		}
		String otherUserKey = getUserKeyFile(keyPath);

		if (otherUserKey != null) {
			if (NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.SSH_OTHER_LOGIN).equals("root")) {
				keys.add(otherUserKey);
			} else {
				Map<String, Object> otherUserEntry = new HashMap<String, Object>();
				otherUserEntry.put("login", NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.SSH_OTHER_LOGIN));
				otherUserEntry.put("keys", Collections.singletonList(otherUserKey));
				otherUserEntry.put("sudo", NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.SSH_OTHER_SUDO));
				users.add(otherUserEntry);
			}
		}

		userEntry.put("keys", keys);
		users.add(userEntry);
		
		// submit the request
		return createSlice(sliceId, resReq, users);
	}

	@SuppressWarnings("unchecked")
	public boolean deleteSlice(String sliceId)  throws Exception {
		boolean res = false;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		Map<String, Object> rr = null;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// delete sliver
			rr = (Map<String, Object>)client.execute(DELETE_SLICE, new Object[]{ sliceId, new Object[]{}});
		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());
		}

		if (rr == null)
                        throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());

		if ((Boolean)rr.get(ERR_RET_FIELD))
			throw new Exception("Unable to delete slice: " + (String)rr.get(MSG_RET_FIELD));
		else
			res = (Boolean)rr.get(RET_RET_FIELD);

		return res;
	}

	@SuppressWarnings("unchecked")
	public String sliceStatus(String sliceId)  throws Exception {
		assert(sliceId != null);

		String result = null;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		Map<String, Object> rr = null;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// sliver status
			rr = (Map<String, Object>)client.execute(SLICE_STATUS, new Object[]{ sliceId, new Object[]{}});

		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());
		}

		if (rr == null)
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());

		if ((Boolean)rr.get(ERR_RET_FIELD))
			throw new Exception("Unable to get sliver status: " + rr.get(MSG_RET_FIELD));

		result = (String)rr.get(RET_RET_FIELD);

		return result;
	}

	@SuppressWarnings("unchecked")
	public String[] listMySlices() throws Exception {
		String[] result = null;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		Map<String, Object> rr = null;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// sliver status
			rr = (Map<String, Object>)client.execute(LIST_SLICES, new Object[]{ new Object[]{}});
		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());
		}

		if (rr == null)
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());

		if ((Boolean)rr.get(ERR_RET_FIELD))
			throw new Exception ("Unable to list active slices: " + rr.get(MSG_RET_FIELD));

		Object[] ll = (Object[])rr.get(RET_RET_FIELD);
		if (ll.length == 0)
			return new String[0];
		else {
			result = new String[ll.length];
			for (int i = 0; i < ll.length; i++)
				result[i] = (String)((Object[])rr.get(RET_RET_FIELD))[i];
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public String modifySlice(String sliceId, String modReq) throws Exception {
		assert(sliceId != null);
		assert(modReq != null);

		String result = null;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		Map<String, Object> rr = null;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// modify slice
			rr = (Map<String, Object>)client.execute(MODIFY_SLICE, new Object[]{ sliceId, new Object[]{}, modReq});
		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());
		}

		if (rr == null)
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());

		if ((Boolean)rr.get(ERR_RET_FIELD))
			throw new Exception("Unable to modify slice: " + (String)rr.get(MSG_RET_FIELD));

		result = (String)rr.get(RET_RET_FIELD);
		return result;
	}

	@SuppressWarnings("unchecked")
	public String listResources() throws Exception {

		String result = null;
		setSSLIdentity(null, NDLLIB.getInstance().getSelectedController());

		Map<String, Object> rr = null;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getSelectedController()));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			// set this transport factory for host-specific SSLContexts to work
			XmlRpcCommonsTransportFactory f = new XmlRpcCommonsTransportFactory(client);
			client.setTransportFactory(f);

			// modify slice
			rr = (Map<String, Object>)client.execute(LIST_RESOURCES, new Object[]{ new Object[]{}, new HashMap<String, String>()});
		} catch (MalformedURLException e) {
			throw new Exception("Please check the SM URL " + NDLLIB.getInstance().getSelectedController());
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController() + " due to " + e);
		} catch (Exception e) {
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());
		}

		if (rr == null)
			throw new Exception("Unable to contact SM " + NDLLIB.getInstance().getSelectedController());

		if ((Boolean)rr.get(ERR_RET_FIELD))
			throw new Exception("Unable to list resources: " + (String)rr.get(MSG_RET_FIELD));

		result = (String)rr.get(RET_RET_FIELD);
		return result;
	}

	/**
	 * Try to get a public key file, first DSA, then RSA
	 * @return
	 */
	private String getAnyUserPubKey() {
		Properties p = System.getProperties();

		String keyFilePathStr = "" + p.getProperty("user.home") + p.getProperty("file.separator") + ".ssh" +
		p.getProperty("file.separator") + SSH_DSA_PUBKEY_FILE;
		File keyFilePath = new File(keyFilePathStr);

		String userKey = getUserKeyFile(keyFilePath);
		if (userKey == null) {
			keyFilePathStr = "" + p.getProperty("user.home") + p.getProperty("file.separator") + ".ssh" + 
			p.getProperty("file.separator") + SSH_RSA_PUBKEY_FILE;
			keyFilePath = new File(keyFilePathStr);
			userKey = getUserKeyFile(keyFilePath);
			if (userKey == null) {
				
				return null;
			}
		}
		return userKey;
	}

	private String getUserKeyFile(File path) {
		try {
			FileInputStream is = new FileInputStream(path);
			BufferedReader bin = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = bin.readLine()) != null) {
				sb.append(line);
				// re-add line separator
				sb.append(System.getProperty("line.separator"));
			}

			bin.close();

			return sb.toString();

		} catch (IOException e) {
			return null;
		}
	}


	/**
	 * Test harness
	 * @param args
	 */
	public static void main(String[] args) {
		OrcaSMXMLRPCProxy p = OrcaSMXMLRPCProxy.getInstance();

		if (args.length != 1) {
			System.err.println("You must specify the request filename");
			System.exit(1);
		}

		StringBuilder sb = null;
		try {
			BufferedReader bin = null;
			File f = new File(args[0]);
			FileInputStream is = new FileInputStream(f);
			bin = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			sb = new StringBuilder();
			String line = null;
			while((line = bin.readLine()) != null) {
				sb.append(line);
				// re-add line separator
				sb.append(System.getProperty("line.separator"));
			}

			bin.close();
		} catch (Exception e) {
			System.err.println("Error "  + e + " encountered while readling file " + args[0]);
			System.exit(1);
		} finally {
			;
		}

		try {
			System.out.println("Placing request against " + NDLLIB.getInstance().getSelectedController());
			String sliceId = UUID.randomUUID().toString();
			System.out.println("Creating slice " + sliceId);
			String result = p.createSlice(sliceId, sb.toString());
			System.out.println("Result of create slice: " + result);

			System.out.println("Sleeping for 60sec");
			Thread.sleep(60000);

			System.out.println("Requesting sliver status");
			System.out.println("Status: " + p.sliceStatus(sliceId));

			//			System.out.println("Deleting slice " + sliceId);
			//			System.out.println("Result of delete slice: " + p.deleteSliver(sliceId));
		} catch (Exception e) {
			System.err.println("An exception has occurred in creating slice " + e);
		}

	}
}
