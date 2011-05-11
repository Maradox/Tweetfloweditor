
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
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.entities.OpenSequence;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class ChangeDataDialogOpenSequence extends Dialog {
	
	private OpenSequence element; 
	private EditorView editorView;
	private EditText condition;
	private EditText selfLoopCondition;

	public ChangeDataDialogOpenSequence(Context context, EditorView editorView, OpenSequence element) {
		super(context);
		this.editorView = editorView;
		this.element = element;
		this.setContentView(R.layout.dialog_changedata_opensequence);
		setTitle("Edit open sequence");
		
		condition = (EditText) findViewById(R.id.dialog_condition);
		selfLoopCondition = (EditText) findViewById(R.id.dialog_selfloopcondition);
						
		loadData();
		
		((Button) findViewById(R.id.dialog_positiveButton)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveData();
				ChangeDataDialogOpenSequence.this.cancel();
			}
		});
		
		((Button) findViewById(R.id.dialog_negativeButton)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ChangeDataDialogOpenSequence.this.cancel();
			}
		});
	}
	
	private void loadData() {
		if(element.getCondition() != null)
			condition.setText(element.getCondition());
		if(element.getSelfLoopCondition() != null)
			selfLoopCondition.setText(element.getSelfLoopCondition());
	}
	
	private void saveData() {
		element.setCondition(condition.getText().toString());
		element.setSelfLoopCondition(selfLoopCondition.getText().toString());
		if(selfLoopCondition.getText().toString().length() > 0) {			
			element.setSelfLoop(true);			
		} else {
			element.setSelfLoop(false);
		}
			
		editorView.redraw();
	}

}
