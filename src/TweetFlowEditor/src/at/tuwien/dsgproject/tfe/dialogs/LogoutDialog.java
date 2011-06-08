package at.tuwien.dsgproject.tfe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.UserManagement;


/**
 * LogoutDialog
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * A dialog logout the current twitter user
 */
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
