/**
 *
 */
package cz.muni.ucn.opsi.core.group;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.Length;

import cz.muni.ucn.opsi.core.client.ClientHibernate;
import cz.u2.eis.valueObjects.hibernate.HibernateValueObject;

/**
 * @author Jan Dosoudil
 *
 */
@Entity(name="Group")
@Table(name="GROUPS", uniqueConstraints={@UniqueConstraint(columnNames="name", name="u_name")})
public class GroupHibernate extends HibernateValueObject {
	private static final long serialVersionUID = 3151715438105784467L;

	private String name;
	private String role;

	private List<ClientHibernate> clients;

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
	@Length(max=50)
	@Column(length=50)
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the clients
	 */
	@OneToMany(mappedBy="group")
	public List<ClientHibernate> getClients() {
		return clients;
	}
	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<ClientHibernate> clients) {
		this.clients = clients;
	}

}
