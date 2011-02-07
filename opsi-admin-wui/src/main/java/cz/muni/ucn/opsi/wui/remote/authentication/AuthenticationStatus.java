/**
 *
 */
package cz.muni.ucn.opsi.wui.remote.authentication;

import java.io.Serializable;

/**
 * @author Jan Dosoudil
 *
 */
public class AuthenticationStatus implements Serializable {
	private static final long serialVersionUID = -2728070266616596504L;

	public static final String STATUS_LOGGED_IN = "OK";
	public static final String STATUS_LOGIN_FAILED = "FAILED";
	public static final String STATUS_NOT_LOGGED_IN = "NOT_LOGGED_IN";

	private String status;
	private String message;
	private String[] roles;
	private String username;
	private String displayName;

	/**
	 *
	 */
	public AuthenticationStatus() {
	}

	/**
	 * @param status
	 */
	public AuthenticationStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	/**
	 * @return the roles
	 */
	public String[] getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


}
