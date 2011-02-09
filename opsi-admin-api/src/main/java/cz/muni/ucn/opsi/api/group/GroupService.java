/**
 *
 */
package cz.muni.ucn.opsi.api.group;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.annotation.Secured;

/**
 * @author Jan Dosoudil
 *
 */
public interface GroupService {

	/**
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	Group createGroup();

	/**
	 * @param fromString
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	Group editGroup(UUID uuid);

	/**
	 * @param group
	 */
	@Secured("ROLE_ADMIN")
	void saveGroup(Group group);

	/**
	 * @param group
	 */
	@Secured("ROLE_ADMIN")
	void deleteGroup(Group group);

	/**
	 * @return
	 */
	List<Group> listGroups();

}
