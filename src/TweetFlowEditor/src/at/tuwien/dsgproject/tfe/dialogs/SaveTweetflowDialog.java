package at.tuwien.dsgproject.tfe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.StorageHandler;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;

public class SaveTweetflowDialog extends Dialog {
	
	private final EditText mEditFilename;
	
	private StorageHandler mStorage;

	public SaveTweetflowDialog(Context context, final TweetFlow tweetFlow) {
		super(context);
		setTitle("Save Tweetflow");
		setContentView(R.layout.dialog_save);
		mStorage = new StorageHandler(context);
		
		mEditFilename = (EditText) findViewById(R.id.save_filename);
		mEditFilename.setText(tweetFlow.getName());
		
		((Button) findViewById(R.id.save_dialog_save)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String name = mEditFilename.getText().toString();
				tweetFlow.setName(name);
				mStorage.write(name, tweetFlow);
				SaveTweetflowDialog.this.dismiss();
			}
		});
		
		((Button) findViewById(R.id.save_dialog_cancel)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SaveTweetflowDialog.this.cancel();
			}
		});
	}
	

}
