/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

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
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * @author dosoudilj
 *
 */
public class ClientImportView extends View {

	/**
	 * @param controller
	 */
	public ClientImportView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		GroupJSO group = (GroupJSO) event.getData("group");
		if (ClientController.CLIENT_IMPORT == type) {
			clientImport(group);
		}
	}

	/**
	 * @param group
	 */
	private void clientImport(final GroupJSO group) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				importClientAsync(group);
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
	protected void importClientAsync(GroupJSO group) {
		importGroupWindow(group);
	}

	/**
	 * @param client
	 * @param b
	 */
	protected void importGroupWindow(GroupJSO group) {
		ClientImportWindow w = createWindow(group);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * @param group
	 * @param newGroup
	 * @return
	 */
	private ClientImportWindow createWindow(GroupJSO group) {
		return new ClientImportWindow(group);
	}

}
