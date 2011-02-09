/**
 *
 */
package cz.muni.ucn.opsi.core.group;

import java.util.List;
import java.util.UUID;

import cz.muni.ucn.opsi.api.group.Group;

/**
 * @author Jan Dosoudil
 *
 */
public interface GroupDao {

	/**
	 * @param uuid
	 * @return
	 */
	Group get(UUID uuid);

	/**
	 * @param group
	 */
	void save(Group group);

	/**
	 * @param group
	 */
	void delete(Group group);

	/**
	 * @return
	 */
	List<Group> list();

}
