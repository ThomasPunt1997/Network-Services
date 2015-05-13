package nl.saxion.network_services.objects;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserMention {
	private int begin;
	private int einde;
	
	public UserMention(JSONObject obj){
		try {
			JSONArray ints = obj.getJSONArray("indices");
			begin = ints.getInt(0);
			einde = ints.getInt(1);
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

	public int getEinde() {
		return einde;
	}

	public void setEinde(int einde) {
		this.einde = einde;
	}
}
