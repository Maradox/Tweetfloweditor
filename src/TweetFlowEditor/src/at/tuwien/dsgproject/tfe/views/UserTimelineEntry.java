
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

import java.net.URL;
import java.util.Date;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.UtilHelper;

public class UserTimelineEntry extends RelativeLayout {
	
	public UserTimelineEntry(URL imageUrl, String owner,String message, Date date, Context context) {
		super(context);
		
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.usertimelineentry, this);
		((TextView) findViewById(R.id.statusContributor)).setText(owner+":");
		((TextView) findViewById(R.id.statusMessage)).setText(message);
		((TextView) findViewById(R.id.statusTime)).setText(DateUtils.getRelativeTimeSpanString(date.getTime()));
		
		ImageView imgView = (ImageView)findViewById(R.id.statusImage);
		imgView.setImageDrawable(UtilHelper.getImageFromUrl(imageUrl,owner));
	}
}
