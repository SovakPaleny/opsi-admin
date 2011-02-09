/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.group;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelComparer;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelFactory;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class GroupsWindow extends Window {

	private BeanModelFactory factory;
	private ListStore<BeanModel> store;
	private GroupConstants groupConstants;
	private Grid<BeanModel> grid;
	private Button buttonNew;
	private Button buttonEdit;
	private Button buttonRemove;
	private MenuItem contextMenuNew;
	private MenuItem contextMenuEdit;
	private MenuItem contextMenuRemove;

	/**
	 * @param lifecycleEventJSO
	 */
	public void onLifecycleEvent(LifecycleEventJSO lifecycleEventJSO) {
		factory = BeanModelLookup.get().getFactory(GroupJSO.CLASS_NAME);

		groupConstants = GWT.create(GroupConstants.class);


		setMinimizable(true);
		setMaximizable(true);
		setHeading("Správa skupin");
		setSize(840, 400);
		setLayout(new FitLayout());

		ToolBar toolbar = createToolbar();

		setTopComponent(toolbar);

		store = new ListStore<BeanModel>();
		store.sort("name", SortDir.ASC);
		store.setKeyProvider(new ModelKeyProvider<BeanModel>() {

			@Override
			public String getKey(BeanModel model) {
				return model.get("uuid");
			}
		});
		store.setModelComparer(new ModelComparer<BeanModel>() {

			@Override
			public boolean equals(BeanModel m1, BeanModel m2) {
				if (m1 == m2) {
					return true;
				}
				if (m1 == null || m2 == null) {
					return false;
				}
				return m1.get("uuid").equals(m2.get("uuid"));
			}
		});

		GroupService groupService = GroupService.getInstance();

		groupService.listGroups(new RemoteRequestCallback<List<GroupJSO>>() {
			@Override
			public void onRequestSuccess(List<GroupJSO> groups) {
				List<BeanModel> groupModels = factory.createModel(groups);
				store.removeAll();
				store.add(groupModels);
			}

			@Override
			public void onRequestFailed(Throwable th) {
				Info.display("Chyba při získávání seznamu skupin: ", th.getMessage());
			}
		});

		ColumnConfig nazev = new ColumnConfig("name", groupConstants.getName(), 180);
		ColumnConfig role = new ColumnConfig("role", groupConstants.getRole(), 180);

		List<ColumnConfig> config = new ArrayList<ColumnConfig>();

		config.add(nazev);
		config.add(role);

		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<BeanModel>(store, cm);
		grid.setBorders(true);
		grid.setColumnReordering(true);

		SelectionChangedListener<BeanModel> selectionListener = new SelectionChangedListener<BeanModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				if (se.getSelection().size() != 1) {
					buttonEdit.disable();
					buttonRemove.disable();
					contextMenuEdit.disable();
					contextMenuRemove.disable();
				} else {
					buttonEdit.enable();
					buttonRemove.enable();
					contextMenuEdit.enable();
					contextMenuRemove.enable();
				}
			}
		};
		grid.getSelectionModel().addSelectionChangedListener(selectionListener);

		GridFilters filters = new GridFilters();
		filters.setLocal(true);

		filters.addFilter(new StringFilter("group"));
		filters.addFilter(new StringFilter("role"));

		grid.addPlugin(filters);

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<BeanModel>>() {

			@Override
			public void handleEvent(GridEvent<BeanModel> be) {

				EventType type = GroupController.GROUP_EDIT;
				GroupJSO group = be.getModel().getBean();
				Dispatcher.forwardEvent(type, group);
			}

		});

		grid.setContextMenu(createGridContextMenu());

		add(grid);



	}

	/**
	 * @return
	 */
	private ToolBar createToolbar() {
		SelectionListener<ButtonEvent> buttonListener = new ToolbarButtonListener();

		ToolBar toolbar = new ToolBar();

		buttonNew = new Button(groupConstants.getGroupNew());
		buttonNew.setIcon(IconHelper.createStyle("add"));
		buttonNew.setData("event", GroupController.GROUP_NEW);
		buttonNew.addSelectionListener(buttonListener);
		toolbar.add(buttonNew);

		buttonEdit = new Button(groupConstants.getGroupEdit());
		buttonEdit.setIcon(IconHelper.createStyle("edit"));
		buttonEdit.setData("event", GroupController.GROUP_EDIT);
		buttonEdit.addSelectionListener(buttonListener);
		buttonEdit.disable();
		toolbar.add(buttonEdit);

		buttonRemove = new Button(groupConstants.getGroupDelete());
		buttonRemove.setIcon(IconHelper.createStyle("remove"));
		buttonRemove.setData("event", GroupController.GROUP_DELETE);
		buttonRemove.addSelectionListener(buttonListener);
		buttonRemove.disable();
		toolbar.add(buttonRemove);
		return toolbar;
	}

	/**
	 * @return
	 */
	private Menu createGridContextMenu() {
		SelectionListener<? extends MenuEvent> buttonListener = new GridContextMenuListener();

		Menu menu = new Menu();

		contextMenuNew = new MenuItem(groupConstants.getGroupNew());
		contextMenuNew.setIcon(IconHelper.createStyle("add"));
		contextMenuNew.setData("event", GroupController.GROUP_NEW);
		contextMenuNew.addSelectionListener(buttonListener);
		menu.add(contextMenuNew);

		contextMenuEdit = new MenuItem(groupConstants.getGroupEdit());
		contextMenuEdit.setIcon(IconHelper.createStyle("edit"));
		contextMenuEdit.setData("event", GroupController.GROUP_EDIT);
		contextMenuEdit.addSelectionListener(buttonListener);
		menu.add(contextMenuEdit);

		contextMenuRemove = new MenuItem(groupConstants.getGroupDelete());
		contextMenuRemove.setIcon(IconHelper.createStyle("remove"));
		contextMenuRemove.setData("event", GroupController.GROUP_DELETE);
		contextMenuRemove.addSelectionListener(buttonListener);
		menu.add(contextMenuRemove);

		return menu;
	}

	/**
	 * @author Jan Dosoudil
	 *
	 */
	private final class ToolbarButtonListener extends SelectionListener<ButtonEvent> {
		@Override
		public void componentSelected(ButtonEvent ce) {
			EventType type = ce.getButton().getData("event");
			GroupJSO group;
			if (GroupController.GROUP_NEW == type) {
				group = null;
			} else {
				group = grid.getSelectionModel().getSelectedItem().getBean();
			}

			Dispatcher.forwardEvent(type, group);
		}
	}

	/**
	 * @author Jan Dosoudil
	 *
	 */
	private final class GridContextMenuListener extends	SelectionListener<MenuEvent> {
		@Override
		public void componentSelected(MenuEvent ce) {
			EventType type = ce.getItem().getData("event");
			GroupJSO group;
			if (GroupController.GROUP_NEW == type) {
				group = null;
			} else {
				group = grid.getSelectionModel().getSelectedItem().getBean();
			}

			Dispatcher.forwardEvent(type, group);
		}
	}

}
