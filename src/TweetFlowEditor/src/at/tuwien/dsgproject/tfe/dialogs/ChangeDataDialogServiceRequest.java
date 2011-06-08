
/* 
 *  Tweetfloweditor - a graphical editor to create Tweetflows
 *  
 *  Copyright (C) 2011  Matthias Neumayr
 *  Copyright (C) 2011  Martin Perebner
 *  
 *  Tweetfloweditor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Tweetfloweditor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Tweetfloweditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tuwien.dsgproject.tfe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.entities.ServiceRequest;
import at.tuwien.dsgproject.tfe.views.EditorView;


/**
 * ChangeDataDialogServiceRequest
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * A dialog to edit all parameters of service requests
 */
public class ChangeDataDialogServiceRequest extends Dialog {
	
	private ServiceRequest element; 
	private EditorView editorView;
	
	private EditText user;
	private EditText operation;
	private EditText service;
	private EditText inputdata;
	private EditText condition;
	
	private EditText selfLoopCondition;
	private EditText closedLoopCondition;
	
	private TableRow rowClosedLoopCondition;

	public ChangeDataDialogServiceRequest(Context context, EditorView editorView, ServiceRequest element) {
		super(context);
		this.editorView = editorView;
		this.element = element;
		this.setContentView(R.layout.dialog_changedata_servicerequest);
		setTitle("Edit service request");
		
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
				ChangeDataDialogServiceRequest.this.cancel();
			}
		});
		
		((Button) findViewById(R.id.dialog_negativeButton)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ChangeDataDialogServiceRequest.this.cancel();
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
		if(selfLoopCondition.getText().toString().length() > 0) {			
			element.setSelfLoop(true);			
		} else {
			element.setSelfLoop(false);
		}
			
		editorView.redraw();

	}

}
