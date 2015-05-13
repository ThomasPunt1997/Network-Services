package nl.saxion.network_services.objects;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Entities {
	private ArrayList<Hashtag> hashtags = new ArrayList<Hashtag>();
	private ArrayList<Media> medias = new ArrayList<Media>();
	private ArrayList<UserMention> usermentions = new ArrayList<UserMention>();
	
	public Entities(JSONObject obj){
		try {
			JSONArray user = obj.getJSONArray("user_mentions");
			
			for(int i = 0; i < user.length(); i++) {
				JSONObject usermentionObject = user.getJSONObject(i);
				UserMention usermention = new UserMention(usermentionObject);
				usermentions.add(usermention);
			}
			
			
			JSONArray hashtag = obj.getJSONArray("hashtags");
			
			for(int i = 0; i < hashtag.length(); i++) {
				JSONObject hashtagObject = hashtag.getJSONObject(i);
				Hashtag hash = new Hashtag(hashtagObject);
				hashtags.add(hash);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public UserMention getUserMentionFromPosition(int position) {
		return usermentions.get(position);
	}

	public ArrayList<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(ArrayList<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public ArrayList<Media> getMedias() {
		return medias;
	}

	public void setMedias(ArrayList<Media> medias) {
		this.medias = medias;
	}

	public ArrayList<UserMention> getUsermentions() {
		return usermentions;
	}

	public void setUsermentions(ArrayList<UserMention> usermentions) {
		this.usermentions = usermentions;
	}
}
