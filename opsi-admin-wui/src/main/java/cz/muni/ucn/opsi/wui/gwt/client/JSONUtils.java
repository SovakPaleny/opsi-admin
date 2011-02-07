/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Jan Dosoudil
 *
 */
public class JSONUtils {

	public static String getString(JSONValue value) {
		JSONString string = value.isString();
		if (null == string) {
			return null;
		}
		return string.stringValue();
	}

	public static JSONValue getJSONString(String string) {
		if (null == string) {
			return JSONNull.getInstance();
		}
		return new JSONString(string);
	}

	public static Boolean getBoolean(JSONValue value) {
		JSONBoolean bool = value.isBoolean();
		if (null == bool) {
			return null;
		}
		return bool.booleanValue();
	}

	public static JSONValue getJSONBoolean(Boolean value) {
		if (null == value) {
			return JSONNull.getInstance();
		}
		return JSONBoolean.getInstance(value);
	}
}
