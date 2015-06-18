package nl.saxion.network_services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nl.saxion.network_services.model.Model;
import nl.saxion.network_services.objects.Tweet;
import nl.saxion.network_services.objects.User;

import com.example.twitter_network_services.AuthorizeActivity;
import com.example.twitter_network_services.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends ActionBarActivity {
	
	private Model model;
	private TextView characters;
	private EditText sendTweet;
	private ListView timeline;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		
		TwitterApp app = (TwitterApp) getApplicationContext();
		model = app.getModel();
		
		User user = model.getLoggedInUser();
		
		TextView userName = (TextView) findViewById(R.id.lblName);
		TextView screenName = (TextView) findViewById(R.id.lblUserName);
		TextView description = (TextView) findViewById(R.id.lblDescription);
		TextView followers = (TextView) findViewById(R.id.lblFollowersCount);
		TextView friends = (TextView) findViewById(R.id.lblFriendsCount);
		TextView favorites = (TextView) findViewById(R.id.lblFavoritesCount);
		timeline = (ListView) findViewById(R.id.listView1);
		characters = (TextView) findViewById(R.id.lblCharacters);
		sendTweet = (EditText) findViewById(R.id.txtTweetText);
		Button buttonSend = (Button) findViewById(R.id.btnSendTweet);
		
		getTimeLine task = new getTimeLine();
		task.execute();
		
		userName.setText(user.getName());
		Log.d("Screen_name", user.getScreen_name());
		screenName.setText("@" + user.getScreen_name());
		description.setText(user.getDescription());
		followers.setText("" + user.getFollowers());
		friends.setText("" + user.getFriends());
		favorites.setText("" + user.getFavorites());
		
		sendTweet.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				characters.setText("" + sendTweet.length() + "/140");
				return false;
			}
		});
		
		buttonSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendTweetTask tweetTask = new sendTweetTask();
				tweetTask.execute();
				
			}
		});
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
	
	public void getTimeLine (HttpRequestBase request) throws OAuthException, ClientProtocolException, IOException {
		model.consumer.sign(request);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse userJson = httpClient.execute(request);
		String json_string = EntityUtils.toString(userJson.getEntity());
		JSONArray timeline = null;
		try {
			timeline = new JSONArray(json_string);
			
			for(int i=0;i < timeline.length(); i++){
				 JSONObject tweetObject = timeline.getJSONObject(i);
				 Tweet tweet = new Tweet(tweetObject);
				 model.addTweetToTimeLine(tweet);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class getTimeLine extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			HttpGet timelineRequest = new HttpGet("https://api.twitter.com/1.1/statuses/home_timeline.json");
			try {
				getTimeLine(timelineRequest);
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
			TwitterAdapter adapter = new TwitterAdapter(ProfileActivity.this, R.layout.tweetview, model.getTimelineTweets());
			timeline.setAdapter(adapter);
		}
	}
	
	private class sendTweetTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				String tweetText = URLEncoder.encode(sendTweet.getText().toString(),"UTF-8");
				HttpPost request = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?status=" + tweetText);
				model.consumer.sign(request);
				
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				client.execute(request, handler);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
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
			
		}
	}
}
