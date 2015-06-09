package com.example.twitter_network_services;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import nl.saxion.network_services.TwitterApp;
import nl.saxion.network_services.model.Model;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		TwitterApp app = (TwitterApp) getApplicationContext();
		Model model = app.getModel();
		
		model.consumer = new CommonsHttpOAuthConsumer(model.API_KEY,model.API_SECRET);	
		model.provider = new CommonsHttpOAuthProvider(model.OAUTH_REQUEST_URL, model.OAUTH_ACCESTOKEN_URL, model.OAUTH_AUTHORIZE_URL);
		
		Button login = (Button) findViewById(R.id.btnLogin);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(StartActivity.this, AuthorizeActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
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
