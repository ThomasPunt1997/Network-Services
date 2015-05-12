package nl.saxion.network_services.objects;

import java.util.ArrayList;

import org.json.JSONObject;

public class Entities {
	private ArrayList<Hashtag> hashtags = new ArrayList<Hashtag>();
	private ArrayList<Media> medias = new ArrayList<Media>();
	private ArrayList<UserMention> usermentions = new ArrayList<UserMention>();
	
	public Entities(JSONObject obj){
		
	}
}
