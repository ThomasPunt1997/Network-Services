package nl.saxion.network_services;

import nl.saxion.network_services.model.Model;
import android.app.Application;

public class TwitterApp extends Application{
	private Model model;
	
	@Override
	public void onCreate() {
		model = new Model();
	}
	
	public Model getModel(){
		return model;
	}
}
