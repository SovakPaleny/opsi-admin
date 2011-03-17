/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.group;

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author dosoudilj
 *
 */
public class GroupEditView extends View {

	private Map<GroupJSO, GroupEditWindow> windows = new HashMap<GroupJSO, GroupEditWindow>();

	/**
	 * @param controller
	 */
	public GroupEditView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (GroupController.GROUP_NEW == type) {
			userEdit(null);
		} else if (GroupController.GROUP_EDIT == type) {
			userEdit((GroupJSO) event.getData());
		}
	}

	/**
	 * @param data
	 */
	private void userEdit(final GroupJSO group) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				editGroupAsync(group);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());
			}
		});

	}

	/**
	 * @param group
	 */
	protected void editGroupAsync(GroupJSO group) {
		GroupService groupService = GroupService.getInstance();
		if (null == group) {
			groupService.createGroup(new RemoteRequestCallback<GroupJSO>() {
				@Override
				public void onRequestSuccess(GroupJSO client) {
					editGroupWindow(client, true);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze založit skupinu", th.getMessage());
				}
			});
		} else {
			groupService.editGroup(group, new RemoteRequestCallback<GroupJSO>() {
				@Override
				public void onRequestSuccess(GroupJSO client) {
					editGroupWindow(client, false);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze upravit skupinu", th.getMessage());
				}
			});

		}

	}

	/**
	 * @param group
	 * @param b
	 */
	protected void editGroupWindow(GroupJSO group, boolean newGroup) {
		GroupEditWindow w;
		if (windows.containsKey(group)) {
			w = windows.get(group);
			w.setGroupModel(group);
		} else {
			w = createWindow(group, newGroup);
			windows.put(group, w);
			Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		}
		w.setGroupModel(group);
		if (w.isVisible()) {
			w.toFront();
		} else {
			w.show();
		}

	}

	/**
	 * @param group
	 * @param newGroup
	 * @return
	 */
	private GroupEditWindow createWindow(GroupJSO group, boolean newGroup) {
		return new GroupEditWindow(newGroup);
	}

}
