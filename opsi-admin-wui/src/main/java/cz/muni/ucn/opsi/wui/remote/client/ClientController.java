/**
 *
 */
package cz.muni.ucn.opsi.wui.remote.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;
import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.ClientService;
import cz.muni.ucn.opsi.api.client.Hardware;
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
	private ObjectMapper mapper;

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
	/**
	 * @param mapper the mapper to set
	 */
	@Autowired
	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
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
	public @ResponseBody List<Client> listClientsForImport(@RequestParam String groupUuid,
			@RequestParam String opsi) {
		return clientService.listClientsForImport(UUID.fromString(groupUuid), opsi);
	}

	@RequestMapping(value = "/clients/hardware/list", method = RequestMethod.GET)
	public @ResponseBody List<Hardware> listHardware(@RequestParam String uuid) {
		return clientService.listHardare(UUID.fromString(uuid));
	}

	@RequestMapping(value = "/clients/importCsv", method = RequestMethod.POST)
	public void importCSV(@RequestParam String groupUuid,
			@RequestParam("importFile") MultipartFile file,
			HttpServletResponse response, OutputStream os) throws IOException {

		UUID group = UUID.fromString(groupUuid);

		List<Client> clients = new ArrayList<Client>();
		//FIXME obsluha zavirani streamu
		CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()), ';');
		String[] record;
		while((record  = reader.readNext()) != null ) {
			if (record.length != 2) {
				continue;
			}
			Client c = clientService.createClient(group);
			c.setName(record[0]);
			c.setMacAddress(record[1]);
			clients.add(c);
		}

		response.setContentType("text/html");
		mapper.writeValue(os, clients);


	}
}
