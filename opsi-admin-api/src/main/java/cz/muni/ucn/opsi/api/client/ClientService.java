/**
 *
 */
package cz.muni.ucn.opsi.api.client;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.annotation.Secured;

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

}
