package nl.saxion.network_services.model;

import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import nl.saxion.network_services.objects.Tweet;
import nl.saxion.network_services.objects.User;

public class Model {
	private ArrayList<Tweet> tweets;
	private ArrayList<Tweet> timelineTweets;
	private ArrayList<User> followersList;
	
	public ArrayList<User> getFollowersList() {
		return followersList;
	}

	public void setFollowersList(ArrayList<User> followersList) {
		this.followersList = followersList;
	}

	public OAuthConsumer consumer;
	public OAuthProvider provider;
	
	public static String API_KEY = "53Lp5N4gNIdsN6YNTSvd0xNsd";
	public static String API_SECRET = "zuCJBCPg9qG59B69T0l2OyYccchPRqhGOoRVArCzuAhilskHzx";
	
	public static String OAUTH_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static String OAUTH_AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	public static String OAUTH_ACCESTOKEN_URL = "https://api.twitter.com/oauth/access_token";
	
	public static String CALLBACK_URL = "https://www.dumpert.nl/haharaar";
	
	private User loggedInUser = null;
	
	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public Model(){
		tweets = new ArrayList<Tweet>();
		timelineTweets = new ArrayList<Tweet>();
		followersList = new ArrayList<User>();
	}
	
	public ArrayList<Tweet> getTimelineTweets() {
		return timelineTweets;
	}

	public void setTimelineTweets(ArrayList<Tweet> timelineTweets) {
		this.timelineTweets = timelineTweets;
	}

	public void addTweet(Tweet tweet) {
		tweets.add(tweet);
	}
	
	public void addTweetToTimeLine(Tweet tweet) {
		timelineTweets.add(tweet);
	}
	
	public Tweet getTweet(int position) {
		return tweets.get(position);
	}
	
	public ArrayList<Tweet> getList() {
		return tweets;
	}
	
	public void setList(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
	}
}
