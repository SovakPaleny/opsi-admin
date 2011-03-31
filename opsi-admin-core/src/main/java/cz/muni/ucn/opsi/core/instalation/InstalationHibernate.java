/**
 *
 */
package cz.muni.ucn.opsi.core.instalation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @author Jan Dosoudil
 *
 */
@Entity(name="Instalation")
@Table(name="INSTALATIONS")
public class InstalationHibernate implements Serializable {
	private static final long serialVersionUID = 3122056880728097443L;

	private String id;
	private String name;

	/**
	 * @return the id
	 */
	@Id
	@Length(min=1, max=50)
	@Column(length=50)
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
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


}
