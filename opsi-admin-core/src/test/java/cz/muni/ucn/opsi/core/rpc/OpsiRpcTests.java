/**
 *
 */
package cz.muni.ucn.opsi.core.rpc;

import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jan Dosoudil
 *
 */
public class OpsiRpcTests {

	private static final String OPSI_SCHEMA = "https";
	private static final String OPSI_SERVER = "opsi.ucn.muni.cz";
	private static final int OPSI_PORT = 4447;

	private static RestTemplate template;

	@BeforeClass
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
		Credentials credentials = new UsernamePasswordCredentials("honza", "honzaPassword");
		httpClient.getState().setCredentials(new AuthScope(OPSI_SERVER, OPSI_PORT), credentials);

		CommonsClientHttpRequestFactory requestFactory = new CommonsClientHttpRequestFactory(httpClient);

		template = new RestTemplate(requestFactory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJacksonHttpMessageConverter());
		template.setMessageConverters(messageConverters);


	}

	@AfterClass
	public static void destroy() {
		template = null;
	}

	@Test
	public void test1() {

		String url = OPSI_SCHEMA + "://" + OPSI_SERVER + ":" + OPSI_PORT + "/rpc";
		OpsiRequest request = new OpsiRequest();
		request.setParams(new ArrayList<Object>());
		request.setId(1);
		request.setMethod("host_getObjects");

		HttpEntity<OpsiRequest> requestEntity = new HttpEntity<OpsiRequest>(request);
		ResponseEntity<OpsiResponse> responseEntity = template.exchange(
				url, HttpMethod.POST, requestEntity, OpsiResponse.class);

		OpsiResponse response = responseEntity.getBody();
		response.toString();
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
