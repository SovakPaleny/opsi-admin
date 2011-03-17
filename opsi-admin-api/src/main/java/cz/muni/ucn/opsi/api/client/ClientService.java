/**
 *
 */
package cz.muni.ucn.opsi.api.client;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.annotation.Secured;

import cz.muni.ucn.opsi.api.instalation.Instalation;

/**
 * @author Jan Dosoudil
 *
 */
public interface ClientService {

	/**
	 * @return
	 */
	@Secured("ROLE_USER")
	Client createClient(UUID groupUuid);

	/**
	 * @param fromString
	 * @return
	 */
	@Secured("ROLE_USER")
	Client editClient(UUID uuid);

	/**
	 * @param client
	 */
	@Secured("ROLE_USER")
	void saveClient(Client client);

	/**
	 * @param client
	 */
	@Secured("ROLE_USER")
	void deleteClient(Client client);

	/**
	 * @param fromString
	 * @return
	 */
	@Secured("ROLE_USER")
	List<Client> listClients(UUID groupUuid);

	/**
	 * @param client
	 * @param i
	 */
	@Secured("ROLE_USER")
	void installClient(Client client, Instalation i);

	/**
	 * @param fromString
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	List<Client> listClientsForImport(UUID groupUuid);

}
