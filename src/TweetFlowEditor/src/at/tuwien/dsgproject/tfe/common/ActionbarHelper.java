
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
import android.net.Uri;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.activities.Editor;
import at.tuwien.dsgproject.tfe.activities.Home;
import at.tuwien.dsgproject.tfe.activities.Tweeter;

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
		try {
			Intent i = new Intent( context, Tweeter.class );
			context.startActivity(i); 	
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void login(Context context) {
		try {
			String authUrl = UserManagement.getInstance().login();
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
		} catch (TwitterException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public static void onNewIntent(Intent intent, Context context) {
		Uri uri = intent.getData();
		if(uri != null) {
			try {
				UserManagement.getInstance().loginIntent(uri);
			} catch (TwitterException e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
}
