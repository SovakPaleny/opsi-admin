/**
 *
 */
package cz.muni.ucn.opsi.api.client;

import java.util.UUID;

import cz.muni.ucn.opsi.api.group.Group;
import cz.u2.eis.valueObjects.ValueObject;

/**
 * @author Jan Dosoudil
 *
 */
public class Client extends ValueObject {
	private static final long serialVersionUID = -6575924560081692249L;

	private String name;
	private String description;
	private String notes;
	private String ipAddress;
	private String macAddress;
	private Group group;

	/**
	 *
	 */
	public Client() {
		super();
	}

	/**
	 * @param uuid
	 */
	public Client(UUID uuid) {
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}


}
