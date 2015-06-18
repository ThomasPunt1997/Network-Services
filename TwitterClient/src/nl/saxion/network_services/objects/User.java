package nl.saxion.network_services.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String id;
	private String created_at;
	private String name;
	private String screen_name;
	private String profile_image_url;
	private String description;
	private int followers;
	private int favorites;
	private int friends;
	
	public User(JSONObject obj){
		try {
			created_at = obj.getString("created_at");
			id = obj.getString("id_str");
			name = obj.getString("name");
			screen_name = obj.getString("screen_name");
			profile_image_url = obj.getString("profile_image_url");
			description = obj.getString("description");
			followers = obj.getInt("followers_count");
			favorites = obj.getInt("favorites_count");
			friends = obj.getInt("friends_count");
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFavorites() {
		return favorites;
	}

	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}

	public int getFriends() {
		return friends;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
}
