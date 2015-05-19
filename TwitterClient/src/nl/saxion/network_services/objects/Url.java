package nl.saxion.network_services.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Url {
	
	private int begin;
	private int eind;
	private String url;
	
	public Url(JSONObject obj) {
		try {
			url = obj.getString("url");
			JSONArray ints = obj.getJSONArray("indices");
			begin = ints.getInt(0);
			eind = ints.getInt(1);
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
