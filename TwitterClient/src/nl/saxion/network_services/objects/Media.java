package nl.saxion.network_services.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Media {
	private int begin;
	private int eind;
	private String media_url;
	
	public Media(JSONObject obj){
		try {
			JSONArray ints = obj.getJSONArray("indices");
			begin = ints.getInt(0);
			eind = ints.getInt(1);
			media_url = obj.getString("media_url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEind() {
		return eind;
	}

	public void setEind(int eind) {
		this.eind = eind;
	}

	public String getMedia_url() {
		return media_url;
	}

	public void setMedia_url(String media_url) {
		this.media_url = media_url;
	}
}
