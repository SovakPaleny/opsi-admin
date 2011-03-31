/**
 *
 */
package cz.muni.ucn.opsi.api.instalation;

import java.util.List;

import org.springframework.security.access.annotation.Secured;

/**
 * @author Jan Dosoudil
 *
 */
public interface InstalationService {

	/**
	 * @return
	 */
	@Secured("ROLE_USER")
	List<Instalation> listInstalations();

	/**
	 * @param instalationId
	 * @return
	 */
	@Secured("ROLE_USER")
	Instalation getInstalationById(String instalationId);

	/**
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	List<Instalation> listInstalationsAll();

	/**
	 * @param instalations
	 */
	@Secured("ROLE_ADMIN")
	void saveInstalations(List<Instalation> instalations);

}
