package at.tuwien.dsgproject.tfe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.activities.Editor;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class ChangeDataDialog extends Dialog {
	
	private AbstractElement element; 
	private EditorView editorView;
	
	private EditText user;
	private EditText operation;
	private EditText service;
	private EditText inputdata;
	private EditText condition;
	
	private EditText selfLoopCondition;
	private EditText closedLoopCondition;
	
	private TableRow rowClosedLoopCondition;

	public ChangeDataDialog(Context context, EditorView editorView, AbstractElement element) {
		super(context);
		this.editorView = editorView;
		this.element = element;
		this.setContentView(R.layout.dialog_changedata);
		
		user = (EditText) findViewById(R.id.dialog_user);
		operation = (EditText) findViewById(R.id.dialog_operation);
		service = (EditText) findViewById(R.id.dialog_service);
		inputdata = (EditText) findViewById(R.id.dialog_inputdata);
		condition = (EditText) findViewById(R.id.dialog_condition);
		selfLoopCondition = (EditText) findViewById(R.id.dialog_selfloopcondition);
		closedLoopCondition = (EditText) findViewById(R.id.dialog_closedloopcondition);
		rowClosedLoopCondition = (TableRow) findViewById(R.id.dialog_row_closedloopcondition);
		
		if(element.getmLoop() == null) 
			rowClosedLoopCondition.setVisibility(View.GONE);
		else
			rowClosedLoopCondition.setVisibility(View.VISIBLE);
				
		loadData();
		
		((Button) findViewById(R.id.dialog_positiveButton)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveData();
				ChangeDataDialog.this.cancel();
			}
		});
		
		((Button) findViewById(R.id.dialog_negativeButton)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ChangeDataDialog.this.cancel();
			}
		});
	}
	
	private void loadData() {
		if(element.getUser() != null)
			user.setText(element.getUser());
		if(element.getOperation() != null)
			operation.setText(element.getOperation());
		if(element.getService() != null)
			service.setText(element.getService());
		if(element.getInputdata() != null)
			inputdata.setText(element.getInputdata());
		if(element.getCondition() != null)
			condition.setText(element.getCondition());
		if(element.getSelfLoopCondition() != null)
			selfLoopCondition.setText(element.getSelfLoopCondition());
		if(element.getmLoop() != null && element.getClosedLoopCondition() != null)
			closedLoopCondition.setText(element.getClosedLoopCondition());
	}
	
	private void saveData() {
		element.setUser(user.getText().toString());
		element.setOperation(operation.getText().toString());
		element.setService(service.getText().toString());
		element.setInputdata(inputdata.getText().toString());
		element.setCondition(condition.getText().toString());
		element.setSelfLoopCondition(selfLoopCondition.getText().toString());
		if(element.getmLoop() != null)
			element.setClosedLoopCondition(closedLoopCondition.getText().toString());
		if(selfLoopCondition.getText().toString() != "") {									//TODO FAIL
			Toast.makeText(getContext(), selfLoopCondition.getText().toString(), Toast.LENGTH_SHORT).show();	
			element.setSelfLoop(true);			
		}
		editorView.redraw();

	}

}
