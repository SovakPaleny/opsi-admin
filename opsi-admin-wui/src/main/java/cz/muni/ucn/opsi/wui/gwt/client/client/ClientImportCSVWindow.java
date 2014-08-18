/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientImportCSVWindow extends  ClientImportWindow {

	private final String data;


	/**
	 * @param group
	 */
	public ClientImportCSVWindow(GroupJSO group, String data) {
		super(group, true);
		this.data = data;
	}


	protected void loadData() {

		JsArray<ClientJSO> clientsA = ClientJSO.fromJSONArray(data);
		List<ClientJSO> clients = new ArrayList<ClientJSO>();
		for(int i = 0; i < clientsA.length(); i++) {
			clients.add(clientsA.get(i));
		}

		List<BeanModel> clientModels = clientFactory.createModel(clients);
		clientStore.add(clientModels);
		clientsGrid.unmask();
	}

}
