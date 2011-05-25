/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientHardwareView extends View {

	/**
	 * @param controller
	 */
	public ClientHardwareView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (ClientController.CLIENT_HARDWARE == type) {
			List<BeanModel> clients = event.getData("clients");
			for (BeanModel beanModel : clients) {
				ClientJSO client = beanModel.getBean();
				clientHardware(client);
			}
		}
	}

	/**
	 * @param client
	 */
	private void clientHardware(final ClientJSO client) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				clientHardwareAsync(client);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());

			}
		});
	}

	/**
	 * @param client
	 */
	protected void clientHardwareAsync(ClientJSO client) {
		clientHardwareWindow(client);
	}

	/**
	 * @param client
	 * @param b
	 */
	protected void clientHardwareWindow(ClientJSO client) {
		ClientHardwareWindow w = createWindow(client);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * @param client
	 * @param newGroup
	 * @return
	 */
	private ClientHardwareWindow createWindow(ClientJSO client) {
		return new ClientHardwareWindow(client);
	}

}
