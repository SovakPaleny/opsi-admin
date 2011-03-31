/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.instalation;

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
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.DualListField.Mode;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelFactory;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class InstalationWindow extends Window {

	private BeanModelFactory factory;
	private InstalationConstants instalationConstants;
	private Button buttonSave;
	private ListStore<BeanModel> fromStore;
	private ListStore<BeanModel> toStore;
	private boolean loadedFrom = false;
	private boolean loadedTo = false;
	private DualListField<BeanModel> lists;

	/**
	 *
	 */
	public InstalationWindow() {
		factory = BeanModelLookup.get().getFactory(InstalaceJSO.CLASS_NAME);

		instalationConstants = GWT.create(InstalationConstants.class);


		setMinimizable(true);
		setMaximizable(true);
		setHeading("Správa instalací");
		setSize(840, 400);
		setLayout(new FitLayout());

		ToolBar toolbar = createToolbar();

		setTopComponent(toolbar);


		ModelKeyProvider<BeanModel> keyProvider = new ModelKeyProvider<BeanModel>() {

			@Override
			public String getKey(BeanModel model) {
				return model.get("id");
			}
		};
		ModelComparer<BeanModel> comparer = new ModelComparer<BeanModel>() {

			@Override
			public boolean equals(BeanModel m1, BeanModel m2) {
				if (m1 == m2) {
					return true;
				}
				if (m1 == null || m2 == null) {
					return false;
				}
				return m1.get("id").equals(m2.get("id"));
			}
		};

	    lists = new DualListField<BeanModel>();
	    lists.setMode(Mode.APPEND);
	    lists.setFieldLabel("instalace");

	    ListField<BeanModel> from = lists.getFromList();
	    from.setDisplayField("name");
	    fromStore = new ListStore<BeanModel>();
	    fromStore.sort("name", SortDir.ASC);
	    fromStore.setKeyProvider(keyProvider);
	    fromStore.setModelComparer(comparer);
	    from.setStore(fromStore);

	    ListField<BeanModel> to = lists.getToList();
	    to.setDisplayField("name");
	    toStore = new ListStore<BeanModel>();
	    toStore.sort("name", SortDir.ASC);
	    toStore.setKeyProvider(keyProvider);
	    toStore.setModelComparer(comparer);
	    to.setStore(toStore);


		lists.mask(GXT.MESSAGES.loadMask_msg());


	    InstalationService groupService = InstalationService.getInstance();

		groupService.listInstalationsAll(new RemoteRequestCallback<List<InstalaceJSO>>() {
			@Override
			public void onRequestSuccess(List<InstalaceJSO> instalations) {
				List<BeanModel> groupModels = factory.createModel(instalations);
				fromStore.removeAll();
				fromStore.add(groupModels);
				loadedFrom = true;
				updateState();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání seznamu všech instalací: ", th.getMessage());
			}
		});

		groupService.listInstalations(new RemoteRequestCallback<List<InstalaceJSO>>() {
			@Override
			public void onRequestSuccess(List<InstalaceJSO> instalations) {
				List<BeanModel> groupModels = factory.createModel(instalations);
				toStore.removeAll();
				toStore.add(groupModels);
				loadedTo = true;
				updateState();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání seznamu instalací: ", th.getMessage());
			}
		});

		add(lists);



	}

	/**
	 *
	 */
	protected void updateState() {
		if (!loadedFrom || !loadedTo) {
			return;
		}
		for (int i = 0; i < toStore.getCount(); i++) {
			BeanModel m = toStore.getAt(i);
			fromStore.remove(m);
		}
		lists.unmask();

	}

	/**
	 * @return
	 */
	private ToolBar createToolbar() {
		ToolBar toolbar = new ToolBar();

		buttonSave = new Button(instalationConstants.getInstalationsSave());
		buttonSave.setIcon(IconHelper.createStyle("save"));
		buttonSave.setData("event", InstalationController.INSTALATIONS_SAVE);
		buttonSave.addSelectionListener(new SaveButtonListener());
		toolbar.add(buttonSave);

		return toolbar;
	}

	/**
	 * @param le
	 */
	public void onLifecycleEvent(LifecycleEventJSO le) {
		BeanModel model = factory.createModel(le.getBean());
		if (LifecycleEventJSO.CREATED == le.getEventType()) {
//			store.add(model);
		} else if (LifecycleEventJSO.MODIFIED == le.getEventType()) {
//			store.update(model);
		} else if (LifecycleEventJSO.DELETED == le.getEventType()) {
//			store.remove(model);
		}

	}

	/**
	 * @author Jan Dosoudil
	 *
	 */
	private final class SaveButtonListener extends SelectionListener<ButtonEvent> {
		@Override
		public void componentSelected(ButtonEvent ce) {
			List<BeanModel> instalations = toStore.getModels();

			InstalationService instalationService = InstalationService.getInstance();
			List<InstalaceJSO> instalace = new ArrayList<InstalaceJSO>();
			for (BeanModel beanModel : instalations) {
				instalace.add((InstalaceJSO) beanModel.getBean());
			}
			instalationService.saveInstalations(instalace, new RemoteRequestCallback<Object>() {

				@Override
				public void onRequestSuccess(Object v) {
					MessageDialog.showMessage("Uloženo", "Seznam instalací byl úspěšně uložen.");
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze uložit seznam instalací", th.getMessage());
				}
			});

		}
	}

}
