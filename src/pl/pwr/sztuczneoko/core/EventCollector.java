package pl.pwr.sztuczneoko.core;
import pl.pwr.sztuczneoko.ui.CameraActivity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

public class EventCollector implements EventCollectorInterface{

	@Override
	public EnrtyMenuEvents getEntryMenuEvents() {		
			
		return new EnrtyMenuEvents(){

			@Override
			public Intent runPhotoActivity(Context c) {
				// TODO Auto-generated method stub
				return new Intent(c,CameraActivity.class);				
			}

			@Override
			public void runBrowsePhotoActivity() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void runPreferenceActivity() {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

}
