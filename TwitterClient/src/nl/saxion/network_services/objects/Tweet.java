package nl.saxion.network_services.objects;

import org.json.JSONObject;

public class Tweet {
	private String id;
	private String text;
	private String created_at;
	private int favorite_count;
	private int retweet_count;
	private User user;
	private Entities entitie;
	
	public Tweet(JSONObject obj){
		
	}
}
