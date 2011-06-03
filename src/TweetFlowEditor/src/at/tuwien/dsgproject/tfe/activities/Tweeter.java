package at.tuwien.dsgproject.tfe.activities;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.UserManagement;
import at.tuwien.dsgproject.tfe.views.UserTimelineEntry;
import at.tuwien.dsgproject.tfe.views.UserTimelineTop;

public class Tweeter extends ActionbarListviewActivity {
	
	private TweetAdapter adapter;
	private UserTimelineTop userTimelineTop;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        //getListView().setDivider(null);

        userTimelineTop = new UserTimelineTop(this);
        getListView().addHeaderView(userTimelineTop);
        
		adapter = new TweetAdapter(this, new ArrayList<Status>());
		setListAdapter(adapter);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	if(UserManagement.getInstance().isLoggedIn()) {
    		updateData();
    	}
    }
    
    public void updateData() {
    	try {
			List<Status> userTimeline = UserManagement.getInstance().getTwitter().getUserTimeline();
			adapter.setUserTimeline(userTimeline);
			adapter.notifyDataSetChanged();
		} catch (TwitterException e) {
			Log.e("TFE-Tweeter", e.getMessage());
		}
    }

    private class TweetAdapter extends BaseAdapter {
		private Context context;
		private List<Status> userTimeline;

		private TweetAdapter(Context context, List<Status> userTimeline) {
			this.context = context;
			this.userTimeline = userTimeline;
		}

		public int getCount() {
			return userTimeline.size();
		}

		public Object getItem(int position) {
			return userTimeline.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			UserTimelineEntry entry = (UserTimelineEntry) convertView;

			Status status = userTimeline.get(position);
			status.getUser().getProfileImageURL();
			if (entry == null) {
				entry = new UserTimelineEntry(status.getUser().getProfileImageURL(),status.getUser().getName(),status.getText(),status.getCreatedAt(),context);
			}
			return entry;
		}

		public void setUserTimeline(List<Status> userTimeline) {
			this.userTimeline = userTimeline;
		}
	}
}