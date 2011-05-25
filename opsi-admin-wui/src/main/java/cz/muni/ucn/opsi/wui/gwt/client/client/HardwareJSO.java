/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class HardwareJSO extends JavaScriptObject implements BeanModelTag {
	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.client.HardwareJSO";
/*
	private String uuid;
	private String name;
	private String description;
	private String ipAddress;
	private String macAddress;
	private GroupJSO group;
 */

	protected HardwareJSO() {
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
	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	/**
	 * @return the description
	 */
	public final native String getDescription() /*-{
		return this.description;
	}-*/;

	/**
	 * @param description the description to set
	 */
	public final native void setDescription(String description) /*-{
		this.description = description;
	}-*/;

	/**
	 * @return the ipAddress
	 */
	public final native String getIpAddress() /*-{
		return this.ipAddress;
	}-*/;

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public final native void setIpAddress(String ipAddress) /*-{
		this.ipAddress = ipAddress;
	}-*/;

	/**
	 * @return the macAddress
	 */
	public final native String getMacAddress() /*-{
		return this.macAddress;
	}-*/;

	/**
	 * @param macAddress the macAddress to set
	 */
	public final native void setMacAddress(String macAddress) /*-{
		this.macAddress = macAddress;
	}-*/;

	/**
	 * @return the group
	 */
	public final native GroupJSO getGroup() /*-{
		return this.group;
	}-*/;

	/**
	 * @param group the group to set
	 */
	public final native void setGroup(GroupJSO group) /*-{
		this.group = group;
	}-*/;

	/**
     * @param u
     * @return
     */
    public static final native HardwareJSO fromJSON(String u) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
        return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
    }-*/;

    /**
     * @param u
     * @return
     */
    public static final native JsArray<HardwareJSO> fromJSONArray(String u) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
        return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
    }-*/;


}
