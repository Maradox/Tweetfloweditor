package at.tuwien.dsgproject.tfe.dialogs;

import android.app.Dialog;
import android.content.Context;
import at.tuwien.dsgproject.tfe.R;

public class ChangeDataDialog extends Dialog {
	
	private String user;
	private String operation;
	private String service;
	private String inputdata;
	private String condition;
	
	private String loopCondition;

	public ChangeDataDialog(Context context) {
		super(context);
		this.setContentView(R.layout.dialog_changedata);
	}

}
