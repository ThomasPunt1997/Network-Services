package nl.saxion.network_services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import nl.saxion.network_services.model.Model;
import nl.saxion.network_services.objects.Tweet;
import com.example.twitter_network_services.R;
import android.support.v7.app.ActionBarActivity;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private String wordToSearch = "";
	private EditText searchWord;
	private ListView tweetList;
	private TwitterAdapter adapter;
	private TwitterApp application;
	private Model model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		application = (TwitterApp) getApplicationContext();
		model = application.getModel();
		
		searchWord = (EditText) findViewById(R.id.txtZoekTerm);
		Button searchButton = (Button) findViewById(R.id.btnZoek);
		tweetList = (ListView) findViewById(R.id.listTweet);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				wordToSearch = searchWord.getText().toString();
				searchTwitter task = new searchTwitter();
				task.execute();
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
	
	/**
     * Reads an asset file and returns a string with the full contents.
     *
     * @param filename  The filename of the file to read.
     * @return          The contents of the file.
     * @throws IOException  If file could not be found or not read.
     */
    private String readAssetIntoString(String filename) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
			InputStream is = getAssets().open(filename, AssetManager.ACCESS_BUFFER);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
            throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();		
	}
    
    public String getToken() {
    	String authString = model.API_KEY + ":" + model.API_SECRET;
    	String base64 = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
    	
    	HttpPost request = new HttpPost("https://api.twitter.com/oauth2/token");
    	request.setHeader("Authorization", "Basic " + base64);
    	request.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    	
    	try {
			request.setEntity(new StringEntity("grant_type=client_credentials"));
			
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = client.execute(request, handler);
			
			JSONObject gebrObj = new JSONObject(result);
			String token = gebrObj.getString("access_token");
			Log.d("Token", token);
			return token;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private class searchTwitter extends AsyncTask<Void, Void, String> {

    	@Override
	    protected String doInBackground(Void... urls) {
	    	String token = getToken();
	    	
	    	HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/search/tweets.json?q=" + wordToSearch);	
	    	HttpClient client = new DefaultHttpClient();
	    	ResponseHandler<String> handler = new BasicResponseHandler();
	    	try {
	    		httpGet.setHeader("Authorization", "Bearer " + token);
				String searchJSON = client.execute(httpGet, handler);
				return searchJSON;
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
	    	try {
				model.setList(new ArrayList<Tweet>());
				JSONObject gebrObj = new JSONObject(result);
				JSONArray getTweets = gebrObj.getJSONArray("statuses");
				
				for(int i=0;i < getTweets.length(); i++){
					 JSONObject tweetObject = getTweets.getJSONObject(i);
					 Tweet tweet = new Tweet(tweetObject);
					 model.addTweet(tweet);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} 
			
			ListView tweetList = (ListView) findViewById(R.id.listTweet);
		
			adapter = new TwitterAdapter(MainActivity.this, R.layout.tweetview, model.getList());
			tweetList.setAdapter(adapter);
	    }
	}
}
