package at.tuwien.dsgproject.tfe.common;

import android.net.Uri;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class UserManagement {
	private static UserManagement instance;
	private final static String consumerKey = ""; //TODO		
	private final static String consumerSecret = ""; //TODO

		
	private final String CALLBACKURL = "T4JOAuth://main";
	private Twitter twitter;
	private RequestToken requestToken;
	private AccessToken accessToken;
	
	private boolean loggedIn = false;
	
	private UserManagement() {};

	public static UserManagement getInstance() {
		if (instance == null) {
			instance = new UserManagement();
		}
		return instance;
	}
	
	public String login() throws TwitterException {
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(UserManagement.getInstance().getConsumerkey(), UserManagement.getInstance().getConsumersecret());
		requestToken = twitter.getOAuthRequestToken(UserManagement.getInstance().getCALLBACKURL());
		String authUrl = requestToken.getAuthenticationURL();
		return authUrl;
	}
	
	@SuppressWarnings("deprecation")
	public void loginIntent(Uri uri) throws TwitterException {
		String verifier = uri.getQueryParameter("oauth_verifier");
		accessToken = UserManagement.getInstance().getTwitter().getOAuthAccessToken(UserManagement.getInstance().getRequestToken(),verifier);
		String token = accessToken.getToken();
		String secret = accessToken.getTokenSecret();		
		twitter.setOAuthAccessToken(token, secret);
		loggedIn = true;
	}

	public void sendTweeterMessage(String message) throws TwitterException {
		twitter.updateStatus(message);
	}
	
	public String getConsumerkey() {
		return consumerKey;
	}

	public String getConsumersecret() {
		return consumerSecret;
	}

	public String getCALLBACKURL() {
		return CALLBACKURL;
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

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	
	
}
