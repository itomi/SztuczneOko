package pl.pwr.sztuczneoko.core;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import pl.pwr.sztuczneoko.communication.BluetoothCommunication;
import pl.pwr.sztuczneoko.communication.Communication;
import pl.pwr.sztuczneoko.communication.CommunicationProvider;
import pl.pwr.sztuczneoko.communication.CommunicationType;
import pl.pwr.sztuczneoko.communication.Device;
import pl.pwr.sztuczneoko.imageProcessor.ImageProcessor;
import pl.pwr.sztuczneoko.ui.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class EventCollector implements EventCollectorInterface{
	private static final int CHECK_TIME = 500;
	
	private Activity activity;
	private Communication comm;
	private ImageProcessor imgProc;
	
	private ArrayList<ExternDevice> edList;
	private ArrayList<Property> camPropList = new ArrayList<Property>();
	private ArrayList<Property> filterPropList = new ArrayList<Property>();
	
	private byte[] img;
	private String imgName;
	
	protected static final String PREFERENCES_NAME = "preferences";
	protected SharedPreferences preferences;
	
	@Override
	public void setCurrentImg(byte[] data) {		
		img = data;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		imgName = dateFormat.format(date) + ".jpeg";
	}
	public void setCurrentImg(ImageItem data) {		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		data.getImage().compress(Bitmap.CompressFormat.JPEG, 100, stream);
		img = stream.toByteArray();
		imgName = data.getTitle();
	}
	public EventCollector() {
		camPropList.add(new Property("faceDetect", false));
		camPropList.add(new Property("voiceDescription", false));
		camPropList.add(new Property("realTime", false));
		filterPropList.add(new Property("autoFilter", true));
		camPropList.add(new Property("flash",false));
		try {
			comm = CommunicationProvider.provideCommunication(CommunicationType.BLUETOOTH);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public EventCollector(Activity a) {
		this();
		this.activity = a;
	}
    class send extends AsyncTask<Void, Void, Void> {
   	 
        Activity activity;
     
        public send(Activity activity) {
            this.activity = activity;
        }
     
        @Override
        protected void onPreExecute() {
           activity.showDialog(1);
        }
     
        @Override
        protected Void doInBackground(Void... arg0) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            return null;
        }
     
        @Override
        protected void onPostExecute(Void result) {
            activity.removeDialog(1);            
        }
     
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
				return new Intent(c,GalleryActivity.class);				
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
		//TODO: asynchronous device discovery, needs to be repaired i think, we need refreshing the view
		
		edList = new ArrayList<ExternDevice>();
		
		Set<Device> devices = ImmutableSet.of();
		try {
			comm.getDevicesByInquiry();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(comm == null) return edList;
		
		while(comm.isBusy()) {
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		devices = comm.getCachedDevices();
		
		for( final Device device : devices ) {
			edList.add(new ExternDevice(device));
		}				

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
		savePreferences(property);
	}
	@Override
	public void sendPhoto(Activity a) {		
		new send(a).execute();
		saveImg(img);
		Log.d("send", "send image " + imgName);
		/*
		 * TODO send to ImageProcessor and after it to external device
		 */
	}
	/*
	 * test saving on sdCard
	 */
	
	private boolean saveImg(byte[] data) {
		
		String savePath = Environment.getExternalStorageDirectory() + "/soAppDir/myImages/";
		File sdSaveDir = new File(savePath);

		sdSaveDir.mkdirs();

		try {
			String filePath = sdSaveDir.toString() +"/"+ imgName;
			if(new File(filePath).exists()){
				Log.d("save", "file is already exist");
				return true;
			}
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			bos.write(data);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.d("save", "Problem with: "+e.getMessage());
			return false;
		} catch (IOException e) {
			Log.d("save", "Problem with: "+e.getMessage());
			return false;
		}

		return true;
	}
	
	private void savePreferences(Property prop){
		preferences = activity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
	    SharedPreferences.Editor preferencesEditor = preferences.edit();
	    preferencesEditor.putInt(prop.getName(), prop.isState() ? 1 : 0);
	    preferencesEditor.commit();	
	}
	
	public void restorePreferences(ArrayList<Property> propList){
		preferences = activity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
		for(Property prop : propList){
			 int property = preferences.getInt(prop.getName(), 0);
			 prop.setState(property);
		}		
	}
	@Override
	public void registerBTActivity(Activity btPropertiesActivity) {
		try{
			comm.registerBTReceiver(btPropertiesActivity);
		}catch(NullPointerException ex){
			Log.e("bt exception", "null pointer when bt service is off when unregister receeiver");
		}
		
	}
	
	@Override
	public void unregisterBTActivity(Activity btPropertiesActivity) {
		try{
			comm.unregisterBTReceiver(btPropertiesActivity);
		}catch(NullPointerException ex){
			Log.e("bt exception", "null pointer when bt service is off when unregister receeiver");
		}
	}
}
