/**
 *
 */
package cz.muni.ucn.opsi.wui.gwtLogin.client.login;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * @author Jan Dosoudil
 *
 */
public class LoginController extends Controller {

	public static final EventType LOGIN = new EventType();
	public static final EventType LOGOUT = new EventType();
	public static final EventType LOGIN_OK = new EventType();
	public static final EventType LOGIN_FAIL = new EventType();
	public static final EventType LOGGED_OUT = new EventType();

	private LoginView loginView;

	/**
	 *
	 */
	public LoginController() {
		registerEventTypes(LoginController.LOGIN);
		registerEventTypes(LoginController.LOGOUT);
		registerEventTypes(LoginController.LOGIN_OK);
		registerEventTypes(LoginController.LOGIN_FAIL);
		registerEventTypes(LoginController.LOGGED_OUT);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGIN == type) {
			onLoginEvent(event);
		} else if (LoginController.LOGOUT == type) {
			onLogoutEvent(event);
		} else if (LoginController.LOGIN_OK == type) {
			onLoginOkEvent(event);
		} else if (LoginController.LOGIN_FAIL == type) {
			onLoginFailEvent(event);
		}
	}

	/**
	 *
	 */
	protected void onLoginEvent(final AppEvent event) {

		LoginService service = LoginService.getInstance();
		service.getLoginStatus(new LoginService.LoginStatusCallback() {

			@Override
			public void onStatusOk(JSONObject object) {
				JSONString authenticatedString = (JSONString) object.get("status");
				if ("OK".equals(authenticatedString.stringValue())) {
					Dispatcher.forwardEvent(LoginController.LOGIN_OK, object);
				} else {
					forwardToView(loginView, event);
				}
			}

			@Override
			public void onStatusFailed(String message) {
				Info.display("Nelze zjistit stav přihlášní", message);
			}
		});

	}


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
				Info.display("Chyba při odhlašování", message);
			}
		});
	}


	/**
	 * @param event
	 *
	 */
	protected void onLoginOkEvent(AppEvent event) {
		Window.Location.assign("index.html");
	}


	/**
	 * @param event
	 *
	 */
	protected void onLoginFailEvent(AppEvent event) {

	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		loginView = new LoginView(this);
	}

}
