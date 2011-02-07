/**
 *
 */
package cz.muni.ucn.opsi.wui.jackson;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Jan Dosoudil
 *
 */
//@JsonTypeInfo(include=As.PROPERTY, use=Id.CLASS)
public abstract class ValueObjectMixin {

	@JsonIgnore
	public abstract String getUuidString();

}
