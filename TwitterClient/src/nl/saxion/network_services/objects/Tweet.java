package nl.saxion.network_services.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private String id;
	private String text;
	private String created_at;
	private int favorite_count;
	private int retweet_count;
	private User user;
	private Entities entitie;
	private Media media;
	
	public Tweet(JSONObject obj){
		try {
			text = obj.getString("text");
			created_at = obj.getString("created_at");
			id = obj.getString("id_str");
			favorite_count = obj.getInt("favorite_count");
			retweet_count = obj.getInt("retweet_count");
			user = new User(obj.getJSONObject("user"));
			entitie = new Entities(obj.getJSONObject("entities"));
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getFavorite_count() {
		return favorite_count;
	}

	public void setFavorite_count(int favorite_count) {
		this.favorite_count = favorite_count;
	}

	public int getRetweet_count() {
		return retweet_count;
	}

	public void setRetweet_count(int retweet_count) {
		this.retweet_count = retweet_count;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Entities getEntitie() {
		return entitie;
	}

	public void setEntitie(Entities entitie) {
		this.entitie = entitie;
	}
}
