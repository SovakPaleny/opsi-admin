/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;

/**
 * @author Jan Dosoudil
 *
 */
public class MessageDialog {

	public static void showError(String title, String message) {
		MessageBox.alert(title, message, new Listener<MessageBoxEvent>() {

			@Override
			public void handleEvent(MessageBoxEvent be) {
			}});
	}

	/**
	 * @param string
	 */
	public static void showMessage(String title, String message) {
		MessageBox.info(title, message, new Listener<MessageBoxEvent>(){

			@Override
			public void handleEvent(MessageBoxEvent be) {
			}

		});
	}
}
