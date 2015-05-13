package nl.saxion.network_services;

import java.util.List;

import com.example.twitter_network_services.R;

import nl.saxion.network_services.objects.Tweet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TwitterAdapter extends ArrayAdapter<Tweet> {
	
	private LayoutInflater inflater;

	public TwitterAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.tweetview, parent, false);
		}
		
		ImageView profile_Image = (ImageView) convertView.findViewById(R.id.imgProfilePhoto);
		TextView name = (TextView) convertView.findViewById(R.id.lblName);
		TextView screen_name = (TextView) convertView.findViewById(R.id.lblScreenName);
		TextView tweet_text = (TextView) convertView.findViewById(R.id.lblTweetText);
		ImageView image_add = (ImageView) convertView.findViewById(R.id.imageView1);
		
		Tweet tweet = getItem(position);
		
		tweet_text.setText(tweet.getText());
		
		return convertView;
	}
}
