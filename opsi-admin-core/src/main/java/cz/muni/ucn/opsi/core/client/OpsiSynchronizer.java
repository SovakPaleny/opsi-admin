/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import java.io.Serializable;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cz.muni.ucn.opsi.api.client.Client;
import cz.u2.eis.api.events.data.LifecycleEvent;

/**
 * @author Jan Dosoudil
 *
 */
@Component
public class OpsiSynchronizer implements ApplicationListener<LifecycleEvent> {

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(LifecycleEvent event) {
		if (!(event.getBean() instanceof Client)) {
			return;
		}

		Client client = (Client) event.getBean();
		if (LifecycleEvent.CREATED == event.getEventType()) {
			createClient(client);
		}

	}

	/**
	 * @param client
	 */
	protected void createClient(Client client) {

	}


}
