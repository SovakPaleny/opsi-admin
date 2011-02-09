/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.Shortcut;
import com.extjs.gxt.desktop.client.StartMenu;
import com.extjs.gxt.desktop.client.TaskBar;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.json.client.JSONObject;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupController;
import cz.muni.ucn.opsi.wui.gwt.client.login.LoginController;

/**
 * @author Jan Dosoudil
 *
 */
public class DesktopController extends Controller {

	private Desktop desktop;

	public static final EventType WINDOW_CREATED = new EventType();
	public static final EventType WINDOW_DESTROYED = new EventType();

	private boolean first = true;

	/**
	 *
	 */
	public DesktopController() {
		registerEventTypes(LoginController.LOGIN_OK);
		registerEventTypes(LoginController.LOGGED_OUT);
		registerEventTypes(DesktopController.WINDOW_CREATED);
		registerEventTypes(DesktopController.WINDOW_DESTROYED);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client
	 * .mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGIN_OK == type) {
			showDesktop();
			JSONObject object = event.getData();
			desktop.getStartMenu().setHeading(object.get("displayName").isString().stringValue());
		} else if (LoginController.LOGGED_OUT == type) {

			List<Window> windows = desktop.getWindows();
			List<Window> w2 = new ArrayList<Window>(windows);
			for (Window w : w2) {
				w.hide();
				desktop.removeWindow(w);
			}

			List<Shortcut> shortcuts = desktop.getShortcuts();
			List<Shortcut> sh2 = new ArrayList<Shortcut>(shortcuts);
			for (Shortcut sh: sh2) {
				desktop.removeShortcut(sh);
			}

			desktop.getStartMenu().removeAll();

			Info.display("Odhlášení", "Odhlášení proběhlo úspěšně");
			Dispatcher.forwardEvent(LoginController.LOGIN);
		} else if (DesktopController.WINDOW_CREATED == type) {
			Window w = event.getData();
			if (!desktop.getWindows().contains(w)) {
				desktop.addWindow(w);
			}
		}
	}

	private void itemSelected(final ComponentEvent ce) {
		Window w;
		EventType event;
		if (ce instanceof MenuEvent) {
			MenuEvent me = (MenuEvent) ce;
			w = me.getItem().getData("window");
			event = me.getItem().getData("event");
		} else {
			w = ce.getComponent().getData("window");
			event = ce.getComponent().getData("event");
		}
		if (w != null) {

			if (!desktop.getWindows().contains(w)) {
				desktop.addWindow(w);
			}
			if (w != null && !w.isVisible()) {
				w.show();
			} else {
				w.toFront();
			}
		} else if (event != null) {
			Dispatcher.forwardEvent(event);
		}
	}

	public void showDesktop() {


//		desktop.getStartMenu().removeAll();
//		desktop.getDesktop().removeAll();


		SelectionListener<MenuEvent> menuListener = new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent me) {
				itemSelected(me);
			}
		};

		SelectionListener<ComponentEvent> shortcutListener = new SelectionListener<ComponentEvent>() {
			@Override
			public void componentSelected(ComponentEvent ce) {
				itemSelected(ce);
			}
		};

		Shortcut s3 = new Shortcut();
		s3.setText("Skupiny");
		s3.setId("users-shortcut");
		s3.setData("event", GroupController.GROUPS);
		s3.addSelectionListener(shortcutListener);
		desktop.addShortcut(s3);
/*
		Shortcut s4 = new Shortcut();
		s4.setText("Organizační struktura");
		s4.setId("organization-shortcut");
		s4.setData("event", OrganizationController.ORGANIZATION);
		s4.addSelectionListener(shortcutListener);
		desktop.addShortcut(s4);

		Shortcut s5 = new Shortcut();
		s5.setText("Úkoly");
		s5.setId("tasks-shortcut");
		s5.setData("event", TasksController.TASKS);
		s5.addSelectionListener(shortcutListener);
		desktop.addShortcut(s5);
*/
		TaskBar taskBar = desktop.getTaskBar();

		StartMenu menu = taskBar.getStartMenu();
		menu.setHeading("Test");
		menu.setIconStyle("user");

		MenuItem menuItem = new MenuItem("Skupiny");
		menuItem.setIcon(IconHelper.createStyle("groups"));
		menuItem.addSelectionListener(menuListener);
		menuItem.setData("event", GroupController.GROUPS);
		menu.add(menuItem);
		/*
		menuItem = new MenuItem("Organizační struktura");
		menuItem.setIcon(IconHelper.createStyle("organization"));
		menuItem.addSelectionListener(menuListener);
		menuItem.setData("event", OrganizationController.ORGANIZATION);
		menu.add(menuItem);

		menuItem = new MenuItem("Úkoly");
		menuItem.setIcon(IconHelper.createStyle("tasks"));
		menuItem.addSelectionListener(menuListener);
		menuItem.setData("event", TasksController.TASKS);
		menu.add(menuItem);
*/
		if (!first) {
			return;
		}
		first = false;

		// tools
		MenuItem tool = new MenuItem("Nastavení");
		tool.setIcon(IconHelper.createStyle("settings"));
		tool.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				Info.display("Event", "The 'Settings' tool was clicked");
			}
		});
		menu.addTool(tool);

		menu.addToolSeperator();

		tool = new MenuItem("Odhlásit");
		tool.setIcon(IconHelper.createStyle("logout"));
		tool.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				Dispatcher.forwardEvent(LoginController.LOGOUT);
			}
		});
		menu.addTool(tool);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		desktop = new Desktop();
	}

}
