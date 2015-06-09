package com.example.twitter_network_services;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import nl.saxion.network_services.TwitterApp;
import nl.saxion.network_services.model.Model;
import android.support.v7.app.ActionBarActivity;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorize);
		
		TwitterApp app = (TwitterApp) getApplicationContext();
		model = app.getModel();
		
		web = (WebView) findViewById(R.id.webView1);
		
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("https://www.dumpert.nl/haharaar")) {
					verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
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
				HttpGet request = new HttpGet("https://api.twitter.com/1.1/account/verify_credentials.json");
				try {
					signInWithUser(request);
				} catch (OAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			
		}
	}
	
	public void signInWithUser(HttpRequestBase request) throws OAuthException {
		model.consumer.sign(request);
	}
}
