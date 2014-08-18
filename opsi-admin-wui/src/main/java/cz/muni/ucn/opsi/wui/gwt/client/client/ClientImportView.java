/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * @author Jan Dosoudil
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
		if (getWindowEventType() == type) {
			clientImport(group, true);
		} else if (getWindowEventType2() == type) {
			clientImport(group, false);
		}
	}

	public EventType getWindowEventType() {
		return ClientController.CLIENT_IMPORT;
	}
	public EventType getWindowEventType2() {
		return ClientController.CLIENT_IMPORT2;
	}

	/**
	 * @param group
	 * @param master 
	 */
	private void clientImport(final GroupJSO group, final boolean master) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				importClientAsync(group, master);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());

			}
		});
	}

	/**
	 * @param group
	 * @param master 
	 */
	protected void importClientAsync(GroupJSO group, boolean master) {
		importGroupWindow(group, master);
	}

	/**
	 * @param master 
	 * @param client
	 * @param b
	 */
	protected void importGroupWindow(GroupJSO group, boolean master) {
		ClientImportWindow w = createWindow(group, master);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * @param group
	 * @param master 
	 * @param newGroup
	 * @return
	 */
	protected ClientImportWindow createWindow(GroupJSO group, boolean master) {
		return new ClientImportWindow(group, master);
	}

}
