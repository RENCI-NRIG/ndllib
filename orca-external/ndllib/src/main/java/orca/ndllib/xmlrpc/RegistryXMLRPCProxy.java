package orca.ndllib.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import orca.ndllib.NDLLIB;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;



/**
 * XMLRPC Proxy singleton for ORCA Actor Registry (partial)
 * @author ibaldin
 *
 */
public class RegistryXMLRPCProxy {
	private static final String GET_AMS = "registryService.getAMs";
	private static final String GET_IMAGES = "registryService.getAllImages";
	private static final String GET_DEFAULT_IMAGE = "registryService.getDefaultImage";
	
	// fields returned by the registry for actors
	public enum Field {
		DESCRIPTION("DESC"),
		FULLRDF("FULLRDF");
		
		private final String name;
		
		private Field (String n) {
			name = n;
		}
		
		public String getName() {
			return name;
		}
	}
	
	private byte[] registryCertDigest;
	
	private RegistryXMLRPCProxy() {
		// singleton
        // get registry cert fingerprint
    	String[] fingerPrintBytes = NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY_CERT_FINGERPRINT).split(":");
    	
    	registryCertDigest = new byte[16];
    	
    	for (int i = 0; i < 16; i++ )
    		registryCertDigest[i] = (byte)(Integer.parseInt(fingerPrintBytes[i], 16) & 0xFF);
        
    	// Create a trust manager that does not validate certificate chains
        // so we can speak to the registry
    	TrustManager[] trustAllCerts = new TrustManager[] {
    			new X509TrustManager() {
    				public X509Certificate[] getAcceptedIssuers() {
    					return null;
    				}

    				public void checkClientTrusted(X509Certificate[] certs, String authType) {
    					// Trust always
    				}

    				public void checkServerTrusted(X509Certificate[] certs, String authType) {
    					// Trust always
    					MessageDigest md = null;
    					try {
    						md = MessageDigest.getInstance("MD5");

    						if (certs.length == 0) 
    							throw new CertificateException();

    						byte[] certDigest = md.digest(certs[0].getEncoded());
    						if (!Arrays.equals(certDigest, registryCertDigest)) {
    							throw new CertificateException();
    						}
    					} catch (NoSuchAlgorithmException e) {
    						;
    					} catch (Exception e) {
//    						ExceptionDialog ed = new ExceptionDialog(NDLLIB.getInstance().getFrame(), "Exception");
//    						ed.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//    						ed.setException("Exception encountered while contacting ORCA registry " + 
//    								NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY), e);
//    						ed.setVisible(true);
    					}
    				}
    			}
    	};
     
        // Install the all-trusting trust manager
        try {
        	SSLContext sc = SSLContext.getInstance("TLS");
        	// Create empty HostnameVerifier
        	HostnameVerifier hv = new HostnameVerifier() {
        		public boolean verify(String arg0, SSLSession arg1) {
        			return true;
        		}
        	};

        	sc.init(null, trustAllCerts, new java.security.SecureRandom());
        	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        	HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (NoSuchAlgorithmException e1) {

        } catch (KeyManagementException e2) {

        }
	}
	
	private static RegistryXMLRPCProxy instance = new RegistryXMLRPCProxy();
	
	public static RegistryXMLRPCProxy getInstance() {
		return instance;
	}
	
	/**
	 * Get data on all known AMs
	 * @param verbose
	 * @return
	 */
	public Map<String, Map<String, String>> getAMs(boolean verbose) throws Exception {
        // call the actor registry
        Map<String, Map<String, String>> amData = null;
        try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY)));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			
			// get verbose list of the AMs
			amData = (Map<String, Map<String, String>>)client.execute(GET_AMS, new Object[]{!verbose});
        } catch (MalformedURLException e) {
        	throw new Exception("Please check the registry URL " + NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY));
        } catch (XmlRpcException e) {
        	throw new Exception("Unable to contact registry " + NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY) + " due to " + e);
        }
		return amData;
	}
	
	/**
	 * Get specific field from the map (field names guaranteed to be typo free, use values from RegistrXMLRPCProxy.Field enum)
	 * @param k
	 * @param m
	 * @param f
	 * @return
	 */
	public static String getField(String k, Map<String, Map< String, String>> m, Field f) {
		if (!m.containsKey(k))
			return null;
		return m.get(k).get(f.getName());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getImages() throws Exception {
		List<Map<String, String>> ret = null;
		
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY)));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
		
			// get verbose list of the AMs
			Object[] rret = (Object[])client.execute(GET_IMAGES, new Object[]{});
			ret = new ArrayList<Map<String, String>>();
			for (Object n: rret) {
				if (n instanceof HashMap<?, ?>) {
					ret.add((HashMap<String, String>)n);
				}
			}
		} catch (MalformedURLException e) {
			throw new Exception("Please check the registry URL " + NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY));
		} catch (XmlRpcException e) {
			throw new Exception("Unable to contact registry " + NDLLIB.getInstance().getPreference(NDLLIB.PrefsEnum.ORCA_REGISTRY) + " due to " + e);
		}
		return ret;
	}
}
