/**
 *
 */
package cz.muni.ucn.opsi.wui.gwtLogin.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

/**
 * @author Jan Dosoudil
 *
 */
public class LoginService {

	private static final String LOGIN_URL = "j_spring_security_check";
	private static final String LOGIN_STATUS_URL = "remote/loginStatus";

	private static final LoginService INSTANCE = new LoginService();

	/**
	 *
	 */
	private LoginService() {
	}

	/**
	 * @return
	 */
	public static LoginService getInstance() {
		return LoginService.INSTANCE;
	}

	/**
	 *
	 * @param username
	 * @param password
	 * @param callback
	 */
	public void login(String username, String password, final LoginCallback callback) {

		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
				GWT.getHostPageBaseURL() + LOGIN_URL);
		rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
		rb.setRequestData("j_username=" + URL.encode(username)
				+ "&j_password=" + URL.encode(password)
				+ "&ajax=true");

		rb.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response.getStatusCode() != Response.SC_OK) {
					callback.onLoginFailed(response.getStatusText());
					return;
				}

				JSONObject jsonObject = (JSONObject) JSONParser.parseStrict(response.getText());
				JSONString resultString = (JSONString) jsonObject.get("status");

				if ("OK".equalsIgnoreCase(resultString.stringValue())) {
					callback.onLoginOk(jsonObject);
				} else {
					String message = ((JSONString)jsonObject.get("message")).stringValue();
					callback.onLoginFailed(message);
				}

			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onLoginFailed(exception.getMessage());
			}


		});

		try {
			rb.send();
		} catch (RequestException e) {
		}

	}

	/**
	 * @author Jan Dosoudil
	 *
	 */
	public interface LoginCallback {
		void onLoginOk(JSONObject loginStatus);
		void onLoginFailed(String message);
	}

	public void getLoginStatus(final LoginStatusCallback callback) {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + LOGIN_STATUS_URL));

		builder.setCallback(new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 != response.getStatusCode()) {
						GWT.log("Server odpovedel chybou pri ziskani stavu prihlasseni: " + response.getStatusText());
						callback.onStatusFailed(response.getStatusText());
						return;
					}

					JSONObject object = (JSONObject) JSONParser.parseStrict(response.getText());
					callback.onStatusOk(object);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					GWT.log("Nelze odeslat pozadavek na zjisteni stavu prihlaseni", exception);
					callback.onStatusFailed(exception.getMessage());
				}
			});
		try {
			builder.send();
		} catch (RequestException e) {
			GWT.log("Chyba při odesílání požadavku", e);
			callback.onStatusFailed(e.getMessage());
		}


	}


	/**
	 * @author Jan Dosoudil
	 *
	 */
	public interface LoginStatusCallback {
		void onStatusOk(JSONObject status);
		void onStatusFailed(String message);
	}

}
