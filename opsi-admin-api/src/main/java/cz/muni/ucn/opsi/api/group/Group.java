/**
 *
 */
package cz.muni.ucn.opsi.api.group;

import cz.u2.eis.valueObjects.ValueObject;

/**
 * @author dosoudilj
 *
 */
public class Group extends ValueObject {

	private String name;
	private String role;
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
