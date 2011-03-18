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

package at.tuwien.dsgproject.tfe.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.UserManagement;

public class Home extends ActionbarActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        SharedPreferences settings = getPreferences(0);
        String requestToken = settings.getString("requestToken", "");
        String secretToken = settings.getString("secretToken", "");
        
        if((requestToken.length() > 0) && (secretToken.length() > 0)) {
        	UserManagement.getInstance().loginAuto(requestToken,secretToken);
        }
    }	
	
}