package nl.saxion.network_services.model;

import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import nl.saxion.network_services.objects.Tweet;

public class Model {
	private ArrayList<Tweet> tweets;
	
	public OAuthConsumer consumer;
	public OAuthProvider provider;
	
	public static String API_KEY = "53Lp5N4gNIdsN6YNTSvd0xNsd";
	public static String API_SECRET = "zuCJBCPg9qG59B69T0l2OyYccchPRqhGOoRVArCzuAhilskHzx";
	
	public static String OAUTH_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static String OAUTH_AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	public static String OAUTH_ACCESTOKEN_URL = "https://api.twitter.com/oauth/access_token";
	
	public static String CALLBACK_URL = "https://www.dumpert.nl/haharaar";
	
	public Model(){
		tweets = new ArrayList<Tweet>();
	}
	
	public void addTweet(Tweet tweet) {
		tweets.add(tweet);
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
