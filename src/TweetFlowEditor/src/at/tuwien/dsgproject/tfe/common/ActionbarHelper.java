
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

package at.tuwien.dsgproject.tfe.common;

import twitter4j.TwitterException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.activities.Editor;
import at.tuwien.dsgproject.tfe.activities.Home;
import at.tuwien.dsgproject.tfe.activities.Tweeter;
import at.tuwien.dsgproject.tfe.dialogs.LogoutDialog;

public class ActionbarHelper {
	public static void openHome(Context context) {	
		Intent i = new Intent( context, Home.class );
		context.startActivity(i);  
	}
	
	public static void openEditor(Context context) {
		Intent i = new Intent( context, Editor.class );	
		i.putExtra(Editor.OPEN_NEW, true);
		context.startActivity(i); 
	}
	
	public static void openMyTwitter(Context context) {
		if(UserManagement.getInstance().isLoggedIn()) {
			try {
				Intent i = new Intent( context, Tweeter.class );
				context.startActivity(i); 	
			} catch (IllegalStateException e) {
				Log.e("TFE", e.getMessage());
			} 
		} else {
			Toast.makeText(context, "You have to login first!", Toast.LENGTH_LONG).show();
		}
	}
	
	public static void login(Context context) {
    	if(!UserManagement.getInstance().isLoggedIn()) {
    		try {
    			String authUrl = UserManagement.getInstance().login();
    			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
    		} catch (TwitterException e) {
    			Toast.makeText(context, "Error while logging in.", Toast.LENGTH_LONG).show();
    		}
    	} else {
			LogoutDialog dialog = new LogoutDialog(context);
			dialog.show();
		}
		
	}
	
	public static void onNewIntent(Intent intent, Context context) {
		Uri uri = intent.getData();
		if(uri != null) {
			try {
				UserManagement.getInstance().loginIntent(uri);
			} catch (TwitterException e) {
				Log.e("TFE", e.getMessage());
			}
		}

		SharedPreferences settings = context.getSharedPreferences("TFE", Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("requestToken", UserManagement.getInstance().getReqToken());
	    editor.putString("secretToken", UserManagement.getInstance().getSecretToken());
	    editor.commit();
	}
}
