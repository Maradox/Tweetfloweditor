
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

import twitter4j.TwitterException;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.activities.Tweeter;
import at.tuwien.dsgproject.tfe.common.UserManagement;

public class UserTimelineTop extends RelativeLayout {
	
	public UserTimelineTop(final Context context) {
		super(context);
		
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.usertimelinetop, this);

		((TextView) findViewById(R.id.userTimelineAddMessage)).setOnClickListener(new OnClickListener() {	//TODO
			public void onClick(View arg0) {
				try {
					UserManagement.getInstance().sendTweeterMessage("sendTweeterMessage works at: "+ System.currentTimeMillis());
					((Tweeter) context).updateData();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
	}
	
}
