package nl.saxion.network_services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends ActionBarActivity {
	
	private Model model;
	private TextView characters;
	private EditText sendTweet;
	private ListView timeline;
	private TextView userName;
	private TextView screenName;
	private TextView description;
	private TextView followers;
	private TextView friends;
	private TextView favorites;
	private ImageView profileImage;
	private Button sendLocationTweet;
	private double longitude;
	private double latitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		
		TwitterApp app = (TwitterApp) getApplicationContext();
		model = app.getModel();
		
		userName = (TextView) findViewById(R.id.lblName);
		screenName = (TextView) findViewById(R.id.lblUserName);
		description = (TextView) findViewById(R.id.lblDescription);
		followers = (TextView) findViewById(R.id.lblFollowersCount);
		friends = (TextView) findViewById(R.id.lblFriendsCount);
		favorites = (TextView) findViewById(R.id.lblFavoritesCount);
		timeline = (ListView) findViewById(R.id.listView1);
		characters = (TextView) findViewById(R.id.lblCharacters);
		sendTweet = (EditText) findViewById(R.id.txtTweetText);
		profileImage = (ImageView) findViewById(R.id.imgProfileImage);
		Button buttonSend = (Button) findViewById(R.id.btnSendTweet);
		sendLocationTweet = (Button) findViewById(R.id.btnSendLocation);
		
		getTimeLine task = new getTimeLine();
		task.execute();
		
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
				sendTweetTask tweetTask = new sendTweetTask(sendTweet.getText().toString());
				tweetTask.execute();
				
			}
		});
		
		sendLocationTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				
				if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					buildAlertMessageNoGps();
				} else {
					LocationListener locationListener = new MyLocationListener();
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
				}
				
			}
		});
		
		followers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this, FollowersActivity.class);
				startActivity(i);
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
			HttpGet request = new HttpGet("https://api.twitter.com/1.1/account/verify_credentials.json");
			try {
				signInWithUser(request);
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
			User user = model.getLoggedInUser();
			
			userName.setText(user.getName());
			Log.d("Screen_name", user.getScreen_name());
			screenName.setText("@" + user.getScreen_name());
			description.setText(user.getDescription());
			followers.setText("" + user.getFollowers());
			friends.setText("" + user.getFriends());
			favorites.setText("" + user.getFavorites());
			new DownloadImageTask(profileImage).execute(user.getProfile_image_url());
			
			TwitterAdapter adapter = new TwitterAdapter(ProfileActivity.this, R.layout.tweetview, model.getTimelineTweets());
			timeline.setAdapter(adapter);
		}
	}
	
	private class sendTweetTask extends AsyncTask<String, Void, String> {

		private String text;
		public sendTweetTask(String string) {
			text = string;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String tweetText = URLEncoder.encode(text,"UTF-8");
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
	
	public void signInWithUser(HttpRequestBase request) throws OAuthException, ClientProtocolException, IOException {
		model.consumer.sign(request);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse userJson = httpClient.execute(request);
		String json_string = EntityUtils.toString(userJson.getEntity());
		JSONObject user = null;
		try {
			user = new JSONObject(json_string);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User loggedUser = new User(user);
		model.setLoggedInUser(loggedUser);
	}
	
	/**
	 * Converts an URL into an image and shows it in your ImageView.
	 * @author Thomas & Wouter
	 *
	 */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	
	private void buildAlertMessageNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
	
	private class MyLocationListener implements LocationListener {

	    @Override
	    public void onProviderDisabled(String provider) {}

	    @Override
	    public void onProviderEnabled(String provider) {}

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {}

		@Override
		public void onLocationChanged(Location location) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			getLocation task = new getLocation();
			task.execute();
		}
	}
	
	private class getLocation extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpGet request = new HttpGet("https://api.twitter.com/1.1/geo/reverse_geocode.json?lat=" + latitude + "&long=" + longitude);
				String locatie = getLocationJson(request);
				return locatie;
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
			sendTweet.setText(result);
		}
	}
	
	public String getLocationJson(HttpRequestBase request) throws OAuthException, ClientProtocolException, IOException {
		model.consumer.sign(request);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse userJson = httpClient.execute(request);
		String json_string = EntityUtils.toString(userJson.getEntity());
		JSONObject location = null;
		try {
			location = new JSONObject(json_string);
			JSONObject result = location.getJSONObject("result");
			JSONArray places = result.getJSONArray("places");
			
			Log.d("Json length", "" + places.length());
			
			JSONObject place = places.getJSONObject(0);
			String country = place.getString("country");
			String fullname = place.getString("full_name");
			Log.d("Results Json", country + " " + fullname);
			
			String locatie = "Ik bevind mij nu in: " + country + " " + fullname;
			return locatie;
			
		} catch (JSONException e) {
			
		}
		return null;
	}
}
