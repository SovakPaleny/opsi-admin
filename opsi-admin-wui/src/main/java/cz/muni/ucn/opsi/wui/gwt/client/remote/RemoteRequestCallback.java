/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.remote;

/**
 * @author Jan Dosoudil
 *
 */
public interface RemoteRequestCallback<V> {
	void onRequestSuccess(V v);
	void onRequestFailed(Throwable th);
}
