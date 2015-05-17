package nl.saxion.network_services.objects;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Entities {
	private ArrayList<Hashtag> hashtags = new ArrayList<Hashtag>();
	private ArrayList<Media> medias = new ArrayList<Media>();
	private ArrayList<UserMention> usermentions = new ArrayList<UserMention>();
	private ArrayList<Url> urls = new ArrayList<Url>();
	
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
			
			JSONArray mediaArray = obj.getJSONArray("media");
			
			for(int i = 0; i < mediaArray.length(); i++) {
				JSONObject mediaObject = mediaArray.getJSONObject(i);
				Media media = new Media(mediaObject);
				medias.add(media);
			}
			
			JSONArray urlArray = obj.getJSONArray("urls");
			
			for(int i = 0; i < urlArray.length(); i++) {
				JSONObject urlObject = urlArray.getJSONObject(i);
				Url url = new Url(urlObject);
				urls.add(url);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Url> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<Url> urls) {
		this.urls = urls;
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
