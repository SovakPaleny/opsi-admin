/**
 *
 */
package cz.muni.ucn.opsi.core.group;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cz.muni.ucn.opsi.api.group.Group;

/**
 * @author Jan Dosoudil
 *
 */
@Repository
public class GroupDaoHibernate implements GroupDao {

	private SessionFactory sessionFactory;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.group.GroupDao#get(java.util.UUID)
	 */
	@Override
	public Group get(UUID uuid) {
		Session session = sessionFactory.getCurrentSession();
		GroupHibernate groupH = (GroupHibernate) session.get(GroupHibernate.class, uuid);
		return transform(groupH);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.group.GroupDao#save(cz.muni.ucn.opsi.api.group.Group)
	 */
	@Override
	public void save(Group group) {
		Session session = sessionFactory.getCurrentSession();

		GroupHibernate saved = (GroupHibernate) session.get(GroupHibernate.class, group.getUuid());
		GroupHibernate toSave = transform(saved, group);

		session.save(toSave);
		session.flush();
	}


	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.group.GroupDao#delete(cz.muni.ucn.opsi.api.group.Group)
	 */
	@Override
	public void delete(Group group) {
		Session session = sessionFactory.getCurrentSession();
		GroupHibernate groupH = (GroupHibernate) session.load(GroupHibernate.class, group.getUuid());
		session.delete(groupH);
		session.flush();
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.group.GroupDao#list()
	 */
	@Override
	public List<Group> list() {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<GroupHibernate> list = session.createQuery("select g from Group g order by name").list();
		return transform(list);
	}

	/**
	 * @param groupH
	 * @return
	 */
	private Group transform(GroupHibernate groupH) {
		if (null == groupH) {
			return null;
		}
		Group g = new Group();
		g.setUuid(groupH.getUuid());
		g.setName(groupH.getName());
		g.setRole(groupH.getRole());
		return g;
	}

	/**
	 * @param list
	 * @return
	 */
	private List<Group> transform(List<GroupHibernate> list) {
		if (null == list) {
			return null;
		}
		List<Group> groups = new ArrayList<Group>(list.size());
		for (GroupHibernate groupHibernate : list) {
			groups.add(transform(groupHibernate));
		}
		return groups;
	}

	/**
	 * @param saved
	 * @param group
	 * @return
	 */
	private GroupHibernate transform(final GroupHibernate saved, final Group group) {
		GroupHibernate toSave = saved;
		if (toSave == null) {
			toSave = new GroupHibernate();
			toSave.setUuid(group.getUuid());
		}
		toSave.setName(group.getName());
		toSave.setRole(group.getRole());
		return toSave;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	@Autowired
	@Qualifier("opsi")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
