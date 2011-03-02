/**
 *
 */
package cz.muni.ucn.opsi.core.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cz.muni.ucn.opsi.api.group.Group;
import cz.muni.ucn.opsi.api.group.GroupService;
import cz.u2.eis.api.events.data.LifecycleEvent;
import cz.u2.eis.api.events.data.SecuredLifecycleEvent;

/**
 * @author Jan Dosoudil
 *
 */
@Service
@Transactional(readOnly=true)
public class GroupServiceImpl implements GroupService, ApplicationEventPublisherAware{

//	private final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

	private ApplicationEventPublisher eventPublisher;
	private AccessDecisionManager accessDecisionManager;

	private GroupDao groupDao;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#createGroup()
	 */
	@Override
	@Transactional
	public Group createGroup() {
		Group group = new Group(UUID.randomUUID());
		return group;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#editGroup(java.util.UUID)
	 */
	@Override
	@Transactional
	public Group editGroup(UUID uuid) {
		return groupDao.get(uuid);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#saveGroup(cz.muni.ucn.opsi.api.group.Group)
	 */
	@Override
	@Transactional(readOnly=false)
	public void saveGroup(Group group) {
        Group loaded = groupDao.get(group.getUuid());
        boolean newGroup = (null == loaded);

        groupDao.save(group);

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
	@Transactional(readOnly=false)
	public void deleteGroup(Group group) {
		groupDao.delete(group);

		eventPublisher.publishEvent(new SecuredLifecycleEvent(LifecycleEvent.DELETED, group, "ROLE_ADMIN"));
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#listGroups()
	 */
	@Override
	@Transactional
	public List<Group> listGroups() {
		List<Group> list = groupDao.list();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		try {
			accessDecisionManager.decide(authentication, null,
					Arrays.asList(new ConfigAttribute[] {new SecurityConfig("ROLE_ADMIN")}));

			return list;
		} catch (AccessDeniedException e) {
		}

		List<Group> retList = new ArrayList<Group>(list.size());
		for (Group group : list) {
			String securityRole = group.getRole();
			if (!StringUtils.hasText(securityRole)) {
				retList.add(group);
				continue;
			}
			try {
				accessDecisionManager.decide(authentication, null,
						Arrays.asList(new ConfigAttribute[] {new SecurityConfig(securityRole)}));
				retList.add(group);
			} catch (AccessDeniedException e) {
			}
		}

		return retList;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.group.GroupService#getGroup(java.util.UUID)
	 */
	@Override
	@Transactional
	public Group getGroup(UUID uuid) {
		return groupDao.get(uuid);
	}


	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
			this.eventPublisher = applicationEventPublisher;
	}

	/**
	 * @param groupDao the groupDao to set
	 */
	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	/**
	 * @param accessDecisionManager the accessDecisionManager to set
	 */
	@Autowired
	public void setAccessDecisionManager(
			AccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}
}
