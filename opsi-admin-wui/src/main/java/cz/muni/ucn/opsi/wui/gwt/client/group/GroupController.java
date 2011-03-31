/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.group;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class GroupController extends Controller {

	public static final EventType GROUPS = new EventType();
	public static final EventType GROUP_NEW = new EventType();
	public static final EventType GROUP_EDIT = new EventType();
	public static final EventType GROUP_DELETE = new EventType();

	private GroupsView groupsView;
	private GroupEditView groupEditView;

	/**
	 *
	 */
	public GroupController() {
		registerEventTypes(GroupController.GROUPS);
		registerEventTypes(GroupController.GROUP_NEW);
		registerEventTypes(GroupController.GROUP_EDIT);
		registerEventTypes(GroupController.GROUP_DELETE);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (GroupController.GROUPS == type) {
			showGroups(event);
		} else if (GroupController.GROUP_NEW == type) {
			groupNew(event);
		} else if (GroupController.GROUP_EDIT == type) {
			groupEdit(event);
		} else if (GroupController.GROUP_DELETE == type) {
			groupDelete(event);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			onLifecycleEvent(event);
		}
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		groupsView = new GroupsView(this);
		groupEditView = new GroupEditView(this);
	}

	/**
	 * @param event
	 */
	private void showGroups(AppEvent event) {
		forwardToView(groupsView, event);
	}

	/**
	 * @param event
	 */
	private void groupNew(AppEvent event) {
		forwardToView(groupEditView, event);
	}

	/**
	 * @param event
	 */
	private void groupEdit(AppEvent event) {
		forwardToView(groupEditView, event);
	}

	/**
	 * @param event
	 */
	private void groupDelete(AppEvent event) {
		forwardToView(groupsView, event);
	}

	/**
	 * @param event
	 */
	private void onLifecycleEvent(AppEvent event) {
        LifecycleEventJSO le = (LifecycleEventJSO) event.getData();
        if (!"cz.muni.ucn.opsi.api.group.Group".equals(le.getBeanClass())) {
                return;
        }
		forwardToView(groupsView, event);
	}

}
