/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientImportCSVView extends ClientImportView {


	protected String data;


	/**
	 * @param controller
	 */
	public ClientImportCSVView(Controller controller) {
		super(controller);
	}

	public EventType getWindowEventType() {
		return ClientController.CLIENT_IMPORT_CSV;
	}



	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.wui.gwt.client.client.ClientImportView#importClientAsync(cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO)
	 */
	protected void importClientAsync(final GroupJSO group) {

		final ClientImportCSVFileDialog dialog = new ClientImportCSVFileDialog(group);
		dialog.addListener(ClientImportCSVFileDialog.IMPORT_EVENT_TYPE, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				ClientImportCSVView.this.data = dialog.getReturnedData();
				importGroupWindow(group, true);
			}
		});

		dialog.show();
	}


	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.wui.gwt.client.client.ClientImportView#createWindow(cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO)
	 */
	protected ClientImportWindow createWindow(GroupJSO group) {
		return new ClientImportCSVWindow(group, this.data);
	}

}
