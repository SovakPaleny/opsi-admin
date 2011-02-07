/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.event;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Jan Dosoudil
 *
 */
public class LifecycleEventJSO extends JavaScriptObject {
	public static final int CREATED = 1;
	public static final int MODIFIED = 2;
	public static final int DELETED = 3;
	public static final int LOCKED = 10;
	public static final int UNLOCKED = 11;

	/**
	 *
	 */
	protected LifecycleEventJSO() {
	}

	/**
	 * @return this.the eventType
	 */
	public final native int getEventType() /*-{
		return this.eventType;
	}-*/;
	/**
	 * @return this.the bean
	 */
	public final native JavaScriptObject getBean() /*-{
		return this.bean;
	}-*/;
	/**
	 * @return this.the bean
	 */
	public final native String getBeanClass() /*-{
		return this.beanClass;
	}-*/;

	/**
	 * @param u
	 * @return
	 */
	public static final native LifecycleEventJSO fromJSON(String u) /*-{
		return eval('(' + u + ')');
	}-*/;

}
