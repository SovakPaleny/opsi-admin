/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.event;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.login.LoginController;
import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.DefaultDomain;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * @author Jan Dosoudil
 *
 */
public class CometController extends Controller implements RemoteEventListener {

	public static final EventType LIFECYCLE_EVENT_TYPE = new EventType();
	private RemoteEventService eventService;
	private Domain lifecycleDomain = new DefaultDomain("lifecycleEvent");

	/**
	 *
	 */
	public CometController() {
		registerEventTypes(LoginController.LOGIN_OK);
		registerEventTypes(LoginController.LOGGED_OUT);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGIN_OK == type) {
			startCommet();
		} else if (LoginController.LOGGED_OUT == type) {
			stopCommet();
		}
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();

		RemoteEventServiceFactory serviceFactory = RemoteEventServiceFactory.getInstance();
		eventService = serviceFactory.getRemoteEventService();
	}

	/**
	 *
	 */
	private void startCommet() {
		GWT.log("startCommet");
		eventService.addListener(lifecycleDomain, this);
	}

	/**
	 *
	 */
	private void stopCommet() {
		GWT.log("stopCommet");
		eventService.removeListeners();
	}

	/* (non-Javadoc)
	 * @see de.novanic.eventservice.client.event.listener.RemoteEventListener#apply(de.novanic.eventservice.client.event.Event)
	 */
	@Override
	public void apply(Event anEvent) {
		LifecycleCometEvent lce = (LifecycleCometEvent) anEvent;
		GWT.log("message: " + lce.getJsonObject());

		LifecycleEventJSO event = LifecycleEventJSO.fromJSON(lce.getJsonObject());
		Dispatcher.forwardEvent(CometController.LIFECYCLE_EVENT_TYPE, event);


	}

}
