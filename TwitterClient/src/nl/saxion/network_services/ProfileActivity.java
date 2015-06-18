package nl.saxion.network_services;

import nl.saxion.network_services.model.Model;
import nl.saxion.network_services.objects.User;

import com.example.twitter_network_services.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends ActionBarActivity {
	
	private Model model;
	
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
		ListView timeline = (ListView) findViewById(R.id.listView1);
		
		userName.setText(user.getName());
		Log.d("Screen_name", user.getScreen_name());
		screenName.setText("@" + user.getScreen_name());
		description.setText(user.getDescription());
		followers.setText("" + user.getFollowers());
		friends.setText("" + user.getFriends());
		favorites.setText("" + user.getFavorites());
		
		TwitterAdapter adapter = new TwitterAdapter(ProfileActivity.this, R.layout.tweetview, model.getTimelineTweets());
		timeline.setAdapter(adapter);
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

}
