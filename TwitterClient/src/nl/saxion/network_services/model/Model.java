package nl.saxion.network_services.model;

import java.util.ArrayList;

import nl.saxion.network_services.objects.Tweet;

public class Model {
	private ArrayList<Tweet> tweets;
	
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
}
