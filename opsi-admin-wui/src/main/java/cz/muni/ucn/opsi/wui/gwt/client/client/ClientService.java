/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstalaceJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequest;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientService {

	private static final ClientService INSTANCE = new ClientService();

	private static final String CLIENT_LIST_URL = "remote/clients/list";
	private static final String CLIENT_CREATE_URL = "remote/clients/create";
	private static final String CLIENT_EDIT_URL = "remote/clients/edit";
	private static final String CLIENT_SAVE_URL = "remote/clients/save";
	private static final String CLIENT_DELETE_URL = "remote/clients/delete";
	private static final String CLIENT_INSTALL_URL = "remote/clients/install";
	private static final String CLIENT_IMPORT_LIST_URL = "remote/clients/import/list";
	private static final String CLIENT_HARDWARE_URL = "remote/clients/hardware/list";

	private ClientService() {
	}

	public static ClientService getInstance() {
		return INSTANCE;
	}

	/**
	 * @param group
	 * @param remoteRequestCallback
	 */
	public void listClients(GroupJSO group, RemoteRequestCallback<List<ClientJSO>> callback) {

		RemoteRequest<List<ClientJSO>> request = new RemoteRequest<List<ClientJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_LIST_URL) +
				"?groupUuid=" + URL.encodeQueryString(group.getUuid())) {

			@Override
			protected List<ClientJSO> transformResponse(String text) {
				return transformArray(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * @param remoteRequestCallback
	 */
	public void createClient(GroupJSO group, RemoteRequestCallback<ClientJSO> callback) {

		RemoteRequest<ClientJSO> request = new RemoteRequest<ClientJSO>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_CREATE_URL) +
				"?groupUuid=" + URL.encodeQueryString(group.getUuid())) {

			@Override
			protected ClientJSO transformResponse(String text) {
				return transform(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * @param client
	 * @param remoteRequestCallback
	 */
	public void editClient(ClientJSO client, RemoteRequestCallback<ClientJSO> callback) {


		RemoteRequest<ClientJSO> request = new RemoteRequest<ClientJSO>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_EDIT_URL)) {

			@Override
			protected ClientJSO transformResponse(String text) {
				return transform(text);
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuilder requestData = new StringBuilder();
		requestData.append("uuid=");
		requestData.append(URL.encodeQueryString(client.getUuid()));
		request.setRequestData(requestData.toString());

		request.execute(callback);

	}

	/**
	 * @param client
	 * @param remoteRequestCallback
	 */
	public void saveClient(ClientJSO client, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.PUT,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_SAVE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(client);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 *
	 * @param client
	 * @param callback
	 */
	public void deleteClient(ClientJSO client, RemoteRequestCallback<Object> callback) {
		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.DELETE,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_DELETE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(client);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);
	}

	/**
	 * @param client
	 * @param instalace
	 * @param remoteRequestCallback
	 */
	public void installClient(ClientJSO client, InstalaceJSO instalace,
			RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.PUT,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_INSTALL_URL +
						"?instalaceId=" + instalace.getId())) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(client);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * @param master 
	 * @param remoteRequestCallback
	 */
	public void listClientsForImport(GroupJSO group,
			boolean master, RemoteRequestCallback<List<ClientJSO>> callback) {

		RemoteRequest<List<ClientJSO>> request = new RemoteRequest<List<ClientJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_IMPORT_LIST_URL) +
				"?groupUuid=" + URL.encodeQueryString(group.getUuid()) +
				"&opsi=" + (master ? "0" : "1" )) {

			@Override
			protected List<ClientJSO> transformResponse(String text) {
				return transformArray(text);
			}
		};

		request.execute(callback);


	}

	/**
	 * @param client
	 * @param remoteRequestCallback
	 */
	public void listHardware(ClientJSO client,
			RemoteRequestCallback<List<HardwareJSO>> callback) {

		RemoteRequest<List<HardwareJSO>> request = new RemoteRequest<List<HardwareJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_HARDWARE_URL) +
				"?uuid=" + URL.encodeQueryString(client.getUuid())) {

			@Override
			protected List<HardwareJSO> transformResponse(String text) {
				return transformArrayHardware(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * @param text
	 * @return
	 */
	protected List<ClientJSO> transformArray(String text) {
        JsArray<ClientJSO> array = ClientJSO.fromJSONArray(text);
        List<ClientJSO> users = new ArrayList<ClientJSO>();
        for(int i = 0; i < array.length(); i++) {
                users.add(array.get(i));
        }
        return users;
	}

	/**
	 * @param text
	 * @return
	 */
	protected List<HardwareJSO> transformArrayHardware(String text) {
		JsArray<HardwareJSO> array = HardwareJSO.fromJSONArray(text);
		List<HardwareJSO> users = new ArrayList<HardwareJSO>();
		for(int i = 0; i < array.length(); i++) {
			users.add(array.get(i));
		}
		return users;
	}

    /**
     * @param jsonObject
     * @return
     */
    private ClientJSO transform(String u) {
    	return ClientJSO.fromJSON(u);
    }

    /**
     * @param user
     * @return
     */
    private JSONObject transform(ClientJSO u) {
    	JSONObject jsonObject = new JSONObject(u);
    	jsonObject.put("$H", null);
    	return jsonObject;
    }


}
