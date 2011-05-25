/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelComparer;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelFactory;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientHardwareWindow extends Window {


	private ClientConstants clientConstants;
//	private FormPanel form;

	private ClientJSO client;
	private ListStore<BeanModel> hardwareStore;
	private Grid<BeanModel> hardwareGrid;
	private BeanModelFactory clientFactory;

	private int importCount = 0;

	/**
	 * @param newClient
	 */
	public ClientHardwareWindow(ClientJSO client) {
		this.client = client;

		clientConstants = GWT.create(ClientConstants.class);

		clientFactory = BeanModelLookup.get().getFactory(HardwareJSO.CLASS_NAME);

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(640, 350);
		setHeading("Import klientů");
//		setBodyStyle("padding: 0px; ");

//		FormLayout layout = new FormLayout();
//		layout.setLabelAlign(LabelAlign.LEFT);
//		setLayout(layout);
		setLayout(new FitLayout());

		hardwareStore = new ListStore<BeanModel>();
		hardwareStore.sort("name", SortDir.ASC);
		hardwareStore.setKeyProvider(new ModelKeyProvider<BeanModel>() {

			@Override
			public String getKey(BeanModel model) {
				return model.get("uuid");
			}
		});
		hardwareStore.setModelComparer(new ModelComparer<BeanModel>() {

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
		ColumnConfig description = new ColumnConfig("description", clientConstants.getDescription(), 180);
		ColumnConfig macAddress = new ColumnConfig("macAddress", clientConstants.getMacAddress(), 140);
		ColumnConfig ipAddress = new ColumnConfig("ipAddress", clientConstants.getIpAddress(), 80);

		final CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();

		List<ColumnConfig> config = new ArrayList<ColumnConfig>();

		config.add(sm.getColumn());
		config.add(name);
		config.add(description);
		config.add(macAddress);
		config.add(ipAddress);

		final ColumnModel cm = new ColumnModel(config);

		hardwareGrid = new Grid<BeanModel>(hardwareStore, cm);
		hardwareGrid.setBorders(true);
		hardwareGrid.setColumnReordering(true);
		hardwareGrid.setSelectionModel(sm);
		hardwareGrid.addPlugin(sm);

		GridFilters filters = new GridFilters();
		filters.setLocal(true);

		filters.addFilter(new StringFilter("name"));
		filters.addFilter(new StringFilter("description"));
		filters.addFilter(new StringFilter("ipAddress"));
		filters.addFilter(new StringFilter("macAddress"));

		hardwareGrid.addPlugin(filters);

		add(hardwareGrid, new FitData());


		hardwareGrid.mask(GXT.MESSAGES.loadMask_msg());

		ClientService clientService = ClientService.getInstance();

		clientService.listHardware(this.client, new RemoteRequestCallback<List<HardwareJSO>>() {
			@Override
			public void onRequestSuccess(List<HardwareJSO> clients) {
				List<BeanModel> hardwareModels = clientFactory.createModel(clients);
				hardwareStore.removeAll();
				hardwareStore.add(hardwareModels);
				hardwareGrid.unmask();

			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání hardware: ", th.getMessage());
				hardwareGrid.unmask();
			}
		});



		generateButtons();
	}

	/**
	 *
	 */
	private void generateButtons() {
		Button buttonCancel = new Button("Zavřít");
		buttonCancel.setIcon(IconHelper.createStyle("Cancel"));
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				ClientHardwareWindow.this.hide(ce.getButton());
			}
		});
		addButton(buttonCancel);

		Button buttonOK = new Button("Importovat vybrané položky");
		buttonOK.setIcon(IconHelper.createStyle("OK"));
		buttonOK.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {

				hardwareGrid.mask(GXT.MESSAGES.loadMask_msg());
				ClientHardwareWindow.this.disable();

				synchronizeState();

				ClientService clientService = ClientService.getInstance();

				List<BeanModel> selectedItems = hardwareGrid.getSelectionModel().getSelectedItems();

				importCount = selectedItems.size();

				for (final BeanModel beanModel : selectedItems) {
					ClientJSO client = beanModel.getBean();
					clientService.saveClient(client, new RemoteRequestCallback<Object>() {

						@Override
						public void onRequestSuccess(Object v) {
							if (--importCount <= 0) {
								ClientHardwareWindow.this.enable();
								hardwareGrid.unmask();

							}
//							ClientImportWindow.this.hide(ce.getButton());
							ClientHardwareWindow.this.hardwareStore.remove(beanModel);
						}

						@Override
						public void onRequestFailed(Throwable th) {
							if (--importCount <= 0) {
								ClientHardwareWindow.this.enable();
								hardwareGrid.unmask();
							}
							MessageDialog.showError("Nelze uložit klienta", th.getMessage());
						}
					});
				}

			}
		});
		addButton(buttonOK);

	}


	/**
	 *
	 */
	protected void synchronizeState() {
	}

}
