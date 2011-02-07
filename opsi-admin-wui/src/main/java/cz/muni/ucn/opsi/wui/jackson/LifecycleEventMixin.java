/**
 *
 */
package cz.muni.ucn.opsi.wui.jackson;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;

/**
 * @author Jan Dosoudil
 *
 */
@JsonTypeInfo(include=As.PROPERTY, use=Id.CLASS)
public abstract class LifecycleEventMixin {

	@JsonIgnore
    public abstract Object getSource();

	@JsonSerialize(typing=Typing.DYNAMIC, using=SerializableJsonSerializer.class)
	public abstract Serializable getBean();

}
