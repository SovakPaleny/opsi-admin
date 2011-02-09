/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.ClientService;
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
public class ClientServiceImpl implements ClientService, ApplicationEventPublisherAware {

	private GroupService groupService;
	private ClientDao clientDao;
	private ApplicationEventPublisher eventPublisher;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#createClient(java.util.UUID)
	 */
	@Override
	@Transactional
	public Client createClient(UUID groupUuid) {
		Group group = groupService.getGroup(groupUuid);
		Client client = new Client(UUID.randomUUID());
		client.setGroup(group);
		return client;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#editClient(java.util.UUID)
	 */
	@Override
	@Transactional
	public Client editClient(UUID uuid) {
		Client client = clientDao.get(uuid);
		return client;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#saveClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	@Transactional(readOnly=false)
	public void saveClient(Client client) {
		Client loaded = clientDao.get(client.getUuid());
		boolean newClient = null == loaded;

		clientDao.save(client);

        SecuredLifecycleEvent event;
        if (newClient) {
        	event = new SecuredLifecycleEvent(LifecycleEvent.CREATED,
        			client, "ROLE_USER");
        } else {
        	event = new SecuredLifecycleEvent(LifecycleEvent.MODIFIED,
        			client, "ROLE_USER");
        }

        eventPublisher.publishEvent(event);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#deleteClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	@Transactional(readOnly=false)
	public void deleteClient(Client client) {
		clientDao.delete(client);

		eventPublisher.publishEvent(new SecuredLifecycleEvent(LifecycleEvent.DELETED, client, "ROLE_USER"));
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#listClients(java.util.UUID)
	 */
	@Override
	@Transactional
	public List<Client> listClients(UUID groupUuid) {
		Group group = groupService.getGroup(groupUuid);
		if (null == group) {
			return null;
		}
		return clientDao.list(group);
	}

	/**
	 * @param clientDao the clientDao to set
	 */
	@Autowired
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	/**
	 * @param groupService the groupService to set
	 */
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}
}
