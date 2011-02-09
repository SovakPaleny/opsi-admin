/**
 *
 */
package cz.muni.ucn.opsi.core.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import cz.muni.ucn.opsi.api.group.Group;
import cz.muni.ucn.opsi.api.group.GroupService;
import cz.u2.eis.api.events.data.LifecycleEvent;
import cz.u2.eis.api.events.data.SecuredLifecycleEvent;

/**
 * @author Jan Dosoudil
 *
 */
@Service
public class GroupServiceImpl implements GroupService, ApplicationEventPublisherAware{

	private Map<UUID, Group> map = new HashMap<UUID, Group>();
	private ApplicationEventPublisher eventPublisher;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#createGroup()
	 */
	@Override
	public Group createGroup() {
		Group group = new Group(UUID.randomUUID());
		return group;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#editGroup(java.util.UUID)
	 */
	@Override
	public Group editGroup(UUID uuid) {
		return map.get(uuid);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#saveGroup(cz.muni.ucn.opsi.api.group.Group)
	 */
	@Override
	public void saveGroup(Group group) {


        Group loaded = map.get(group.getUuid());
        boolean newGroup = (null == loaded);

        map.put(group.getUuid(), group);

        SecuredLifecycleEvent event;
        if (newGroup) {
        	event = new SecuredLifecycleEvent(LifecycleEvent.CREATED,
        			group, "ROLE_ADMIN");
        } else {
        	event = new SecuredLifecycleEvent(LifecycleEvent.MODIFIED,
        			group, "ROLE_ADMIN");
        }

        eventPublisher.publishEvent(event);

	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#removeGroup(cz.muni.ucn.opsi.api.group.Group)
	 */
	@Override
	public void deleteGroup(Group group) {
		map.remove(group.getUuid());

		eventPublisher.publishEvent(new SecuredLifecycleEvent(LifecycleEvent.DELETED, group, "ROLE_ADMIN"));
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#listGroups()
	 */
	@Override
	public List<Group> listGroups() {
		return new ArrayList<Group>(map.values());
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
			this.eventPublisher = applicationEventPublisher;
	}

}
