/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author dosoudilj
 *
 */
public class ClientEditView extends View {

	private Map<ClientJSO, ClientEditWindow> windows = new HashMap<ClientJSO, ClientEditWindow>();

	/**
	 * @param controller
	 */
	public ClientEditView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		GroupJSO group = (GroupJSO) event.getData("group");
		if (ClientController.CLIENT_NEW == type) {
			clientEdit(group, null);
		} else if (ClientController.CLIENT_EDIT == type) {
			List<BeanModel> clients = event.getData("clients");
			for (BeanModel beanModel : clients) {
				ClientJSO client = beanModel.getBean();
				clientEdit(group, client);
			}
		}
	}

	/**
	 * @param data
	 */
	private void clientEdit(final GroupJSO group, final ClientJSO client) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				editClientAsync(group, client);
			}

			@Override
			public void onFailure(Throwable reason) {
				Info.display("Operaci nelze provést", reason.getMessage());
			}
		});

	}

	/**
	 * @param client
	 */
	protected void editClientAsync(GroupJSO group, ClientJSO client) {
		ClientService clientService = ClientService.getInstance();
		if (null == client) {
			clientService.createClient(group, new RemoteRequestCallback<ClientJSO>() {
				@Override
				public void onRequestSuccess(ClientJSO user) {
					editGroupWindow(user, true);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					Info.display("Nelze založit klienta", th.getMessage());
				}
			});
		} else {
			clientService.editClient(client, new RemoteRequestCallback<ClientJSO>() {
				@Override
				public void onRequestSuccess(ClientJSO client) {
					editGroupWindow(client, false);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					Info.display("Nelze upravit klienta", th.getMessage());
				}
			});

		}

	}

	/**
	 * @param client
	 * @param b
	 */
	protected void editGroupWindow(ClientJSO client, boolean newGroup) {
		ClientEditWindow w;
		if (windows.containsKey(client)) {
			w = windows.get(client);
			w.setGroupModel(client);
		} else {
			w = createWindow(client, newGroup);
			windows.put(client, w);
			Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		}
		w.setGroupModel(client);
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
	private ClientEditWindow createWindow(ClientJSO group, boolean newGroup) {
		return new ClientEditWindow(newGroup);
	}

}
