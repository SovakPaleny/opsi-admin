/**
 *
 */
package cz.muni.ucn.opsi.wui.gwtLogin.client.login;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * @author Jan Dosoudil
 *
 */
public class LoginController extends Controller {

	public static final EventType LOGIN = new EventType();
	public static final EventType LOGIN_OK = new EventType();
	public static final EventType LOGIN_FAIL = new EventType();

	private LoginView loginView;

	/**
	 *
	 */
	public LoginController() {
		registerEventTypes(LoginController.LOGIN);
		registerEventTypes(LoginController.LOGIN_OK);
		registerEventTypes(LoginController.LOGIN_FAIL);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGIN == type) {
			onLoginEvent(event);
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
				MessageBox.alert("Nelze zjistit stav přihlášní", message,
						new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
							}
						});
			}
		});

	}

	/**
	 * @param event
	 *
	 */
	protected void onLoginOkEvent(AppEvent event) {
		Window.Location.assign("index.jsp");
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
