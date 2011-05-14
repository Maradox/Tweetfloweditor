package at.tuwien.dsgproject.tfe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.StorageHandler;
import at.tuwien.dsgproject.tfe.common.UserManagement;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;

public class LogoutDialog extends Dialog {
	
	public LogoutDialog(Context context) {
		super(context);
		setTitle("You are already logged in");
		setContentView(R.layout.dialog_logout);

		
		((Button) findViewById(R.id.logout_dialog_logout)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UserManagement.getInstance().logout(LogoutDialog.this.getContext());
				LogoutDialog.this.dismiss();
			}
		});
		
		((Button) findViewById(R.id.logout_dialog_cancel)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogoutDialog.this.cancel();
			}
		});
	}
	

}
