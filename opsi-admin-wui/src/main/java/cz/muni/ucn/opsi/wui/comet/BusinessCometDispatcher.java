/**
 *
 */
package cz.muni.ucn.opsi.wui.comet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleCometEvent;
import cz.u2.eis.api.events.RemoteEvent;
import cz.u2.eis.api.events.data.LifecycleEvent;
import de.novanic.eventservice.client.event.domain.DefaultDomain;
import de.novanic.eventservice.service.registry.EventRegistryFactory;

/**
 * @author Jan Dosoudil
 *
 */
@Component
public class BusinessCometDispatcher implements ApplicationListener<LifecycleEvent>,
		InitializingBean {

	/**
	 *
	 */
	private static final DefaultDomain DOMAIN = new DefaultDomain("lifecycleEvent");

	private static final Logger logger = LoggerFactory.getLogger(BusinessCometDispatcher.class);

	private JsonFactory jsonFactory;

	private ObjectMapper objectMapper;


	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(LifecycleEvent event) {
		if (!(event instanceof RemoteEvent)) {
			return;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			JsonGenerator generator = jsonFactory.createJsonGenerator(baos, JsonEncoding.UTF8);
			generator.writeObject(event);
			String message = baos.toString("utf-8");

			LifecycleCometEvent ce = new LifecycleCometEvent(message);
			EventRegistryFactory.getInstance().getEventRegistry().addEvent(DOMAIN, ce);
		} catch (JsonProcessingException e) {
			logger.warn("publishing event", e);
		} catch (IOException e) {
			logger.warn("publishing event", e);
		}

	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		jsonFactory = new JsonFactory();
		jsonFactory.setCodec(objectMapper);

	}

	/**
	 * @param objectMapper the objectMapper to set
	 */
	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}
