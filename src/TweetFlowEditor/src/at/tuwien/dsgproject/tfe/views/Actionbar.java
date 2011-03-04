
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
