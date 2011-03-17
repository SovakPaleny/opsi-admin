/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.login;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;

/**
 * @author Jan Dosoudil
 *
 */
public class LoginController extends Controller {

	public static final EventType LOGOUT = new EventType();
	public static final EventType LOGGED_OUT = new EventType();


	/**
	 *
	 */
	public LoginController() {
		registerEventTypes(LoginController.LOGOUT);
		registerEventTypes(LoginController.LOGGED_OUT);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGOUT == type) {
			onLogoutEvent(event);
		}
	}

	/**
	/**
	 * @param event
	 *
	 */
	protected void onLogoutEvent(AppEvent event) {

		LoginService service = LoginService.getInstance();
		service.logout(new LoginService.LogoutCallback() {

			@Override
			public void onLogoutOk() {
				Dispatcher.forwardEvent(LoginController.LOGGED_OUT);
			}

			@Override
			public void onLogoutFailed(String message) {
				MessageDialog.showError("Chyba při odhlašování", message);
			}
		});
	}

}
