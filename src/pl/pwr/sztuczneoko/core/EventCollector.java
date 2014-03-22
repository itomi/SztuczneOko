package pl.pwr.sztuczneoko.core;
import pl.pwr.sztuczneoko.communication.Communication;
import pl.pwr.sztuczneoko.imageProcessor.ImageProcessor;
import pl.pwr.sztuczneoko.ui.CameraActivity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

public class EventCollector implements EventCollectorInterface{

	private Communication comm;
	private ImageProcessor imgProc;
	
	@Override
	public void setCurrentImg(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnrtyMenuEvents getEntryMenuEvents() {		
			
		return new EnrtyMenuEvents(){

			@Override
			public Intent runPhotoActivity(Context c) {
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
