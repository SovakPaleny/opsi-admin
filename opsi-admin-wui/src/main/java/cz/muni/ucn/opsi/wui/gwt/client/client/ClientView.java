/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author dosoudilj
 *
 */
public class ClientView extends View {

	private ClientWindow window;

	/**
	 * @param controller
	 */
	public ClientView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (ClientController.CLIENTS == type) {
			showClients();
		} else if (ClientController.CLIENT_DELETE == type) {
			ClientJSO client = event.getData("client");
			groupDelete(client);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			LifecycleEventJSO lifecycleEventJSO = (LifecycleEventJSO)event.getData();
			onLifecycleEvent(lifecycleEventJSO);
		}
	}

	/**
	 *
	 */
	private void showClients() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				if (null == window) {
					window = new ClientWindow();
				}

				Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, window);
				if (window.isVisible()) {
					window.toFront();
				} else {
					window.show();
				}
			}

			@Override
			public void onFailure(Throwable reason) {
				Info.display("Akci nelze provést", reason.getMessage());
			}
		});

	}

	/**
	 *
	 */
	private void groupDelete(final ClientJSO client) {
		MessageBox.confirm("Odstranit klienta?",
				"Opravdu chcete klienta " + client.getName() + " odstranit? ",
				new Listener<MessageBoxEvent>() {

			@Override
			public void handleEvent(MessageBoxEvent be) {
				if (!Dialog.YES.equals(be.getButtonClicked().getItemId())) {
					return;
				}
				ClientService.getInstance().deleteClient(client, new RemoteRequestCallback<Object>() {
					@Override
					public void onRequestSuccess(Object v) {
						Info.display("Klient odstraněn", "");
					}

					@Override
					public void onRequestFailed(Throwable th) {
						Info.display("Chyba při ostraňování klienta", th.getMessage());
					}
				});
			}
		});

	}

	/**
	 * @param lifecycleEventJSO
	 */
	private void onLifecycleEvent(LifecycleEventJSO lifecycleEventJSO) {
		if (null == window) {
			return;
		}
		window.onLifecycleEvent(lifecycleEventJSO);

	}

}
