/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.group;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;

/**
 * @author dosoudilj
 *
 */
public class GroupJSO extends JavaScriptObject implements BeanModelTag {
	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO";
/*
	private String uuid;
	private String name;
	private String role;
*/

	protected GroupJSO() {
	}

	/**
	 * @return the uuid
	 */
	public final native String getUuid() /*-{
		return this.uuid;
	}-*/;
	/**
	 * @param uuid the uuid to set
	 */
	public final native  void setUuid(String uuid) /*-{
		this.uuid = uuid;
	}-*/;
	/**
	 * @return the name
	 */
	public final native String getName() /*-{
		return this.name;
	}-*/;
	/**
	 * @param name the name to set
	 */
	public final native  void setName(String name) /*-{
		this.name = name;
	}-*/;
	/**
	 * @return the role
	 */
	public final native  String getRole() /*-{
		return this.role;
	}-*/;
	/**
	 * @param role the role to set
	 */
	public final native  void setRole(String role) /*-{
		this.role = role;
	}-*/;

    /**
     * @param u
     * @return
     */
    public static final native GroupJSO fromJSON(String u) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
        return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
    }-*/;

    /**
     * @param u
     * @return
     */
    public static final native JsArray<GroupJSO> fromJSONArray(String u) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
        return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
    }-*/;


}
