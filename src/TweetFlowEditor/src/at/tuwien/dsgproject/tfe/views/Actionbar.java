package at.tuwien.dsgproject.tfe.views;

import android.content.Intent;
import android.view.View;

public interface Actionbar {
	/**
	 * Called when the Home button in the Action Bar is pressed
	 * 
	 * @param v
	 */
	public void openHome(View v);
	
	/**
	 * Called when the editor button in the Action Bar is pressed
	 * 
	 * @param v
	 */
	public void openEditor(View v);

	/**
	 * Called when the tweeter button in the Action Bar is pressed
	 * 
	 * @param v
	 */
	public void openMyTwitter(View v);

	/**
	 * Called when the Chat button in the Action Bar is pressed
	 * 
	 * @param v
	 */
	public void login(View v);
	
	public void onNewIntent(Intent intent);
	
}
