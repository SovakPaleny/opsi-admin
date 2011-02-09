/**
 *
 */
package cz.muni.ucn.opsi.core.group;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

import cz.u2.eis.valueObjects.hibernate.HibernateValueObject;

/**
 * @author Jan Dosoudil
 *
 */
@Entity(name="Group")
@Table(name="GROUPS")
public class GroupHibernate extends HibernateValueObject {
	private static final long serialVersionUID = 3151715438105784467L;

	private String name;
	private String role;

	/**
	 *
	 */
	public GroupHibernate() {
		super();
	}
	/**
	 * @param uuid
	 */
	public GroupHibernate(UUID uuid) {
		super(uuid);
	}
	/**
	 * @return the name
	 */
	@NaturalId
	@Length(min=1, max=50)
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
	 * @return the role
	 */
	@Length(max=20)
	@Column(length=20)
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
