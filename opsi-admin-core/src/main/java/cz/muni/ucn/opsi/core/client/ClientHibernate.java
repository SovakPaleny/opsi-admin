/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

import cz.muni.ucn.opsi.core.group.GroupHibernate;
import cz.u2.eis.valueObjects.hibernate.HibernateValueObject;

/**
 * @author Jan Dosoudil
 *
 */
@Entity(name="Client")
@Table(name="CLIENTS")
public class ClientHibernate extends HibernateValueObject {
	private static final long serialVersionUID = 1472266922881562059L;

	private String name;
	private String description;
	private String notes;
	private String ipAddress;
	private String macAddress;
	private GroupHibernate group;

	/**
	 *
	 */
	public ClientHibernate() {
		super();
	}
	/**
	 * @param uuid
	 */
	public ClientHibernate(UUID uuid) {
		super(uuid);
	}

	/**
	 * @return the name
	 */
	@NaturalId
	@Length(min=1, max=50)
	@NotNull
	@Column(length=50)
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
	@Length(max=250)
	@Column(length=250)
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
	 * @return the notes
	 */
	@Length(max=250)
	@Column(length=250)
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * @return the ipAddress
	 */
	@Length(max=15)
	@Pattern(regexp="[0-2]?[0-9]{1,2}\\.[0-2]?[0-9]{1,2}\\.[0-2]?[0-9]{1,2}\\.[0-2]?[0-9]{1,2}")
	@Column(length=15)
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
	@Length(max=17)
	@Pattern(regexp="[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}")
	@Column(length=17)
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
	@ManyToOne
	@NotNull
	public GroupHibernate getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(GroupHibernate group) {
		this.group = group;
	}
}
