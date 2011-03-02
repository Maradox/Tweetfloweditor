package at.tuwien.dsgproject.tfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import at.tuwien.dsgproject.tfe.common.ActionbarHelper;
import at.tuwien.dsgproject.tfe.views.Actionbar;

public abstract class ActionbarActivity extends Activity implements Actionbar {

	public void openHome(View v) {	
		ActionbarHelper.openHome(this);
	}
	
	public void openEditor(View v) {	
		ActionbarHelper.openEditor(this);
	}
	
	public void openMyTwitter(View v) {	
		ActionbarHelper.openMyTwitter(this);
	}

	public void login(View v) {
		ActionbarHelper.login(this);
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		ActionbarHelper.onNewIntent(intent, this);
	}
}