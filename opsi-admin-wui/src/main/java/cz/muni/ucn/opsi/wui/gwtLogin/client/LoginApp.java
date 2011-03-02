/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 *
 * http://extjs.com/license
 */
package cz.muni.ucn.opsi.wui.gwtLogin.client;

import com.extjs.gxt.themes.client.Slate;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.google.gwt.core.client.EntryPoint;

import cz.muni.ucn.opsi.wui.gwtLogin.client.login.LoginController;

public class LoginApp implements EntryPoint {

	public void onModuleLoad() {
		ThemeManager.register(Slate.SLATE);
		GXT.setDefaultTheme(Slate.SLATE, true);

		Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(new LoginController());

		dispatcher.dispatch(LoginController.LOGIN);

		GXT.hideLoadingPanel("loading");
	}
}
