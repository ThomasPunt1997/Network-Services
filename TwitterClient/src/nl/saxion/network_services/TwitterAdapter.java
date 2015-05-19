package nl.saxion.network_services;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.example.twitter_network_services.R;

import nl.saxion.network_services.objects.Hashtag;
import nl.saxion.network_services.objects.Media;
import nl.saxion.network_services.objects.Tweet;
import nl.saxion.network_services.objects.Url;
import nl.saxion.network_services.objects.UserMention;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TwitterAdapter extends ArrayAdapter<Tweet> {
	
	private LayoutInflater inflater;
	private ImageView profile_Image;
	private ImageView image_add;
	private Spannable blueColor;
	private Context context;

	public TwitterAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.tweetview, parent, false);
		}
		
		profile_Image = (ImageView) convertView.findViewById(R.id.imgProfilePhoto);
		TextView name = (TextView) convertView.findViewById(R.id.lblName);
		TextView screen_name = (TextView) convertView.findViewById(R.id.lblScreenName);
		TextView tweet_text = (TextView) convertView.findViewById(R.id.lblTweetText);
		image_add = (ImageView) convertView.findViewById(R.id.imageView1);
		
		Tweet tweet = getItem(position);
		
		name.setText(tweet.getUser().getName());
		screen_name.setText("@" + tweet.getUser().getScreen_name());
		
		blueColor = new SpannableString(tweet.getText());
		
		//Usermentions
		if(tweet.getEntitie().getUsermentions().size() > 0) {
			for(UserMention user : tweet.getEntitie().getUsermentions()) {			
				setColorToText(user.getBegin(), user.getEinde());
			}
		}
		
		//Hashtags
		if(tweet.getEntitie().getHashtags().size() > 0) {
			for(Hashtag hash : tweet.getEntitie().getHashtags()) {			
				setColorToText(hash.getBegin(), hash.getEinde());
			}
		}
		
		//Media's
		if(tweet.getEntitie().getMedias().size() > 0) {
			for(Media med : tweet.getEntitie().getMedias()) {			
				setColorToText(med.getBegin(), med.getEind());
			}
		}
		
		//Url
		if(tweet.getEntitie().getUrls().size() > 0) {
			for(Url url : tweet.getEntitie().getUrls()) {	
				blueColor.setSpan(new URLSpan(url.getUrl()), url.getBegin(), url.getEind(), 0);
			}
		}
		
		//Checks if there is any text that has to be blue.
		if(blueColor != null) {
			tweet_text.setText(blueColor);
		} else {
			tweet_text.setText(tweet.getText());
		}
		
		new DownloadImageTask(profile_Image).execute(tweet.getUser().getProfile_image_url());
		
		if(tweet.getEntitie().getMedias().size() > 0) {
			new DownloadImageTask(image_add).execute(tweet.getEntitie().getMedias().get(0).getMedia_url());
		} else {
			image_add.setVisibility(View.INVISIBLE);
		}
		
		tweet_text.setLinksClickable(true);
		tweet_text.setMovementMethod(LinkMovementMethod.getInstance());
		
		return convertView;
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
	
	/**
	 * Makes part of the text blue.
	 * @param begin the startposition
	 * @param end the endposition
	 */
	public void setColorToText(int begin, int end) {
		blueColor.setSpan(new ForegroundColorSpan(Color.BLUE), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
}
