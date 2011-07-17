/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class ClientImportCSVFileDialog extends Dialog {

	private static final String IMPORT_CSV_URL = "remote/clients/importCsv";
	private FormPanel form;
	protected String returnedData;

	public static final EventType IMPORT_EVENT_TYPE = new EventType();

	/**
	 *
	 */
	public ClientImportCSVFileDialog(GroupJSO group) {
		super();

		setButtons(OKCANCEL);

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(350, 120);
		setLayout(new FitLayout());

		form = new FormPanel();
		form.setHeaderVisible(false);
		form.setLabelWidth(100);
		form.setBodyBorder(false);
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);
		form.setAction(GWT.getHostPageBaseURL() + IMPORT_CSV_URL);

		FileUploadField uploadField = new FileUploadField();
		uploadField.setName("importFile");
		uploadField.setAllowBlank(false);
		uploadField.setFieldLabel("CSV soubor");


		form.add(uploadField, new FormData("100%"));

		HiddenField<String> groupUuid = new HiddenField<String>();
		groupUuid.setValue(group.getUuid());
		groupUuid.setName("groupUuid");
		form.add(groupUuid);

		form.addListener(Events.Submit, new Listener<FormEvent>() {

			@Override
			public void handleEvent(FormEvent fe) {
				String data = fe.getResultHtml();
				ClientImportCSVFileDialog.this.returnedData = data;
				GWT.log(data);
				ClientImportCSVFileDialog.this.fireEvent(IMPORT_EVENT_TYPE);
				ClientImportCSVFileDialog.this.hide();
			}
		});

		add(form, new FitData());

	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Dialog#onButtonPressed(com.extjs.gxt.ui.client.widget.button.Button)
	 */
	protected void onButtonPressed(Button button) {
		super.onButtonPressed(button);
		if (button == getButtonBar().getItemByItemId(OK)) {
			form.submit();
		}
	}

	/**
	 * @return the returnedData
	 */
	public String getReturnedData() {
		return returnedData;
	}
}
