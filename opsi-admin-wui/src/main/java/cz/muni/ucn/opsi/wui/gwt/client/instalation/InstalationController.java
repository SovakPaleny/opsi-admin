/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class InstalationController extends Controller {

	public static final EventType INSTALATIONS = new EventType();
	public static final EventType INSTALATIONS_SAVE = new EventType();

	private InstalationView instalationView;

	/**
	 *
	 */
	public InstalationController() {
		registerEventTypes(InstalationController.INSTALATIONS);
		registerEventTypes(InstalationController.INSTALATIONS_SAVE);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (InstalationController.INSTALATIONS == type) {
			showInstalations(event);
		} else if (InstalationController.INSTALATIONS_SAVE == type) {
				saveInstalations(event);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			onLifecycleEvent(event);
		}
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		instalationView = new InstalationView(this);
	}

	/**
	 * @param event
	 */
	private void showInstalations(AppEvent event) {
		forwardToView(instalationView, event);
	}

	/**
	 * @param event
	 */
	private void saveInstalations(AppEvent event) {
		forwardToView(instalationView, event);
	}

	/**
	 * @param event
	 */
	private void onLifecycleEvent(AppEvent event) {
        LifecycleEventJSO le = (LifecycleEventJSO) event.getData();
        if (!"cz.muni.ucn.opsi.api.instalation.Instalation".equals(le.getBeanClass())) {
                return;
        }
		forwardToView(instalationView, event);
	}

}
