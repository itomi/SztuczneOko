package pl.pwr.sztuczneoko.core;
import android.content.Intent;
import android.provider.MediaStore;

public class EventCollector implements EventCollectorInterface{

	@Override
	public EnrtyMenuEvents getEntryMenuEvents() {		
			
		return new EnrtyMenuEvents(){

			@Override
			public Intent runPhotoActivity() {
				// TODO Auto-generated method stub
				return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);				
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
