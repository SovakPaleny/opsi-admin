/**
 *
 */
package cz.muni.ucn.opsi.api.opsiClient;

import java.util.List;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.instalation.Instalation;

/**
 * @author Jan Dosoudil
 *
 */
public interface OpsiClientService {

	/**
	 * @param client
	 */
	void createClient(Client client);

	/**
	 * @return
	 */
	List<Instalation> listInstalations();

}
