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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.instalation.Instalation;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;

/**
 * @author Jan Dosoudil
 *
 */
@Service
public class OpsiClientServiceImpl implements OpsiClientService, InitializingBean {

	private static final String OPSI_SCHEMA = "https";
	private static final String OPSI_SERVER = "opsi.ucn.muni.cz";
	private static final int OPSI_PORT = 4447;
	private static final String OPSI_URL = OPSI_SCHEMA + "://" + OPSI_SERVER + ":" + OPSI_PORT + "/rpc";

	private static RestTemplate template;

	public static void init() {

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
		Credentials credentials = new UsernamePasswordCredentials("opsiuser", "Kaka0.Pramen");
		httpClient.getState().setCredentials(new AuthScope(OPSI_SERVER, OPSI_PORT), credentials);

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

		OpsiRequest request = new OpsiRequest();
		String name = client.getName();
		int indexOf = name.indexOf(".");

		String hostname = name.substring(0, indexOf);
		String domainname = name.substring(indexOf + 1);
		String description = client.getDescription();
		String notes = "";
		String ipaddress = client.getIpAddress();
		String macaddress = client.getMacAddress();
		request.setParams(Arrays.asList(new Object[] {
				hostname, domainname, description, notes, ipaddress, macaddress}));
		request.setId(1);
		request.setMethod("createClient");

		HttpEntity<OpsiRequest> requestEntity = new HttpEntity<OpsiRequest>(request);
		ResponseEntity<OpsiResponse> responseEntity = template.exchange(
				OPSI_URL, HttpMethod.POST, requestEntity, OpsiResponse.class);

		OpsiResponse response = responseEntity.getBody();



		response.toString();

	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#deleteClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	public void deleteClient(Client client) {
		OpsiRequest requestProds = new OpsiRequest();
		requestProds.setParams(Arrays.asList(new Object[] {client.getName()}));
		requestProds.setId(5);
		requestProds.setMethod("deleteClient");

		HttpEntity<OpsiRequest> requestProdsEntity = new HttpEntity<OpsiRequest>(requestProds);
		ResponseEntity<OpsiResponse> responseProdsEntity = template.exchange(
				OPSI_URL, HttpMethod.POST, requestProdsEntity, OpsiResponse.class);

		OpsiResponse responseProds = responseProdsEntity.getBody();

	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#updateClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	public void updateClient(Client client) {
		// TODO Auto-generated method stub


	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.opsiClient.OpsiClientService#listInstalations()
	 */
	@Override
	public List<Instalation> listInstalations() {
		OpsiRequest requestProds = new OpsiRequest();
		requestProds.setParams(Arrays.asList(new Object[] {}));
		requestProds.setId(2);
		requestProds.setMethod("getProducts_hash");

		HttpEntity<OpsiRequest> requestProdsEntity = new HttpEntity<OpsiRequest>(requestProds);
		ResponseEntity<OpsiResponse> responseProdsEntity = template.exchange(
				OPSI_URL, HttpMethod.POST, requestProdsEntity, OpsiResponse.class);

		OpsiResponse responseProds = responseProdsEntity.getBody();

		@SuppressWarnings("unchecked")
		Map<String, Map<String, Map<String, String>>> depoMap =
			(Map<String, Map<String, Map<String, String>>>) responseProds.getResult();
		Map<String, Map<String, String>> prodMap = depoMap.values().iterator().next();

		OpsiRequest request = new OpsiRequest();
		request.setParams(Arrays.asList(new Object[] {null, null}));
		request.setId(3);
		request.setMethod("getNetBootProductIds_list");

		HttpEntity<OpsiRequest> requestEntity = new HttpEntity<OpsiRequest>(request);
		ResponseEntity<OpsiResponse> responseEntity = template.exchange(
				OPSI_URL, HttpMethod.POST, requestEntity, OpsiResponse.class);

		OpsiResponse response = responseEntity.getBody();
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
		OpsiRequest requestProds = new OpsiRequest();
		requestProds.setParams(Arrays.asList(new Object[] {instalationId}));
		requestProds.setId(4);
		requestProds.setMethod("getProduct_hash");

		HttpEntity<OpsiRequest> requestProdsEntity = new HttpEntity<OpsiRequest>(requestProds);
		ResponseEntity<OpsiResponse> responseProdsEntity = template.exchange(
				OPSI_URL, HttpMethod.POST, requestProdsEntity, OpsiResponse.class);

		OpsiResponse responseProds = responseProdsEntity.getBody();

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
		OpsiRequest requestProds = new OpsiRequest();
		requestProds.setParams(Arrays.asList(new Object[] {i.getId(), client.getName(), "setup"}));
		requestProds.setId(6);
		requestProds.setMethod("setProductActionRequest");

		HttpEntity<OpsiRequest> requestProdsEntity = new HttpEntity<OpsiRequest>(requestProds);
		ResponseEntity<OpsiResponse> responseProdsEntity = template.exchange(
				OPSI_URL, HttpMethod.POST, requestProdsEntity, OpsiResponse.class);

		OpsiResponse responseProds = responseProdsEntity.getBody();

	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
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
