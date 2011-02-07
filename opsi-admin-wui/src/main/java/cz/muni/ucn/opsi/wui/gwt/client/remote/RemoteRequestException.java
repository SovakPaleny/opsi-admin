/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.remote;

/**
 * @author Jan Dosoudil
 *
 */
public class RemoteRequestException extends RuntimeException {
	private static final long serialVersionUID = 1368071864708518048L;

	/**
	 *
	 */
	public RemoteRequestException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RemoteRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RemoteRequestException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RemoteRequestException(Throwable cause) {
		super(cause);
	}
}
