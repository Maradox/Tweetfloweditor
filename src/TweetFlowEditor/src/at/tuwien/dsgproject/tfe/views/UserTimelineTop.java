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
