/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientEditWindow extends Window {

	/**
	 *
	 */
	private static final String NAME_REGEXP = "^([a-zA-Z0-9\\-\\_]+)\\.([a-zA-Z0-9\\-\\_\\.]+)$";

	private static final String FIELD_SPEC = "-18";

	private final boolean newClient;
	private ClientConstants clientConstants;
	private FormPanel form;

	private FormBinding binding;

	private ClientJSO client;

	/**
	 * @param newClient
	 */
	public ClientEditWindow(boolean newClient) {
		this.newClient = newClient;

		clientConstants = GWT.create(ClientConstants.class);

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(400, 200);
//		setBodyStyle("padding: 0px; ");

//		FormLayout layout = new FormLayout();
//		layout.setLabelAlign(LabelAlign.LEFT);
//		setLayout(layout);
		setLayout(new FitLayout());


		form = new FormPanel();
		form.setHeaderVisible(false);
		form.setLabelWidth(100);
		form.setBodyBorder(false);

		ContentPanel borderPanel = new ContentPanel();
		borderPanel.setLayout(new RowLayout(Orientation.VERTICAL));
		borderPanel.setHeaderVisible(false);
//		rowPanel.setFrame(false);
		borderPanel.setBodyBorder(false);
//		rowPanel.setBorders(false);
		borderPanel.setBodyStyle("background: transparent;");

		form.add(borderPanel, new FormData("100% 100%"));

		ContentPanel formPanel = new ContentPanel();
		FormLayout formPanelLayout = new FormLayout();
		formPanel.setLayout(formPanelLayout);
		formPanel.setHeaderVisible(false);
//		formPanel.setFrame(false);
		formPanel.setBodyBorder(false);
//		formPanel.setBorders(false);
		formPanel.setBodyStyle("background: transparent;");

		borderPanel.add(formPanel, new RowData(1, -1));

		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel(clientConstants.getName());
		name.setMinLength(1);
		name.setAllowBlank(false);
		name.setAutoValidate(true);
		name.setValidator(new Validator() {

			@Override
			public String validate(Field<?> field, String value) {
				if (!value.matches(NAME_REGEXP)) {
					return "Pole nevyhovuje formátu: " + NAME_REGEXP;
				}
				return null;
			}
		});

		if (!newClient) {
			name.setReadOnly(true);
		}

		formPanel.add(name, new FormData(FIELD_SPEC));

		TextField<String> description = new TextField<String>();
		description.setName("description");
		description.setFieldLabel(clientConstants.getDescription());
		formPanel.add(description, new FormData(FIELD_SPEC));

		TextField<String> ipAddress = new TextField<String>();
		ipAddress.setName("ipAddress");
		ipAddress.setFieldLabel(clientConstants.getIpAddress());
		ipAddress.setValidator(new Validator() {

			@Override
			public String validate(Field<?> field, String value) {
				if (value.isEmpty()) {
					return null;
				}
				String[] split = value.split("\\.");
				if (split.length == 4) {
					int errs = 0;
					for (int i = 0; i < split.length; i++) {
						int p = Integer.parseInt(split[i]);
						if (p >= 0 && p <= 255) {
							continue;
						}
						errs++;
					}
					if (0 == errs) {
						return null;
					}
				}
				return "Vyplňte platnou IPv4 adresu.";
			}
		});
		formPanel.add(ipAddress, new FormData(FIELD_SPEC));

		TextField<String> macAddress = new TextField<String>();
		macAddress.setName("macAddress");
		macAddress.setFieldLabel(clientConstants.getMacAddress());
		macAddress.setValidator(new Validator() {

			@Override
			public String validate(Field<?> field, String value) {
				if (value.matches("^([0-9a-fA-F]{1,2}:){5}([0-9a-fA-F]{1,2})$")) {
					return null;
				}
				return "Zadejte platnou MAC adresu ve tvaru 01:23:45:67:89:ab";
			}
		});
		formPanel.add(macAddress, new FormData(FIELD_SPEC));


		add(form, new FitData());

		binding = new FormBinding(form, true);
		binding.setUpdateOriginalValue(true);

		generateButtons();
	}

	/**
	 *
	 */
	private void generateButtons() {
		Button buttonCancel = new Button("Zrušit");
		buttonCancel.setIcon(IconHelper.createStyle("Cancel"));
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				ClientEditWindow.this.hide(ce.getButton());
			}
		});
		addButton(buttonCancel);

		Button buttonOK = new Button("Uložit");
		buttonOK.setIcon(IconHelper.createStyle("OK"));
		buttonOK.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {

				if (!validate()) {
					MessageBox.alert("Nelze uložit", "Formulář obsahuje chyby",
							new Listener<MessageBoxEvent>() {

						@Override
						public void handleEvent(MessageBoxEvent be) {
						}
					});
					return;
				}

				ClientEditWindow.this.disable();

				synchronizeState();

				ClientService groupService = ClientService.getInstance();
				groupService.saveClient(client, new RemoteRequestCallback<Object>() {
					@Override
					public void onRequestSuccess(Object v) {
						ClientEditWindow.this.enable();
						ClientEditWindow.this.hide(ce.getButton());
					}

					@Override
					public void onRequestFailed(Throwable th) {
						ClientEditWindow.this.enable();
						MessageDialog.showError("Nelze uložit skupinu", th.getMessage());
					}
				});

			}
		});
		addButton(buttonOK);

	}

	/**
	 * @param group
	 */
	public void setGroupModel(ClientJSO group) {
		this.client = group;
		BeanModel model = BeanModelLookup.get().getFactory(ClientJSO.CLASS_NAME).createModel(group);
		binding.bind(model);

		updateHeading();
	}

	/**
	 *
	 */
	private void updateHeading() {
		if (newClient) {
			setHeading("Nová skupina");
		} else {
			setHeading("Úprava skupiny: " + client.getName());
		}
	}

	/**
	 *
	 */
	protected void synchronizeState() {
	}

	/**
	 * @return
	 */
	protected boolean validate() {
		List<Field<?>> fields = form.getFields();
		boolean valid = true;
		for (Field<?> field : fields) {
			if (!field.validate()) {
				valid = false;
			}
		}
		return valid;
	}

}
