/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;
import cz.u2.eis.api.events.data.LifecycleEvent;

/**
 * @author Jan Dosoudil
 *
 */
@Component
public class OpsiSynchronizer implements ApplicationListener<LifecycleEvent> {

	private OpsiClientService clientService;

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
		} else if (LifecycleEvent.DELETED == event.getEventType()) {
			deleteClient(client);
		} else if (LifecycleEvent.MODIFIED == event.getEventType()) {
			updateClient(client);
		}

	}

	/**
	 * @param client
	 */
	protected void createClient(Client client) {
		clientService.createClient(client);
	}

	/**
	 * @param client
	 */
	private void deleteClient(Client client) {
		clientService.deleteClient(client);
	}

	/**
	 * @param client
	 */
	private void updateClient(Client client) {
		clientService.updateClient(client);
	}



	/**
	 * @param clientService the clientService to set
	 */
	@Autowired
	public void setClientService(OpsiClientService clientService) {
		this.clientService = clientService;
	}
}
