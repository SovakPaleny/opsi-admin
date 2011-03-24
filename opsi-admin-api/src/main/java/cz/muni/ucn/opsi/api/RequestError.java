/**
 *
 */
package cz.muni.ucn.opsi.api;

import java.io.Serializable;

/**
 * @author Jan Dosoudil
 *
 */
public class RequestError implements Serializable {
	private static final long serialVersionUID = 1006428434123111930L;

	private String message;

	/**
	 * @param message2
	 */
	public RequestError(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
