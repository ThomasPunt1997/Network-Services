package nl.saxion.network_services;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.example.twitter_network_services.R;

import nl.saxion.network_services.objects.Hashtag;
import nl.saxion.network_services.objects.Tweet;
import nl.saxion.network_services.objects.UserMention;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TwitterAdapter extends ArrayAdapter<Tweet> {
	
	private LayoutInflater inflater;
	private ImageView profile_Image;
	private Spannable blueColor;

	public TwitterAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
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
		ImageView image_add = (ImageView) convertView.findViewById(R.id.imageView1);
		
		Tweet tweet = getItem(position);
		
		name.setText(tweet.getUser().getName());
		screen_name.setText("@" + tweet.getUser().getScreen_name());
		
		blueColor = new SpannableString(tweet.getText());
		
		if(tweet.getEntitie().getUsermentions().size() > 0) {
			for(UserMention user : tweet.getEntitie().getUsermentions()) {			
				setColorToText(user.getBegin(), user.getEinde());
			}
		}
		
		//hashtags
		if(tweet.getEntitie().getHashtags().size() > 0) {
			for(Hashtag hash : tweet.getEntitie().getHashtags()) {			
				setColorToText(hash.getBegin(), hash.getEinde());
			}
		}
		
		if(blueColor != null) {
			tweet_text.setText(blueColor);
		} else {
			tweet_text.setText(tweet.getText());
		}
		
		new DownloadImageTask(profile_Image).execute(tweet.getUser().getProfile_image_url());
		
		image_add.setVisibility(View.INVISIBLE);
		
		return convertView;
	}
	
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
	
	public void setColorToText(int begin, int end) {
		blueColor.setSpan(new ForegroundColorSpan(Color.BLUE), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
}
