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
import pl.pwr.sztuczneoko.imageProcessor.ImageFilter;
import pl.pwr.sztuczneoko.imageProcessor.ImageProcessor;
import pl.pwr.sztuczneoko.ui.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * 
 *  
 */
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
	
	/**
	 * set img from param and imgName as current data time    
	 * @param data image convert to byte array
	 */
	@Override
	public void setCurrentImg(byte[] data) {				
		img = data;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		imgName = dateFormat.format(date) + ".jpeg";
	}
	
	/**
	 * set img and imgName from ImageItem object 
	 * @param data ImageItem object 
	 */
	public void setCurrentImg(ImageItem data) {		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		data.getImage().compress(Bitmap.CompressFormat.JPEG, 100, stream);
		img = stream.toByteArray();
		imgName = data.getTitle();
	}
	/**
	 * default constructor
	 */
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
	
	/**
	 * constructor with activity object
	 * @param a
	 */
	public EventCollector(Activity a) {
		this();
		this.activity = a;
	}
	
	/**
	 * class extends thread call save img(byte[]) as /soAppDir/myImage/'imgName'
	 * convert to bitmap and resize, call filter img and call save 
	 * it as /soAppDir/myFilterImages/filtered-'imgName'$[d]   
	 * @author mateusz
	 *
	 */
    class send extends AsyncTask<Void, Void, Void> {
   	 
        Activity activity;
        String location;
        public send(Activity activity,String location) {
            this.activity = activity;
            this.location = location;
        }
     
        @Override
        protected void onPreExecute() {
        	Object[] args = {"Proszę czekać....","filtracja i wysyłanie zdjęcia"}; 
        	try {
        		Log.d("class", activity.getClass().getName());
        		Class[] args1 = new Class[2];
                args1[0] = String.class;
                args1[1] = String.class;
				activity.getClass().getMethod("showProgressDialog",args1).invoke(activity, args);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
     
        @Override
        protected Void doInBackground(Void... arg0) {
        	try {
        		String tmpFileName = imgName;
        		
        		saveImg(img,imgName,location);
        		Log.d("send", "send image " + imgName );
        		
        		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        		BitmapFactory.Options options = new BitmapFactory.Options();             	
        		Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length, options);
        		bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        		switch(getPreferences("currentFilter")){
	        		case "gray":
	        			new ImageFilter(bitmap).grayFilter().compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        			break;
	        		case "blur":
	        			new ImageFilter(bitmap).blur().compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        			break;
	        		case "sobel":
	        			new ImageFilter(bitmap).sobel().compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        			break;
	        		case "canny":
	        			//(min, max) (70,90),(30,50),(50,70),(90,110)
	        			int min = Integer.parseInt(getPreferences("filterParam").split(",")[0]);
	        			int max = Integer.parseInt(getPreferences("filterParam").split(",")[1]);
	        			new ImageFilter(bitmap).cannyFilter().compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        			break;
	        		case "treshold":
	        			//blockSize 3,5,7; color 1 lub 0
	        			int blockSize = Integer.parseInt(getPreferences("filterParam"));
	        			int color = Integer.parseInt(getPreferences("secondFilterParam"));
	        			new ImageFilter(bitmap).thresholdFilter().compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        			break;
	        		case "binary":
	        			new ImageFilter(bitmap).binaryFilter().compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        			break;
	        		case "cropp":
	        			new ImageFilter(bitmap).cropp().compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        			break;
	        		default:
	        			break;
        		}  
        		Log.d("send", "filter img " + imgName + " done");
        		File f= new File(Environment.getExternalStorageDirectory()+"/soAppDir/myFilterImages/filtered-"+tmpFileName); 
        		while(f.exists()){
        			if(tmpFileName.matches(".*\\$\\d.*")){
        				int counter = Integer.parseInt(tmpFileName.split("(\\$)|(\\.)")[1]);
        				tmpFileName = tmpFileName.replaceAll("\\$\\d.+","\\$"+(counter+1)+".jpeg");        			    
        			}else {
						tmpFileName=tmpFileName.replaceAll("\\.","\\$1\\.");//"$1";
					}
        			f= new File(Environment.getExternalStorageDirectory()+"/soAppDir/myFilterImages/filtered-"+tmpFileName); 
        		}
        		saveImg(stream.toByteArray(),"filtered-"+tmpFileName,"/soAppDir/myFilterImages/");
        		
			} catch (Exception e) {
				e.printStackTrace();
			}
            return null;
        }
     
		@Override
        protected void onPostExecute(Void result) {
			try {
				activity.getClass().getMethod("hideProgressDialog",null).invoke(activity);
			} catch (Exception e) {
				e.printStackTrace();
			}          
        }
     
    }
    
    /**
     * runs activities from entry activity
     */
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
	
	/**
	 * runs activities from properties menu 
	 */
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
	
	/**
	 * method get list of devices from bluetooth
	 * @return ArrayList<ExternDevice>
	 */
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
	
	/**
	 * set device connect param as true
	 * @param ed 
	 */
	@Override
	public void connectToDevice(ExternDevice ed) {
		for (ExternDevice e : edList) {
			e.setConnected(false);
		}
		ed.setConnected(true);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public ArrayList<String> getFilterProperiesWithDialog() {
		ArrayList<String> array = new ArrayList<String>(Arrays.asList("chooseFilter","setParamFilter"));
		if(filterParamsCount()>1)array.add("setSecondParamFilter");
		return array;
	}
	
	private int filterParamsCount(){
		int result;
		switch(getPreferences("currentFilter")){
		case "treshold":
			result = 2;			
			break;
		case "binary":
			result = 2;
			break;		
		default:
			result = 1;
			break;
		}
		return result;
	}
	/**
	 * @return
	 */
	@Override
	public ArrayList<String> getCamProperiesWithDialog() {
		return new ArrayList<String>(Arrays.asList("whiteBalance","collorEfect"));
	}	
	
	/**
	 * 
	 * @return
	 */
	@Override
	public ArrayList<String> getEnableFilters() {
		/*
		 * TODO implement in imgProc getter for list possible filters
		 */
		return new ArrayList<String>(Arrays.asList("gray","canny","treshold","blur","sobel", "binary", "cropp"));
	}
	public ArrayList<String> getParamFilter() {
		ArrayList<String> result = new ArrayList<>();
		switch(getPreferences("currentFilter")){
			case "gray":
				
				break;
			case "blur":
				//bl 3,5,7
				result.add("3");
				result.add("5");
				result.add("7");
				break;
			case "sobel":
				//(minVal, maxVal) (-100, 100),(-200,200), (-400,400)
				result.add("-100, 100");
				result.add("-200,200");
				result.add("-400,400");
				break;
			case "canny":
				result.add("70,90");
				result.add("30,50");
				result.add("50,70");
				result.add("90,110");
				break;
			case "treshold":
				//blockSize 3,5,7; color 1 lub 0
				result.add("3");
				result.add("5");
				result.add("7");
								
				break;
			case "binary":
				//bin -1, 20, 120, 220; color 1 lub 0
				result.add("-1");
				result.add("20");
				result.add("120");
				result.add("220");
								
				break;
			case "cropp":
				
				break;
			default:
				break;
		}
		return result;
	}
	/**
	 * get arraylist of camera properties
	 * @return ArrayList<Property> 
	 */	
	@Override
	public ArrayList<Property> getCamProperties(){		
		return camPropList;
	}
	
	/**
	 * get arraylist of filter class properties
	 * @return ArrayList<Property> 
	 */	
	@Override
	public ArrayList<Property> getFilterProperties(){		
		return filterPropList;
	}
	
	/**
	 * switch state of property (t/f) and save in shared preferences
	 */	
	@Override
	public void switchProp(Property property) {
		property.setState((property.isState()?false:true));
		savePreferences(property);
	}
	
	/**
	 * method create asynctask send class and run new thread to save->filter->send img
	 * @param a calling activity
	 */
	@Override
	public void sendPhoto(Activity a,String location) {		
		if (img==null) return;
		new send(a,location).execute();
	}
	
	/**
	 * saving image on sdCard, doesn't save if file exist
	 * @param data image convert to byte[]
	 * @param name name of new file
	 * @param location parent directory new file 
	 */	
	private boolean saveImg(byte[] data,String name,String location) {
		
		String savePath = Environment.getExternalStorageDirectory() + location;
		File sdSaveDir = new File(savePath);

		sdSaveDir.mkdirs();

		try {
			String filePath = sdSaveDir.toString() +"/"+ name;
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
	
	/**
	 * save property in SharedPreferences
	 * @param prop 
	 */
	private void savePreferences(Property prop){
		preferences = activity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
	    SharedPreferences.Editor preferencesEditor = preferences.edit();
	    preferencesEditor.putInt(prop.getName(), prop.isState() ? 1 : 0);
	    preferencesEditor.commit();	
	}
	
	/**
	 * save [string][string] in SharedPreferences
	 * @param name
	 * @param value
	 */
	public void savePreferences(String name,String value){
		preferences = activity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
	    SharedPreferences.Editor preferencesEditor = preferences.edit();
	    preferencesEditor.putString(name, value);
	    preferencesEditor.commit();	
	}
	
	/**
	 * get sharedPrefs and set array<Property> from param to last save state 
	 * @param propList
	 */
	public void restorePreferences(ArrayList<Property> propList){
		preferences = activity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
		for(Property prop : propList){
			 int property = preferences.getInt(prop.getName(), 0);
			 prop.setState(property);
		}		
	}
	
	/**
	 * get string value from sharedPref, if key not exist return value ""
	 * @param name String key  
	 * @return String value 
	 */
	public String getPreferences(String name){
		preferences = activity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
		return preferences.getString(name, "");
	}
	
	/**
	 * jakies cuda do bt :)
	 */
	@Override
	public void registerBTActivity(Activity btPropertiesActivity) {
		try{
			comm.registerBTReceiver(btPropertiesActivity);
		}catch(NullPointerException ex){
			Log.e("bt exception", "null pointer when bt service is off when unregister receeiver");
		}
		
	}
	
	/**
	 * jakies cuda do bt :)
	 */
	@Override
	public void unregisterBTActivity(Activity btPropertiesActivity) {
		try{
			comm.unregisterBTReceiver(btPropertiesActivity);
		}catch(NullPointerException ex){
			Log.e("bt exception", "null pointer when bt service is off when unregister receeiver");
		}
	}	
}
