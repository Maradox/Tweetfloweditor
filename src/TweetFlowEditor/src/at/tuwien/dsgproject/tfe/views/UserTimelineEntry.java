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
