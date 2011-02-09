/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.group;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;

import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequest;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class GroupService {

	private static final GroupService INSTANCE = new GroupService();

	private static final String GROUPS_LIST_URL = "remote/groups/list";
	private static final String GROUP_CREATE_URL = "remote/groups/create";
	private static final String GROUP_EDIT_URL = "remote/groups/edit";
	private static final String GROUP_SAVE_URL = "remote/groups/save";
	private static final String GROUP_DELETE_URL = "remote/groups/delete";

	private GroupService() {
	}

	public static GroupService getInstance() {
		return INSTANCE;
	}

	/**
	 * @param remoteRequestCallback
	 */
	public void listGroups(RemoteRequestCallback<List<GroupJSO>> callback) {

		RemoteRequest<List<GroupJSO>> listGroupsRequest = new RemoteRequest<List<GroupJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + GROUPS_LIST_URL)) {

			@Override
			protected List<GroupJSO> transformResponse(String text) {
				return transformArray(text);
			}
		};
		listGroupsRequest.execute(callback);

	}

	/**
	 * @param remoteRequestCallback
	 */
	public void createGroup(RemoteRequestCallback<GroupJSO> callback) {

		RemoteRequest<GroupJSO> request = new RemoteRequest<GroupJSO>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_CREATE_URL)) {

			@Override
			protected GroupJSO transformResponse(String text) {
				return transform(text);
			}
		};
		request.execute(callback);

	}

	/**
	 * @param group
	 * @param remoteRequestCallback
	 */
	public void editGroup(GroupJSO group, RemoteRequestCallback<GroupJSO> callback) {


		RemoteRequest<GroupJSO> request = new RemoteRequest<GroupJSO>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_EDIT_URL)) {

			@Override
			protected GroupJSO transformResponse(String text) {
				return transform(text);
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuilder requestData = new StringBuilder();
		requestData.append("uuid=");
		requestData.append(URL.encodeQueryString(group.getUuid()));
		request.setRequestData(requestData.toString());

		request.execute(callback);

	}

	/**
	 * @param group
	 * @param remoteRequestCallback
	 */
	public void saveGroup(GroupJSO group, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.PUT,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_SAVE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject groupJson = transform(group);

		String data = groupJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 *
	 * @param group
	 * @param callback
	 */
	public void deleteGroup(GroupJSO group, RemoteRequestCallback<Object> callback) {
		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.DELETE,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_DELETE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(group);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);
	}

	/**
	 * @param text
	 * @return
	 */
	protected List<GroupJSO> transformArray(String text) {
        JsArray<GroupJSO> array = GroupJSO.fromJSONArray(text);
        List<GroupJSO> users = new ArrayList<GroupJSO>();
        for(int i = 0; i < array.length(); i++) {
                users.add(array.get(i));
        }
        return users;
	}

    /**
     * @param jsonObject
     * @return
     */
    private GroupJSO transform(String u) {
    	return GroupJSO.fromJSON(u);
    }

    /**
     * @param user
     * @return
     */
    private JSONObject transform(GroupJSO u) {
    	JSONObject jsonObject = new JSONObject(u);
    	jsonObject.put("$H", null);
    	return jsonObject;
    }


}
