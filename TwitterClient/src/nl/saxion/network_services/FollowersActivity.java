package nl.saxion.network_services;

import java.io.IOException;
import java.util.ArrayList;
import nl.saxion.network_services.model.Model;
import nl.saxion.network_services.objects.User;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.twitter_network_services.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class FollowersActivity extends ActionBarActivity {

	private Model model;
	private ListView followersList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.followers_list);
		
		TwitterApp app = (TwitterApp) getApplicationContext();
		model = app.getModel();
		
		followersList = (ListView) findViewById(R.id.listFollowers);
		
		getFollowersTask task = new getFollowersTask();
		task.execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void getFollowers(HttpRequestBase request) throws OAuthException, ClientProtocolException, IOException {
		model.consumer.sign(request);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse userJson = httpClient.execute(request);
		String json_string = EntityUtils.toString(userJson.getEntity());
		try {
			JSONObject follower = new JSONObject(json_string);
			JSONArray followerArray = follower.getJSONArray("users");
			
			ArrayList<User> followers = new ArrayList<User>();
			for(int i = 0; i < followerArray.length(); i++) {
				JSONObject follow = followerArray.getJSONObject(i);
				User user = new User(follow);
				followers.add(user);
			}
			
			model.setFollowersList(followers);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class getFollowersTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			HttpGet request = new HttpGet("https://api.twitter.com/1.1/followers/list.json?screen_name=" + model.getLoggedInUser().getScreen_name() + "&count=" + model.getLoggedInUser().getFollowers());
			try {
				getFollowers(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			FollowersAdapter adapter = new FollowersAdapter(FollowersActivity.this, R.layout.follower_item, model.getFollowersList());
			followersList.setAdapter(adapter);
		}
	}
}
