/**
 *
 */
package cz.muni.ucn.opsi.wui.jackson;

import java.util.UUID;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Jan Dosoudil
 *
 */
@JsonAutoDetect(value={JsonMethod.NONE})
public abstract class StubMixin {

	@JsonProperty
	public abstract UUID getUuid();
}
