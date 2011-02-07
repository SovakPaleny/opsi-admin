/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 *
 * http://extjs.com/license
 */
package cz.muni.ucn.opsi.wui.gwt.client;

import com.extjs.gxt.themes.client.Slate;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.google.gwt.core.client.EntryPoint;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.login.LoginController;

public class OpsiAdminApp implements EntryPoint {

	public void onModuleLoad() {
		ThemeManager.register(Slate.SLATE);
		GXT.setDefaultTheme(Slate.SLATE, true);
//		ThemeManager.register(Theme.GRAY);
//		GXT.setDefaultTheme(Theme.GRAY, true);

		Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(new LoginController());
		dispatcher.addController(new DesktopController());
		dispatcher.addController(new CometController());

		dispatcher.dispatch(LoginController.LOGIN);

		GXT.hideLoadingPanel("loading");
	}
}
