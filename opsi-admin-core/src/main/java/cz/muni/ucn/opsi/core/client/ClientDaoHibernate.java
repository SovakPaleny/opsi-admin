/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.group.Group;
import cz.muni.ucn.opsi.core.group.GroupHibernate;

/**
 * @author Jan Dosoudil
 *
 */
@Repository
public class ClientDaoHibernate implements ClientDao {

	private SessionFactory sessionFactory;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.client.ClientDao#get(java.util.UUID)
	 */
	@Override
	public Client get(UUID uuid) {
		Session session = sessionFactory.getCurrentSession();
		ClientHibernate clientH = (ClientHibernate) session.get(ClientHibernate.class, uuid);

		return transform(clientH);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.client.ClientDao#save(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	public void save(Client client) {
		Session session = sessionFactory.getCurrentSession();
		ClientHibernate loaded = (ClientHibernate) session.get(ClientHibernate.class, client.getUuid());

		ClientHibernate toSave = transform(loaded, client);
		session.save(toSave);
		session.flush();
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.client.ClientDao#delete(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	public void delete(Client client) {
		Session session = sessionFactory.getCurrentSession();
		ClientHibernate clientH = (ClientHibernate) session.load(ClientHibernate.class, client.getUuid());
		session.delete(clientH);
		session.flush();
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.client.ClientDao#list(cz.muni.ucn.opsi.api.group.Group)
	 */
	@Override
	public List<Client> list(Group group) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<ClientHibernate> list = session.createQuery("select c from Client c " +
					"where c.group.uuid = :group")
			.setParameter("group", group.getUuid())
			.list();

		return transform(list);
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	/**
	 * @param clientH
	 * @return
	 */
	private Client transform(ClientHibernate clientH) {
		if (null == clientH) {
			return null;
		}

		Client c = new Client();
		c.setUuid(clientH.getUuid());
		c.setName(clientH.getName());
		c.setDescription(clientH.getDescription());
		c.setIpAddress(clientH.getIpAddress());
		c.setMacAddress(clientH.getMacAddress());

		GroupHibernate groupH = clientH.getGroup();
		Group g = new Group();
		g.setUuid(groupH.getUuid());
		g.setName(groupH.getName());
		g.setRole(groupH.getRole());

		c.setGroup(g);

		return c;
	}

	/**
	 * @param list
	 * @return
	 */
	private List<Client> transform(List<ClientHibernate> list) {
		if (null == list) {
			return null;
		}
		List<Client> ret = new ArrayList<Client>(list.size());
		for (ClientHibernate clientHibernate : list) {
			ret.add(transform(clientHibernate));
		}
		return ret;
	}

	/**
	 * @param loaded
	 * @param client
	 * @return
	 */
	private ClientHibernate transform(ClientHibernate loaded, Client client) {
		Session session = sessionFactory.getCurrentSession();

		ClientHibernate toSave = loaded;
		if (null == toSave) {
			toSave = new ClientHibernate();
			toSave.setUuid(client.getUuid());
		}
		toSave.setUuid(client.getUuid());
		toSave.setName(client.getName());
		toSave.setDescription(client.getDescription());
		toSave.setIpAddress(client.getIpAddress());
		toSave.setMacAddress(client.getMacAddress());

		GroupHibernate groupH = (GroupHibernate) session.load(GroupHibernate.class, client.getGroup().getUuid());
		toSave.setGroup(groupH);

		return toSave;
	}

}
