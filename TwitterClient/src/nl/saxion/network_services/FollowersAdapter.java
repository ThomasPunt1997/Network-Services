package nl.saxion.network_services;

import java.io.InputStream;
import java.util.List;
import com.example.twitter_network_services.R;
import nl.saxion.network_services.objects.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FollowersAdapter extends ArrayAdapter<User>{

	private LayoutInflater inflater;
	private User follower;
	
	public FollowersAdapter(Context context, int resource, List<User> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.follower_item, parent, false);
		}
		
		ImageView profile = (ImageView) convertView.findViewById(R.id.imgFollower);
		TextView name = (TextView) convertView.findViewById(R.id.lblFollowerName);
		TextView screenname = (TextView) convertView.findViewById(R.id.lblFollowerScreen);
		
		follower = getItem(position);
		
		new DownloadImageTask(profile).execute(follower.getProfile_image_url());
		name.setText(follower.getName());
		screenname.setText("@" + follower.getScreen_name());
		
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
}
