/**
 *
 */
package cz.muni.ucn.opsi.core.opsiClient;

import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.instalation.Instalation;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;

/**
 * @author Jan Dosoudil
 *
 */
//@Service
public class OpsiClientServiceImpl implements OpsiClientService, InitializingBean {

	private RestTemplate template;

	private URL opsiUrl;
	private String userName;
	private String password;

	private AtomicInteger sequence = new AtomicInteger();

	public void init() {



		try {
			new URL("https://0.0.0.0/").getContent();
		} catch (IOException e) {
			// This invocation will always fail, but it will register the
			// default SSL provider to the URL class.
		}

		try{
			SSLContext sslc;

			sslc = SSLContext.getInstance("TLS");

			TrustManager[] trustManagerArray = { new X509TrustManager() {

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

				}

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

				}
			} };
			sslc.init(null, trustManagerArray, null);

			HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

		} catch(Exception e){
			e.printStackTrace();
		}

		Protocol easyhttps = new Protocol("https",
				(ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);

		HttpClient httpClient = new HttpClient();
		Credentials credentials = new UsernamePasswordCredentials(userName, password);
		int port = opsiUrl.getPort();
		if (-1 == port) {
			port = "https".equalsIgnoreCase(opsiUrl.getProtocol()) ? 443 : 80;
		}
		httpClient.getState().setCredentials(new AuthScope(opsiUrl.getHost(), port), credentials);

		CommonsClientHttpRequestFactory requestFactory = new CommonsClientHttpRequestFactory(httpClient);

		template = new RestTemplate(requestFactory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJacksonHttpMessageConverter());
		template.setMessageConverters(messageConverters);

	}



	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#createClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	public void createClient(Client client) {

		String name = client.getName();
		int indexOf = name.indexOf(".");

		String hostname = name.substring(0, indexOf);
		String domainname = name.substring(indexOf + 1);
		String description = client.getDescription();
		String notes = "";
		String ipaddress = client.getIpAddress();
		String macaddress = client.getMacAddress();

		OpsiResponse response = callOpsi("createClient", hostname, domainname,
				description, notes, ipaddress, macaddress);

		response.toString();

	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#deleteClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	public void deleteClient(Client client) {
		callOpsi("deleteClient", client.getName());

	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#updateClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	public void updateClient(Client client) {
		callOpsi("setMacAddress", client.getName(), client.getMacAddress());
		String description = client.getDescription();
		if (null == description) {
			description = "";
		}
		callOpsi("setHostDescription", client.getName(), description);
//		callOpsi("setHostNotes", client.getName(), client.getIpAddress());
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#listInstalations()
	 */
	@Override
	public List<Instalation> listInstalations() {

		OpsiResponse responseProds = callOpsi("getProducts_hash");

		@SuppressWarnings("unchecked")
		Map<String, Map<String, Map<String, String>>> depoMap =
			(Map<String, Map<String, Map<String, String>>>) responseProds.getResult();
		Map<String, Map<String, String>> prodMap = depoMap.values().iterator().next();

		OpsiResponse response = callOpsi("getNetBootProductIds_list", null, null);

		@SuppressWarnings("unchecked")
		List<Object> res = (List<Object>) response.getResult();
		List<Instalation> ret = new ArrayList<Instalation>();
		for (Object o : res) {
			Instalation inst = new Instalation();
			String id = (String) o;
			inst.setId(id);
			Map<String, String> prod = prodMap.get(id);
			if (null != prod) {
				inst.setName(prod.get("name"));
			} else {
				inst.setId(id);
			}
			ret.add(inst);
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#getIntalationById(java.lang.String)
	 */
	@Override
	public Instalation getIntalationById(String instalationId) {
		OpsiResponse responseProds = callOpsi("getProduct_hash", instalationId);

		@SuppressWarnings("unchecked")
		Map<String, String> produktMap = (Map<String, String>) responseProds.getResult();

		Instalation inst = new Instalation();
		String id = produktMap.get("productId");
		inst.setId(id);
		inst.setName(produktMap.get("name"));

		return inst;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#clientInstall(cz.muni.ucn.opsi.api.client.Client, cz.muni.ucn.opsi.api.instalation.Instalation)
	 */
	@Override
	public void clientInstall(Client client, Instalation i) {
		callOpsi("setProductActionRequest", i.getId(), client.getName(), "setup");

	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#listClientsForImport()
	 */
	@Override
	public List<Client> listClientsForImport() {
		OpsiResponse response = callOpsi("getClients_listOfHashes");

		@SuppressWarnings("unchecked")
		List<Map<String, String>> clientMaps = (List<Map<String, String>>) response.getResult();
		List<Client> ret = new ArrayList<Client>(clientMaps.size());
		for (Map<String,String> map : clientMaps) {
			Client c = new Client(UUID.randomUUID());
			c.setName(map.get("hostId"));
			c.setDescription(map.get("description"));

			String macAddress = map.get("hardwareAddress");
			if (StringUtils.isBlank(macAddress)) {
				macAddress = null;
			}
			c.setMacAddress(macAddress);

			String ipAddress = map.get("ipAddress");
			if (StringUtils.isBlank(ipAddress)) {
				ipAddress = null;
			}
			c.setIpAddress(ipAddress);
			ret.add(c);
		}

		return ret;
	}

	/**
	 * @return
	 */
	protected int getRequestId() {
		return sequence.incrementAndGet();
	}

	/**
	 *
	 * @param method
	 * @param args
	 * @return
	 */
	protected OpsiResponse callOpsi(String method, Object... args) {
		OpsiRequest requestProds = new OpsiRequest();
		requestProds.setParams(Arrays.asList(args));
		requestProds.setId(getRequestId());
		requestProds.setMethod(method);

		HttpEntity<OpsiRequest> requestEntity = new HttpEntity<OpsiRequest>(requestProds);
		ResponseEntity<OpsiResponse> responseProdsEntity = template.exchange(
				opsiUrl.toString(), HttpMethod.POST, requestEntity, OpsiResponse.class);

		OpsiResponse response = responseProdsEntity.getBody();

		return response;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	/**
	 * @param opsiUrl the opsiUrl to set
	 */
	@Required
	public void setOpsiUrl(URL opsiUrl) {
		this.opsiUrl = opsiUrl;
	}

	/**
	 * @param userName the userName to set
	 */
	@Required
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param password the password to set
	 */
	@Required
	public void setPassword(String password) {
		this.password = password;
	}

	public static class OpsiRequest {
		private int id;
		private String method;
		private List<Object> params;
		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}
		/**
		 * @return the method
		 */
		public String getMethod() {
			return method;
		}
		/**
		 * @param method the method to set
		 */
		public void setMethod(String method) {
			this.method = method;
		}
		/**
		 * @return the params
		 */
		public List<Object> getParams() {
			return params;
		}
		/**
		 * @param params the params to set
		 */
		public void setParams(List<Object> params) {
			this.params = params;
		}


	}

	public static class OpsiResponse {
		private int id;
		private Object result;
		private String error;
		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}
		/**
		 * @return the result
		 */
		public Object getResult() {
			return result;
		}
		/**
		 * @param result the result to set
		 */
		public void setResult(Object result) {
			this.result = result;
		}
		/**
		 * @return the error
		 */
		public String getError() {
			return error;
		}
		/**
		 * @param error the error to set
		 */
		public void setError(String error) {
			this.error = error;
		}


	}

}
