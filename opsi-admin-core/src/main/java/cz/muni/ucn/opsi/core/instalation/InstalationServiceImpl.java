/**
 *
 */
package cz.muni.ucn.opsi.core.instalation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.muni.ucn.opsi.api.instalation.Instalation;
import cz.muni.ucn.opsi.api.instalation.InstalationService;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;

/**
 * @author Jan Dosoudil
 *
 */
@Service
@Transactional(readOnly=true)
public class InstalationServiceImpl implements InstalationService {

	private OpsiClientService opsiClientService;
	private InstalationDao instalationDao;

	/**
	 * @param opsiClientService the opsiClientService to set
	 */
	@Autowired
	public void setOpsiClientService(OpsiClientService opsiClientService) {
		this.opsiClientService = opsiClientService;
	}

	/**
	 * @param instalationDao the instalationDao to set
	 */
	@Autowired
	public void setInstalationDao(InstalationDao instalationDao) {
		this.instalationDao = instalationDao;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.instalation.InstalationService#listInstalations()
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Instalation> listInstalations() {
		return instalationDao.listInstalations();
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.instalation.InstalationService#listInstalationsAll()
	 */
	@Override
	public List<Instalation> listInstalationsAll() {
		return opsiClientService.listInstalations();
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.instalation.InstalationService#getInstalationById(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public Instalation getInstalationById(String instalationId) {
		return opsiClientService.getIntalationById(instalationId);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.instalation.InstalationService#saveInstalations(java.util.List)
	 */
	@Override
	@Transactional(readOnly=false)
	public void saveInstalations(List<Instalation> instalations) {
		instalationDao.saveInstalations(instalations);
	}

}
