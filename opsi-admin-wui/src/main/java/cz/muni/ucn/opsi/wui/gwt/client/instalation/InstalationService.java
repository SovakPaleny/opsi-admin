/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequest;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class InstalationService {

	private static final InstalationService INSTANCE = new InstalationService();

	private static final String INSTALATION_LIST_URL = "remote/instalation/list";
	private static final String INSTALATION_LIST_ALL_URL = "remote/instalation/listAll";
	private static final String INSTALATION_SAVE_URL = "remote/instalation/save";

	private InstalationService() {
	}

	public static InstalationService getInstance() {
		return INSTANCE;
	}


	/**
	 *
	 */
	public void listInstalations(RemoteRequestCallback<List<InstalaceJSO>> callback) {
		RemoteRequest<List<InstalaceJSO>> request = new RemoteRequest<List<InstalaceJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + INSTALATION_LIST_URL)) {

			@Override
			protected List<InstalaceJSO> transformResponse(String text) {
				return transformInstalation(text);
			}
		};

		request.execute(callback);

	}

	/**
	 *
	 */
	public void listInstalationsAll(RemoteRequestCallback<List<InstalaceJSO>> callback) {
		RemoteRequest<List<InstalaceJSO>> request = new RemoteRequest<List<InstalaceJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + INSTALATION_LIST_ALL_URL)) {

			@Override
			protected List<InstalaceJSO> transformResponse(String text) {
				return transformInstalation(text);
			}
		};

		request.execute(callback);

	}

	/**
	 *
	 */
	public void saveInstalations(List<InstalaceJSO> instalations, RemoteRequestCallback<Object> callback) {
		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + INSTALATION_SAVE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONArray instalaceJson = transform(instalations);

		String data = instalaceJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * @param instalations
	 * @return
	 */
	private JSONArray transform(List<InstalaceJSO> instalations) {
		JSONArray jsonArray = new JSONArray();
		for (InstalaceJSO i : instalations) {
			jsonArray.set(jsonArray.size(), transform(i));
		}
		return jsonArray;
	}

	/**
	 * @param i
	 * @return
	 */
	private JSONValue transform(InstalaceJSO i) {
		JSONObject jsonObject = new JSONObject(i);
		jsonObject.put("$H", null);
		return jsonObject;
	}

	/**
	 * @param text
	 * @return
	 */
	protected List<InstalaceJSO> transformInstalation(String text) {

        JsArray<InstalaceJSO> array = InstalaceJSO.fromJSONArray(text);
        List<InstalaceJSO> insts = new ArrayList<InstalaceJSO>();
        for(int i = 0; i < array.length(); i++) {
                insts.add(array.get(i));
        }

        return insts;
	}


}
