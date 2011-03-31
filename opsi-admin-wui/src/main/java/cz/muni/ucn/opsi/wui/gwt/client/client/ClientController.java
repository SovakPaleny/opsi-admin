/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientController extends Controller {

	public static final EventType CLIENTS = new EventType();
	public static final EventType CLIENT_NEW = new EventType();
	public static final EventType CLIENT_EDIT = new EventType();
	public static final EventType CLIENT_DELETE = new EventType();
	public static final EventType CLIENT_INSTALL = new EventType();
	public static final EventType CLIENT_IMPORT = new EventType();

	private ClientView clientView;
	private ClientEditView clientEditView;
	private ClientImportView clientImportView;

	/**
	 *
	 */
	public ClientController() {
		registerEventTypes(ClientController.CLIENTS);
		registerEventTypes(ClientController.CLIENT_NEW);
		registerEventTypes(ClientController.CLIENT_EDIT);
		registerEventTypes(ClientController.CLIENT_DELETE);
		registerEventTypes(ClientController.CLIENT_INSTALL);
		registerEventTypes(ClientController.CLIENT_IMPORT);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (ClientController.CLIENTS == type) {
			showClients(event);
		} else if (ClientController.CLIENT_NEW == type) {
			clientNew(event);
		} else if (ClientController.CLIENT_EDIT == type) {
			clientEdit(event);
		} else if (ClientController.CLIENT_DELETE == type) {
			clientDelete(event);
		} else if (ClientController.CLIENT_INSTALL == type) {
			clientInstall(event);
		} else if (ClientController.CLIENT_IMPORT == type) {
			clientImport(event);
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
		clientView = new ClientView(this);
		clientEditView = new ClientEditView(this);
		clientImportView = new ClientImportView(this);
	}

	/**
	 * @param event
	 */
	private void showClients(AppEvent event) {
		forwardToView(clientView, event);
	}

	/**
	 * @param event
	 */
	private void clientNew(AppEvent event) {
		forwardToView(clientEditView, event);
	}

	/**
	 * @param event
	 */
	private void clientEdit(AppEvent event) {
		forwardToView(clientEditView, event);
	}

	/**
	 * @param event
	 */
	private void clientDelete(AppEvent event) {
		forwardToView(clientView, event);
	}

	/**
	 * @param event
	 */
	private void clientInstall(AppEvent event) {
		forwardToView(clientView, event);
	}

	/**
	 * @param event
	 */
	private void clientImport(AppEvent event) {
		forwardToView(clientImportView, event);
	}

	/**
	 * @param event
	 */
	private void onLifecycleEvent(AppEvent event) {
        LifecycleEventJSO le = (LifecycleEventJSO) event.getData();
        if (!"cz.muni.ucn.opsi.api.client.Client".equals(le.getBeanClass())
        		&& !"cz.muni.ucn.opsi.api.group.Group".equals(le.getBeanClass())) {
                return;
        }
		forwardToView(clientView, event);
	}

}
