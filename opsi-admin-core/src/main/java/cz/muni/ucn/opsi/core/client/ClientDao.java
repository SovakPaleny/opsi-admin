/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import java.util.List;
import java.util.UUID;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.group.Group;

/**
 * @author Jan Dosoudil
 *
 */
public interface ClientDao {

	/**
	 * @param uuid
	 */
	Client get(UUID uuid);

	/**
	 * @param client
	 */
	void save(Client client);

	/**
	 * @param client
	 */
	void delete(Client client);

	/**
	 * @param group
	 * @return
	 */
	List<Client> list(Group group);

	/**
	 * @return
	 */
	List<String> listNamesAll();

}
