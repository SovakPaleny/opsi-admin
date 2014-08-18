/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
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
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelFactory;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupConstants;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupService;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstalaceJSO;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstalationService;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientWindow extends Window {

	private BeanModelFactory clientFactory;
	private BeanModelFactory groupFactory;
	private ListStore<BeanModel> clientStore;
	private GroupConstants groupConstants;
	private ClientConstants clientConstants;
	private Grid<BeanModel> clientsGrid;
	private Button buttonNew;
	private Button buttonEdit;
	private Button buttonRemove;
	private Button buttonImport;
	private MenuItem contextMenuNew;
	private MenuItem contextMenuEdit;
	private MenuItem contextMenuRemove;
	private ListStore<BeanModel> groupsStore;
	private Grid<BeanModel> groupGrid;
	private BeanModel selectedGroupItem;
	private Button buttonInstall;

	/**
	 *
	 */
	public ClientWindow() {
		clientFactory = BeanModelLookup.get().getFactory(ClientJSO.CLASS_NAME);
		groupFactory = BeanModelLookup.get().getFactory(GroupJSO.CLASS_NAME);

		clientConstants = GWT.create(ClientConstants.class);
		groupConstants = GWT.create(GroupConstants.class);


		setMinimizable(true);
		setMaximizable(true);
		setHeading("Správa klientů");
		setSize(900, 400);
		setLayout(new FitLayout());

		ToolBar toolbar = createToolbar();

		setTopComponent(toolbar);

		createGroups();

		createClients();


		ContentPanel panel = new ContentPanel();
		panel.setHeaderVisible(false);
		panel.setLayout(new BorderLayout());

		BorderLayoutData west = new BorderLayoutData(LayoutRegion.WEST);
		west.setCollapsible(true);
		west.setFloatable(true);
		west.setSplit(true);

		BorderLayoutData center = new BorderLayoutData(LayoutRegion.CENTER);

		panel.add(groupGrid, west);
		panel.add(clientsGrid, center);

		add(panel);



	}

	/**
	 *
	 */
	private void createGroups() {
		groupsStore = new ListStore<BeanModel>();
		groupsStore.sort("name", SortDir.ASC);
		groupsStore.setKeyProvider(new ModelKeyProvider<BeanModel>() {

			@Override
			public String getKey(BeanModel model) {
				return model.get("uuid");
			}
		});
		groupsStore.setModelComparer(new ModelComparer<BeanModel>() {

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

		ColumnConfig nazev = new ColumnConfig("name", groupConstants.getName(), 180);

		List<ColumnConfig> config = new ArrayList<ColumnConfig>();

		config.add(nazev);

		final ColumnModel cm = new ColumnModel(config);

		groupGrid = new Grid<BeanModel>(groupsStore, cm);
		groupGrid.setBorders(true);
		groupGrid.setColumnReordering(true);

		SelectionChangedListener<BeanModel> selectionListener = new SelectionChangedListener<BeanModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				if (se.getSelection().size() != 1) {
					buttonNew.disable();
					buttonEdit.disable();
					buttonRemove.disable();
					buttonInstall.disable();
					contextMenuNew.disable();
					contextMenuEdit.disable();
					contextMenuRemove.disable();
					updateClients(null);
				} else {
					buttonNew.enable();
//					buttonEdit.enable();
//					buttonRemove.enable();
//					buttonInstall.enable();
					buttonImport.enable();
					contextMenuNew.enable();
//					contextMenuEdit.enable();
//					contextMenuRemove.enable();
					updateClients(se.getSelectedItem());
				}
			}
		};
		groupGrid.getSelectionModel().addSelectionChangedListener(selectionListener);

		GridFilters filters = new GridFilters();
		filters.setLocal(true);

		filters.addFilter(new StringFilter("group"));

		groupGrid.addPlugin(filters);


		GroupService groupService = GroupService.getInstance();

		groupGrid.mask(GXT.MESSAGES.loadMask_msg());

		groupService.listGroups(new RemoteRequestCallback<List<GroupJSO>>() {
			@Override
			public void onRequestSuccess(List<GroupJSO> groups) {
				List<BeanModel> groupModels = clientFactory.createModel(groups);
				groupsStore.removeAll();
				groupsStore.add(groupModels);
				groupGrid.unmask();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání seznamu skupin: ", th.getMessage());
			}
		});


	}

	private void createClients() {
		clientStore = new ListStore<BeanModel>();
		clientStore.sort("name", SortDir.ASC);
		clientStore.setKeyProvider(new ModelKeyProvider<BeanModel>() {

			@Override
			public String getKey(BeanModel model) {
				return model.get("uuid");
			}
		});
		clientStore.setModelComparer(new ModelComparer<BeanModel>() {

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

		ColumnConfig name = new ColumnConfig("name", clientConstants.getName(), 180);
		ColumnConfig description = new ColumnConfig("description", clientConstants.getDescription(), 80);
		ColumnConfig notes = new ColumnConfig("notes", clientConstants.getNotes(), 180);
		ColumnConfig macAddress = new ColumnConfig("macAddress", clientConstants.getMacAddress(), 140);
		ColumnConfig ipAddress = new ColumnConfig("ipAddress", clientConstants.getIpAddress(), 80);

		final CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();

		List<ColumnConfig> config = new ArrayList<ColumnConfig>();

		config.add(sm.getColumn());
		config.add(name);
		config.add(macAddress);
		config.add(description);
		config.add(notes);
		config.add(ipAddress);

		final ColumnModel cm = new ColumnModel(config);

		clientsGrid = new Grid<BeanModel>(clientStore, cm);
		clientsGrid.setBorders(true);
		clientsGrid.setColumnReordering(true);
		clientsGrid.setSelectionModel(sm);
		clientsGrid.addPlugin(sm);

		SelectionChangedListener<BeanModel> selectionListener = new SelectionChangedListener<BeanModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				int selectionSize = se.getSelection().size();
				if (selectionSize != 1) {
					buttonEdit.disable();
					contextMenuEdit.disable();
				} else {
					buttonEdit.enable();
					contextMenuEdit.enable();
				}
				if (selectionSize >= 1) {
					buttonInstall.enable();
					buttonRemove.enable();
					contextMenuRemove.enable();
				} else {
					buttonInstall.disable();
					buttonRemove.disable();
					contextMenuRemove.disable();
				}
			}
		};
		clientsGrid.getSelectionModel().addSelectionChangedListener(selectionListener);

		GridFilters filters = new GridFilters();
		filters.setLocal(true);

		filters.addFilter(new StringFilter("name"));
		filters.addFilter(new StringFilter("description"));
		filters.addFilter(new StringFilter("ipAddress"));
		filters.addFilter(new StringFilter("macAddress"));

		clientsGrid.addPlugin(filters);

		clientsGrid.addListener(Events.RowDoubleClick, new Listener<GridEvent<BeanModel>>() {

			@Override
			public void handleEvent(GridEvent<BeanModel> be) {

				EventType type = ClientController.CLIENT_EDIT;
				List<BeanModel> l = new ArrayList<BeanModel>();
				l.add(be.getModel());
				AppEvent event = new AppEvent(type);
				event.setData("clients", l);
				event.setData("group", getSelectedGroupItem().getBean());
				Dispatcher.forwardEvent(event);
			}

		});

		clientsGrid.setContextMenu(createGridContextMenu());
	}

	/**
	 * @param selectedItem
	 */
	protected void updateClients(BeanModel selectedItem) {
		this.selectedGroupItem = selectedItem;

		if (null == selectedItem) {
			clientStore.removeAll();
			return;
		}

		ClientService clientService = ClientService.getInstance();
		clientsGrid.mask(GXT.MESSAGES.loadMask_msg());

		GroupJSO group = selectedItem.getBean();
		clientService.listClients(group, new RemoteRequestCallback<List<ClientJSO>>() {
			@Override
			public void onRequestSuccess(List<ClientJSO> clients) {
				List<BeanModel> clientModels = clientFactory.createModel(clients);
				clientStore.removeAll();
				clientStore.add(clientModels);
				clientsGrid.unmask();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání seznamu skupin: ", th.getMessage());
				clientsGrid.unmask();
			}
		});
	}

	/**
	 * @return the selectedGroupItem
	 */
	public BeanModel getSelectedGroupItem() {
		return selectedGroupItem;
	}

	/**
	 * @return
	 */
	private ToolBar createToolbar() {
		SelectionListener<ButtonEvent> buttonListener = new ToolbarButtonListener();
		GridContextMenuListener menuListener = new GridContextMenuListener();

		ToolBar toolbar = new ToolBar();

		buttonNew = new Button(clientConstants.getClientNew());
		buttonNew.setIcon(IconHelper.createStyle("add"));
		buttonNew.setData("event", ClientController.CLIENT_NEW);
		buttonNew.addSelectionListener(buttonListener);
		buttonNew.disable();
		toolbar.add(buttonNew);

		buttonEdit = new Button(clientConstants.getClientEdit());
		buttonEdit.setIcon(IconHelper.createStyle("edit"));
		buttonEdit.setData("event", ClientController.CLIENT_EDIT);
		buttonEdit.addSelectionListener(buttonListener);
		buttonEdit.disable();
		toolbar.add(buttonEdit);

		buttonRemove = new Button(clientConstants.getClientDelete());
		buttonRemove.setIcon(IconHelper.createStyle("remove"));
		buttonRemove.setData("event", ClientController.CLIENT_DELETE);
		buttonRemove.addSelectionListener(buttonListener);
		buttonRemove.disable();
		toolbar.add(buttonRemove);

		buttonInstall = new Button(clientConstants.getClientInstall());
		buttonInstall.setIcon(IconHelper.createStyle("install"));
		buttonInstall.disable();
		buttonInstall.setMenu(createInstallMenu());
		toolbar.add(buttonInstall);

//		buttonImport = new Button(clientConstants.getClientImport());
//		buttonImport.setIcon(IconHelper.createStyle("import"));
//		buttonImport.setData("event", ClientController.CLIENT_IMPORT);
//		buttonImport.addSelectionListener(buttonListener);
//		buttonImport.disable();
//		toolbar.add(buttonImport);

		buttonImport = new Button(clientConstants.getClientImport());
		buttonImport.setIcon(IconHelper.createStyle("import"));
		buttonImport.disable();
		toolbar.add(buttonImport);

		final Menu importMenu = new Menu();

		MenuItem importOpsi = new MenuItem(clientConstants.getClientImportOpsi());
		importOpsi.setIcon(IconHelper.createStyle("import"));
		importOpsi.setData("event", ClientController.CLIENT_IMPORT);
		importOpsi.addSelectionListener(menuListener);
		importMenu.add(importOpsi);

		MenuItem importOpsi2 = new MenuItem(clientConstants.getClientImportOpsi2());
		importOpsi2.setIcon(IconHelper.createStyle("import"));
		importOpsi2.setData("event", ClientController.CLIENT_IMPORT2);
		importOpsi2.addSelectionListener(menuListener);
		importMenu.add(importOpsi2);
		
		MenuItem importCSV = new MenuItem(clientConstants.getClientImportCSV());
		importCSV.setIcon(IconHelper.createStyle("import"));
		importCSV.setData("event", ClientController.CLIENT_IMPORT_CSV);
		importCSV.addSelectionListener(menuListener);
		importMenu.add(importCSV);

		buttonImport.setMenu(importMenu);


		return toolbar;
	}

	/**
	 * @return
	 */
	private Menu createInstallMenu() {
		final Menu installMenu = new Menu();
		final SelectionListener<? extends MenuEvent> installListener = new InstalaceMenuListener();

		InstalationService service = InstalationService.getInstance();
		service.listInstalations(new RemoteRequestCallback<List<InstalaceJSO>>() {

			@Override
			public void onRequestSuccess(List<InstalaceJSO> v) {
				for (InstalaceJSO in : v) {
					MenuItem mi = new MenuItem(in.getName());
					mi.addSelectionListener(installListener);
					mi.setData("instalace", in);
					mi.setData("event", ClientController.CLIENT_INSTALL);
					installMenu.add(mi);
				}
			}

			@Override
			public void onRequestFailed(Throwable th) {
			}
		});

		return installMenu;
	}

	/**
	 * @return
	 */
	private Menu createGridContextMenu() {
		SelectionListener<? extends MenuEvent> buttonListener = new GridContextMenuListener();

		Menu menu = new Menu();

		contextMenuNew = new MenuItem(clientConstants.getClientNew());
		contextMenuNew.setIcon(IconHelper.createStyle("add"));
		contextMenuNew.setData("event", ClientController.CLIENT_NEW);
		contextMenuNew.addSelectionListener(buttonListener);
		menu.add(contextMenuNew);

		contextMenuEdit = new MenuItem(clientConstants.getClientEdit());
		contextMenuEdit.setIcon(IconHelper.createStyle("edit"));
		contextMenuEdit.setData("event", ClientController.CLIENT_EDIT);
		contextMenuEdit.addSelectionListener(buttonListener);
		menu.add(contextMenuEdit);

		contextMenuRemove = new MenuItem(clientConstants.getClientDelete());
		contextMenuRemove.setIcon(IconHelper.createStyle("remove"));
		contextMenuRemove.setData("event", ClientController.CLIENT_DELETE);
		contextMenuRemove.addSelectionListener(buttonListener);
		menu.add(contextMenuRemove);

		return menu;
	}

	/**
	 * @param le
	 */
	public void onLifecycleEvent(LifecycleEventJSO le) {

		ListStore<BeanModel> store = clientStore;

		BeanModel model;
        if ("cz.muni.ucn.opsi.api.client.Client".equals(le.getBeanClass())) {
        	store = clientStore;
			model = clientFactory.createModel(le.getBean());

			if (getSelectedGroupItem() == null) {
				return;
			}
			String selectedGroupUuid = ((GroupJSO)getSelectedGroupItem().getBean()).getUuid();
			String eventGroupUuid = ((ClientJSO)model.getBean()).getGroup().getUuid();
			if (!selectedGroupUuid.equals(eventGroupUuid)) {
				return;
			}

        } else if ("cz.muni.ucn.opsi.api.group.Group".equals(le.getBeanClass())) {
        	store = groupsStore;
        	model = groupFactory.createModel(le.getBean());
        } else {
        	return;
        }



		if (LifecycleEventJSO.CREATED == le.getEventType()) {
			store.add(model);
		} else if (LifecycleEventJSO.MODIFIED == le.getEventType()) {
			store.update(model);
		} else if (LifecycleEventJSO.DELETED == le.getEventType()) {
			store.remove(model);
		}

	}

	/**
	 * @author Jan Dosoudil
	 *
	 */
	private final class ToolbarButtonListener extends SelectionListener<ButtonEvent> {
		@Override
		public void componentSelected(ButtonEvent ce) {

			EventType type = ce.getButton().getData("event");
			List<BeanModel> clients;
			if (ClientController.CLIENT_NEW == type) {
				clients = null;
			} else {
				clients = clientsGrid.getSelectionModel().getSelectedItems();
			}
			if (null == clients) {
				AppEvent event = new AppEvent(type);
//				event.setData("client", null);
				event.setData("group", getSelectedGroupItem().getBean());
				Dispatcher.forwardEvent(event);
			} else {
				AppEvent event = new AppEvent(type);
				event.setData("clients", clients);
				event.setData("group", getSelectedGroupItem().getBean());
				Dispatcher.forwardEvent(event);

			}

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

			List<BeanModel> clients;
			if (ClientController.CLIENT_NEW == type) {
				clients = null;
			} else {
				clients = clientsGrid.getSelectionModel().getSelectedItems();
			}
			if (null == clients) {
				AppEvent event = new AppEvent(type);
//				event.setData("client", null);
				event.setData("group", getSelectedGroupItem().getBean());
				Dispatcher.forwardEvent(event);
			} else {
				AppEvent event = new AppEvent(type);
				event.setData("clients", clients);
				event.setData("group", getSelectedGroupItem().getBean());
				Dispatcher.forwardEvent(event);

			}

		}
	}


	/**
	 * @author Jan Dosoudil
	 *
	 */
	private final class InstalaceMenuListener extends SelectionListener<MenuEvent> {
		@Override
		public void componentSelected(MenuEvent ce) {
			final EventType type = ce.getItem().getData("event");
			final InstalaceJSO instalace = ce.getItem().getData("instalace");
			final List<BeanModel> clients = clientsGrid.getSelectionModel().getSelectedItems();

			MessageBox.confirm("Provést instalaci?",
					"Opravdu provést instalaci " + instalace.getName() +
					" na " + clients.size() + " počítačů?" ,
					new Listener<MessageBoxEvent>() {

						@Override
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked() == null) {
								return;
							}
							if (!Dialog.YES.equals(be.getButtonClicked().getItemId())) {
								return;
							}
							AppEvent event = new AppEvent(type);
							event.setData("clients", clients);
							event.setData("instalace", instalace);
							event.setData("group", getSelectedGroupItem().getBean());
							Dispatcher.forwardEvent(event);

						}

			});


		}
	}

}
