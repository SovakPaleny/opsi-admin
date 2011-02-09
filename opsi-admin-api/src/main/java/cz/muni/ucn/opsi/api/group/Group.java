/**
 *
 */
package cz.muni.ucn.opsi.api.group;

import java.util.UUID;

import cz.u2.eis.valueObjects.ValueObject;

/**
 * @author dosoudilj
 *
 */
public class Group extends ValueObject {
	private static final long serialVersionUID = 377295439759900534L;

	private String name;
	private String role;

	/**
	 *
	 */
	public Group() {
		super();
	}
	/**
	 * @param uuid
	 */
	public Group(UUID uuid) {
		super(uuid);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}


}
