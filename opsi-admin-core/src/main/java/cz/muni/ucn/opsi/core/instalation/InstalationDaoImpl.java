/**
 *
 */
package cz.muni.ucn.opsi.core.instalation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.muni.ucn.opsi.api.instalation.Instalation;

/**
 * @author Jan Dosoudil
 *
 */
@Repository
public class InstalationDaoImpl implements InstalationDao {

	private SessionFactory sessionFactory;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.instalation.InstalationDao#listInstalations()
	 */
	@Override
	public List<Instalation> listInstalations() {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<InstalationHibernate> instalations = session.createQuery(
					"select i from Instalation i order by i.name")
				.list();

		return transform(instalations);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.core.instalation.InstalationDao#saveInstalations(java.util.List)
	 */
	@Override
	public void saveInstalations(List<Instalation> instalations) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<InstalationHibernate> loadedHib = session.createQuery(
					"select i from Instalation i order by i.name")
				.list();
		Map<String, InstalationHibernate> mapHib = new HashMap<String, InstalationHibernate>();
		for (InstalationHibernate ih : loadedHib) {
			mapHib.put(ih.getId(), ih);
		}

		Set<Instalation> loaded = new HashSet<Instalation>(transform(loadedHib));

		List<Instalation> existing = new ArrayList<Instalation>();
		for (Instalation i : instalations) {
			if (!loaded.contains(i)) {
				continue;
			}
			existing.add(i);
		}
		instalations.removeAll(existing);


		List<Instalation> toRemove = new ArrayList<Instalation>();
		for (Instalation i : loaded) {
			if (!instalations.contains(i)) {
				toRemove.add(i);
			}
		}

		for (Instalation instalation : toRemove) {
			session.delete(mapHib.get(instalation.getId()));
		}

		for (Instalation i : instalations) {
			session.save(transform(i));

		}

	}

	/**
	 * @param i
	 * @return
	 */
	private InstalationHibernate transform(Instalation i) {
		if (null == i) {
			return null;
		}
		InstalationHibernate ih = new InstalationHibernate();
		ih.setId(i.getId());
		ih.setName(i.getName());
		return ih;
	}

	/**
	 * @param instalations
	 * @return
	 */
	private List<Instalation> transform(List<InstalationHibernate> instalations) {
		if (null == instalations) {
			return null;
		}
		List<Instalation> ret = new ArrayList<Instalation>(instalations.size());
		for (InstalationHibernate ih : instalations) {
			ret.add(transform(ih));
		}
		return ret;
	}

	/**
	 * @param ih
	 * @return
	 */
	private Instalation transform(InstalationHibernate ih) {
		if (null == ih) {
			return null;
		}
		Instalation i = new Instalation();
		i.setId(ih.getId());
		i.setName(ih.getName());
		return i;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
