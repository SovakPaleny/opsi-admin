/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.google.gwt.i18n.client.Constants;

/**
 * @author Jan Dosoudil
 *
 */
public interface ClientConstants extends Constants {
	@Key("client.name")
	String getName();
	@Key("client.description")
	String getDescription();
	@Key("client.ipAddress")
	String getIpAddress();
	@Key("client.macAddress")
	String getMacAddress();
	@Key("client.clientNew")
	String getClientNew();
	@Key("client.clientEdit")
	String getClientEdit();
	@Key("client.clientDelete")
	String getClientDelete();
}
