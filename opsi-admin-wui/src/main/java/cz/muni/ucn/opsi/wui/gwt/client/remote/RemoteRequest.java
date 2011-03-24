/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.remote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * @author Jan Dosoudil
 *
 */
public abstract class RemoteRequest<T> {

	private RequestBuilder builder;

	/**
	 * @param url
	 * @param method
	 */
	public RemoteRequest(Method method, String url) {
		super();
		builder = new RequestBuilder(method, url);
	}

	/**
	 *
	 * @param requestData
	 */
	public void setRequestData(String requestData) {
		builder.setRequestData(requestData);
	}

	/**
	 *
	 * @param header
	 * @param value
	 */
	public void setHeader(String header, String value) {
		builder.setHeader(header, value);
	}

	/**
	 * @return the builder
	 */
	public RequestBuilder getBuilder() {
		return builder;
	}

	/**
	 *
	 * @param callback
	 */
	public void execute(final RemoteRequestCallback<T> callback) {
		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				try {
					T v = processResponse(request, response);
					callback.onRequestSuccess(v);
				} catch (Exception e) {
					GWT.log("onResponseReceived exception: " + e);
					callback.onRequestFailed(e);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				GWT.log("onError: " + exception);
				callback.onRequestFailed(exception);
			}
		});

		try {
			builder.send();
		} catch (RequestException e) {
			GWT.log("Chyba při odesílání požadavku", e);
			callback.onRequestFailed(e);
		}

	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	protected T processResponse(Request request, Response response) {
		int statusCode = response.getStatusCode();
		if (200 != statusCode) {
			GWT.log("Server odpovedel chybou pri odeslani pozadavku: " + response.getStatusText());
			pocessError(request, response);
		}

		String text = response.getText();
		if ("".equals(text)) {
			return null;
		}
		return transformResponse(text);
	}

	/**
	 * @param request
	 * @param response
	 */
	protected void pocessError(Request request, Response response) {
		String contentType = response.getHeader("Content-Type");
		String statusText = response.getStatusText();

		if (contentType.startsWith("application/json")) {
			JSONObject error = JSONParser.parseStrict(response.getText()).isObject();
			statusText = error.get("message").isString().stringValue();
		}
		throw new RemoteRequestException(statusText);
	}

	/**
	 * @param text
	 * @return
	 */
	protected abstract T transformResponse(String text);
}
