/**
 *
 */
package cz.muni.ucn.opsi.core.opsiClient;

/**
 * @author Jan Dosoudil
 *
 */
public class OpsiException extends RuntimeException {

	private static final long serialVersionUID = 6742229821639572698L;

	/**
	 *
	 */
	public OpsiException() {
	}

	/**
	 * @param message
	 */
	public OpsiException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public OpsiException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public OpsiException(String message, Throwable cause) {
		super(message, cause);
	}

}
