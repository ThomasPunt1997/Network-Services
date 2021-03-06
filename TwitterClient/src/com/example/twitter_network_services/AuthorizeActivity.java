package com.example.twitter_network_services;

import java.io.IOException;

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

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import nl.saxion.network_services.ProfileActivity;
import nl.saxion.network_services.TwitterApp;
import nl.saxion.network_services.model.Model;
import nl.saxion.network_services.objects.Tweet;
import nl.saxion.network_services.objects.User;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthorizeActivity extends ActionBarActivity {

	private Model model;
	private WebView web;
	private String verifier;
	private SharedPreferences prefs;
	private Editor edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorize);
		
		prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
		edit = prefs.edit();
		
		TwitterApp app = (TwitterApp) getApplicationContext();
		model = app.getModel();
		
		web = (WebView) findViewById(R.id.webView1);
		
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("https://www.dumpert.nl/haharaar")) {
					verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
					getAccessToken tokenTask = new getAccessToken();
					tokenTask.execute();
					return true;
				}
				return false;
			}
		});
		
		authorizeWebView task = new authorizeWebView();
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authorize, menu);
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
	
	private class authorizeWebView extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				String url = model.provider.retrieveRequestToken(model.consumer, model.CALLBACK_URL);
				Log.d("Url", url);
				return url;
				
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			web.loadUrl(result);
		}
		
	}
	
	private class getAccessToken extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				model.provider.retrieveAccessToken(model.consumer, verifier);
				edit.putString("Token", model.consumer.getToken());
				edit.putString("SecretToken", model.consumer.getTokenSecret());
				edit.commit();
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Intent i = new Intent(AuthorizeActivity.this, ProfileActivity.class);
			startActivity(i);
		}
	}
}
