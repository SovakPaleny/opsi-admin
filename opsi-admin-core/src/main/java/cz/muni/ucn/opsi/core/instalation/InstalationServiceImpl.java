/**
 *
 */
package cz.muni.ucn.opsi.core.instalation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.muni.ucn.opsi.api.instalation.Instalation;
import cz.muni.ucn.opsi.api.instalation.InstalationService;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;

/**
 * @author Jan Dosoudil
 *
 */
@Service
public class InstalationServiceImpl implements InstalationService {

	private OpsiClientService opsiClientService;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.instalation.InstalationService#listInstalations()
	 */
	@Override
	public List<Instalation> listInstalations() {
		return opsiClientService.listInstalations();
	}

	/**
	 * @param opsiClientService the opsiClientService to set
	 */
	@Autowired
	public void setOpsiClientService(OpsiClientService opsiClientService) {
		this.opsiClientService = opsiClientService;
	}

}
