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
	
	//private final TweetFlow mTweetFlow;
	private final EditText mEditFilename;
	
	private StorageHandler mStorage;

	public SaveTweetflowDialog(Context context, final TweetFlow tweetFlow, final String fileName) {
		super(context);
		setTitle("Save Tweetflow");
		setContentView(R.layout.dialog_save);
		mStorage = new StorageHandler(context);
		
		
		
		//mTweetFlow = tweetFlow;
		mEditFilename = (EditText) findViewById(R.id.save_filename);
		
		if(fileName != null) mEditFilename.setText(fileName);
		
		
		
		((Button) findViewById(R.id.save_dialog_save)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mStorage.write(mEditFilename.getText().toString(), tweetFlow);
				SaveTweetflowDialog.this.cancel();
			}
		});
		
		((Button) findViewById(R.id.save_dialog_cancel)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SaveTweetflowDialog.this.cancel();
			}
		});
	}
	

}
