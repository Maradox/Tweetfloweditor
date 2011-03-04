
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
