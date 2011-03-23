/**
 *
 */
package cz.muni.ucn.opsi.wui.remote.client;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.ClientService;
import cz.muni.ucn.opsi.api.instalation.Instalation;
import cz.muni.ucn.opsi.api.instalation.InstalationService;

/**
 * @author Jan Dosoudil
 *
 */
@Controller
public class ClientController {

	private ClientService clientService;
	private InstalationService instalationService;
	private Validator validator;

	/**
	 * @param clientService the clientService to set
	 */
	@Autowired
	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}
	/**
	 * @param instalationService the instalationService to set
	 */
	@Autowired
	public void setInstalationService(InstalationService instalationService) {
		this.instalationService = instalationService;
	}
	/**
	 * @param validator the validator to set
	 */
	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@RequestMapping(value = "/clients/create", method = RequestMethod.GET)
	public @ResponseBody Client createClient(@RequestParam String groupUuid) {
		return clientService.createClient(UUID.fromString(groupUuid));
	}

	@RequestMapping(value = "/clients/edit", method = RequestMethod.POST)
	public @ResponseBody Client editClient(@RequestParam String uuid) {
		return clientService.editClient(UUID.fromString(uuid));
	}

	@RequestMapping(value = "/clients/save", method = RequestMethod.PUT)
	public @ResponseBody void saveClient(@RequestBody Client client) {
		Set<ConstraintViolation<Client>> validation = validator.validate(client);
		if (!validation.isEmpty()) {
			throw new ValidationException("Požadavek obsahuje chyby");
		}
		clientService.saveClient(client);
	}

	@RequestMapping(value = "/clients/delete", method = RequestMethod.DELETE)
	public @ResponseBody void deleteClient(@RequestBody Client client) {
		clientService.deleteClient(client);
	}

	@RequestMapping(value = "/clients/install", method = RequestMethod.PUT)
	public @ResponseBody void installClient(@RequestBody Client client,
			@RequestParam String instalaceId) {

		Instalation i = instalationService.getInstalationById(instalaceId);

		clientService.installClient(client, i);
	}

	@RequestMapping(value = "/clients/list", method = RequestMethod.GET)
	public @ResponseBody List<Client> listClients(@RequestParam String groupUuid) {
		return clientService.listClients(UUID.fromString(groupUuid));
	}

	@RequestMapping(value = "/clients/import/list", method = RequestMethod.GET)
	public @ResponseBody List<Client> listClientsForImport(@RequestParam String groupUuid) {
		return clientService.listClientsForImport(UUID.fromString(groupUuid));
	}

	@ExceptionHandler
	public void exceptionHandler(Throwable exception,
			HttpServletResponse response) throws IOException {

		String message = exception.getMessage();

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);

//		RequestError error = new RequestError(message);
//		return error;
	}

}
