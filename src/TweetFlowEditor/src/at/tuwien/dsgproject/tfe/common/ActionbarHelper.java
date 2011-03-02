package at.tuwien.dsgproject.tfe.common;

import android.content.Context;
import android.content.Intent;
import at.tuwien.dsgproject.tfe.activities.Editor;
import at.tuwien.dsgproject.tfe.activities.Home;

public class ActionbarHelper {
	public static void openHome(Context context) {	
		Intent i = new Intent( context, Home.class );
		context.startActivity(i);  
	}
	
	public static void openEditor(Context context) {
		Intent i = new Intent( context, Editor.class );	
		context.startActivity(i); 
	}
	
	public static void openMyTwitter(Context context) {
		//TODO
	}
	
	public static void login(Context context) {
		//TODO
	}
	
	public static void onNewIntent(Intent intent, Context context) {
		//TODO
	}
}
