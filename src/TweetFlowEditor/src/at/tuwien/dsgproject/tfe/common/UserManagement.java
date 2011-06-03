
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
import android.content.SharedPreferences;
import android.net.Uri;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class UserManagement {
	private static UserManagement instance;

	private final static String CONSUMER_KEY = "XDzRCbsjZCHD1WPkVoU8g"; 		
	private final static String CONSUMER_SECRET = "PXcaRO74YOZegeQWxen8l2pGZKHHvAbPQvNGqP4Ajk"; 
		
	private static final String CALLBACKURL = "T4JOAuth://main";

	private Twitter twitter;
	private RequestToken requestToken;
	private AccessToken accessToken;
	
	private boolean loggedIn = false;
	
	private String reqToken;
	private String secretToken;
	
	private UserManagement() {};

	public static UserManagement getInstance() {
		if (instance == null) {
			instance = new UserManagement();
		}
		return instance;
	}
	
	public String login() throws TwitterException {
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
		String authUrl = requestToken.getAuthorizationURL();
		return authUrl;
	}
	
	public void loginIntent(Uri uri) throws TwitterException {
		String verifier = uri.getQueryParameter("oauth_verifier");
		accessToken = UserManagement.getInstance().
				getTwitter().getOAuthAccessToken(UserManagement.getInstance().getRequestToken(),verifier);
		reqToken = accessToken.getToken();
		secretToken = accessToken.getTokenSecret();		
		twitter.setOAuthAccessToken(accessToken);
		loggedIn = true;
	}
	
	public void loginAuto(String requestToken, String secretToken) {
		twitter = new TwitterFactory().getInstance();
		accessToken = new AccessToken(requestToken, secretToken);
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(accessToken);
		loggedIn = true;
	}

	public void sendTweeterMessage(String message) throws TwitterException {
		if(twitter!=null) twitter.updateStatus(message);
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	public RequestToken getRequestToken() {
		return requestToken;
	}


	public AccessToken getAccessToken() {
		return accessToken;
	}


	public boolean isLoggedIn() {
		return loggedIn;
	}


	public String getReqToken() {
		return reqToken;
	}


	public String getSecretToken() {
		return secretToken;
	}
	
	public void logout(Context context) {
		loggedIn = false;
		requestToken = null;
		accessToken = null;
		
		reqToken = null;
		secretToken = null;
		
		twitter = null;
		
		SharedPreferences settings = context.getSharedPreferences("TFE", Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.remove("requestToken");
	    editor.remove("secretToken");
	    editor.commit();
		
	}
	
	
	
}
