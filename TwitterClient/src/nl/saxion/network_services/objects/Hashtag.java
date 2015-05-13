package nl.saxion.network_services.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Hashtag {
	private int begin;
	private int einde;
	
	public Hashtag(JSONObject obj){
		try {
			JSONArray ints = obj.getJSONArray("indices");
			begin = ints.getInt(0);
			einde = ints.getInt(1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getBegin(){
		return begin;
	}
	
	public int getEinde(){
		return einde;
	}
}
