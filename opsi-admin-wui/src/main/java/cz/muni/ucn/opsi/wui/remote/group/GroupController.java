/**
 *
 */
package cz.muni.ucn.opsi.wui.remote.group;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.muni.ucn.opsi.api.group.Group;
import cz.muni.ucn.opsi.api.group.GroupService;

/**
 * @author Jan Dosoudil
 *
 */
@Controller
public class GroupController {

	private GroupService groupService;
	private Validator validator;

	/**
	 * @param groupService the groupService to set
	 */
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * @param validator the validator to set
	 */
	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@RequestMapping(value = "/groups/create", method = RequestMethod.GET)
	public @ResponseBody Group createGroup() {
		return groupService.createGroup();
	}

	@RequestMapping(value = "/groups/edit", method = RequestMethod.POST)
	public @ResponseBody Group editGroup(@RequestParam String uuid) {
		return groupService.editGroup(UUID.fromString(uuid));
	}

	@RequestMapping(value = "/groups/save", method = RequestMethod.PUT)
	public @ResponseBody void saveGroup(@RequestBody Group group) {
		Set<ConstraintViolation<Group>> validation = validator.validate(group);
		if (!validation.isEmpty()) {
			throw new ValidationException("Požadavek obsahuje chyby");
		}
		groupService.saveGroup(group);
	}

	@RequestMapping(value = "/groups/delete", method = RequestMethod.DELETE)
	public @ResponseBody void deleteGroup(@RequestBody Group group) {
		groupService.deleteGroup(group);
	}

	@RequestMapping(value = "/groups/list", method = RequestMethod.GET)
	public @ResponseBody List<Group> listGroups() {
		return groupService.listGroups();
	}

}
