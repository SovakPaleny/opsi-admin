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

	/**
	 * @param instalationId
	 * @return
	 */
	Instalation getIntalationById(String instalationId);

	/**
	 * @param client
	 * @param i
	 */
	void clientInstall(Client client, Instalation i);

	/**
	 * @param client
	 */
	void deleteClient(Client client);

	/**
	 * @param client
	 */
	void updateClient(Client client);

	/**
	 * @return
	 */
	List<Client> listClientsForImport();

}
