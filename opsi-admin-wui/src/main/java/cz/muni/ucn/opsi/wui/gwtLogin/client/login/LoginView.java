/**
 *
 */
package cz.muni.ucn.opsi.wui.gwtLogin.client.login;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;

/**
 * @author Jan Dosoudil
 *
 */
public class LoginView extends View {

	private LoginDialog dialog;

	/**
	 * @param controller
	 */
	public LoginView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGIN == type) {
			dialog.show();
		}
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		dialog = new LoginDialog();
		dialog.setClosable(false);
	}

}
