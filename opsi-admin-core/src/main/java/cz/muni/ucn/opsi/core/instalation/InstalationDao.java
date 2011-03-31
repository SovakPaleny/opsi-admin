/**
 *
 */
package cz.muni.ucn.opsi.core.instalation;

import java.util.List;

import cz.muni.ucn.opsi.api.instalation.Instalation;

/**
 * @author Jan Dosoudil
 *
 */
public interface InstalationDao {

	/**
	 * @return
	 *
	 */
	List<Instalation> listInstalations();

	/**
	 * @param instalations
	 * @return
	 */
	void saveInstalations(List<Instalation> instalations);

}
