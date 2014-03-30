package pl.pwr.sztuczneoko.core;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import pl.pwr.sztuczneoko.communication.Communication;
import pl.pwr.sztuczneoko.imageProcessor.ImageProcessor;
import pl.pwr.sztuczneoko.ui.*;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

public class EventCollector implements EventCollectorInterface{

	private Communication comm;
	private ImageProcessor imgProc;
	private ArrayList<ExternDevice> edList;
	private ArrayList<Property> camPropList = new ArrayList<Property>();
	private ArrayList<Property> filterPropList = new ArrayList<Property>();
	
	@Override
	public void setCurrentImg(byte[] data) {
		// TODO Auto-generated method stub		
	}

	public EventCollector() {
		camPropList.add(new Property("faceDetect", false));
		camPropList.add(new Property("voiceDescription", false));
		camPropList.add(new Property("realTime", false));
		filterPropList.add(new Property("autoFilter", true));
		camPropList.add(new Property("flash",false));
	}
	
	@Override
	public EnrtyMenuEvents getEntryMenuEvents() {		
			
		return new EnrtyMenuEvents(){

			@Override
			public Intent runPhotoActivity(Context c) {
				return new Intent(c,CameraActivity.class);				
			}

			@Override
			public Intent runBrowsePhotoActivity(Context c) {
				return null;
				//return new Intent(c,FilterPropActivity.class);				
			}

			@Override
			public Intent runPropertiesActivity(Context c) {
				return new Intent(c,PropertiesActivity.class);
			}
			
		};
	}
	@Override
	public PropertiesMenuEvents getPropertiesMenuEvents() {
		return new PropertiesMenuEvents() {
			
			@Override
			public Intent runFilterPropertiesActivity(Context c) {
				return new Intent(c,FilterPropertiesActivity.class);
			}
			
			@Override
			public Intent runCamPropertiesActivity(Context c) {
				return new Intent(c,CamPropertiesActivity.class);
			}
			
			@Override
			public Intent runBTPropertiesActivity(Context c) {			
				return new Intent(c,BTPropertiesActivity.class);
			}
			@Override
			public Intent runAboutActivity(Context c) {
				return null;
				//return new Intent(c,AboutActivity.class);
			}
		};
	}
	@Override
	public ArrayList<ExternDevice> getEnableDevices() {
		edList = new ArrayList<ExternDevice>(Arrays.asList(
				new ExternDevice("test",false,"cos"),
				new ExternDevice("test1",false,"cos"))); 
		return edList;
	}
	@Override
	public void connectToDevice(ExternDevice ed) {
		for (ExternDevice e : edList) {
			e.setConnected(false);
		}
		ed.setConnected(true);
	}
	@Override
	public ArrayList<Property> getCamProperties(){		
		return camPropList;
	}
	@Override
	public ArrayList<Property> getFilterProperties(){		
		return filterPropList;
	}
	@Override
	public void switchProp(Property property) {
		property.setState((property.isState()?false:true));
	}
}
