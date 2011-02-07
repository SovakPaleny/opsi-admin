/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.event;

import de.novanic.eventservice.client.event.Event;

/**
 * @author Jan Dosoudil
 *
 */
public class LifecycleCometEvent implements Event {

	private static final long serialVersionUID = 1143019399990173603L;

	private String jsonObject;

	/**
	 *
	 */
	public LifecycleCometEvent() {
	}

	/**
	 * @param jsonObject
	 */
	public LifecycleCometEvent(String jsonObject) {
		super();
		this.jsonObject = jsonObject;
	}

	/**
	 * @return the jsonObject
	 */
	public String getJsonObject() {
		return jsonObject;
	}

	/**
	 * @param jsonObject the jsonObject to set
	 */
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}


}
