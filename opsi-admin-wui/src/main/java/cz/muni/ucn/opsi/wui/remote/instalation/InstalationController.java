/**
 *
 */
package cz.muni.ucn.opsi.wui.remote.instalation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.muni.ucn.opsi.api.instalation.Instalation;
import cz.muni.ucn.opsi.api.instalation.InstalationService;

/**
 * @author Jan Dosoudil
 *
 */
@Controller
public class InstalationController {

	private InstalationService instalationService;

	/**
	 * @param instalationService the instalationService to set
	 */
	@Autowired
	public void setInstalationService(InstalationService instalationService) {
		this.instalationService = instalationService;
	}

	@RequestMapping(value = "/instalation/list", method = RequestMethod.GET)
	public @ResponseBody List<Instalation> listInstalations() {
		return instalationService.listInstalations();
	}

	@RequestMapping(value = "/instalation/listAll", method = RequestMethod.GET)
	public @ResponseBody List<Instalation> listInstalationsAll() {
		return instalationService.listInstalationsAll();
	}

	@RequestMapping(value = "/instalation/save", method = RequestMethod.POST)
	public @ResponseBody void saveInstalations(@RequestBody InstalationList instalations) {
		instalationService.saveInstalations(instalations);
	}


	@SuppressWarnings("serial")
	public static class InstalationList extends ArrayList<Instalation> { }


}
