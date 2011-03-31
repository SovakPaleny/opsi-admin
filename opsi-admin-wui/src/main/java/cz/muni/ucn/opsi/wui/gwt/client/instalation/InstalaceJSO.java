/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;

/**
 * @author Jan Dosoudil
 *
 */
public class InstalaceJSO extends JavaScriptObject implements BeanModelTag {
	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.client.InstalaceJSO";
/*
	private String id;
	private String name;
*/
	/**
	 *
	 */
	protected InstalaceJSO() {
	}

	/**
	 * @return the id
	 */
	public final native String getId() /*-{
		return this.id;
	}-*/;
	/**
	 * @param id the id to set
	 */
	public final native void setId(String id) /*-{
		this.id = id;
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
     * @param u
     * @return
     */
    public static final native InstalaceJSO fromJSON(String u) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
        return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
    }-*/;

    /**
     * @param u
     * @return
     */
    public static final native JsArray<InstalaceJSO> fromJSONArray(String u) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
        return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
    }-*/;


}
