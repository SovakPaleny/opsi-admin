/**
 *
 */
package cz.muni.ucn.opsi.wui.jackson;

import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @author Jan Dosoudil
 *
 */
public class SerializableJsonSerializer extends JsonSerializer<Serializable> {


	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(Serializable value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		provider.findTypedValueSerializer(value.getClass(), true).serialize(value, jgen, provider);
	}

}
