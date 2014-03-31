package pl.pwr.sztuczneoko.core;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import pl.pwr.sztuczneoko.communication.Communication;
import pl.pwr.sztuczneoko.imageProcessor.ImageProcessor;
import pl.pwr.sztuczneoko.ui.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class EventCollector implements EventCollectorInterface{

	private Communication comm;
	private ImageProcessor imgProc;
	private ArrayList<ExternDevice> edList;
	private ArrayList<Property> camPropList = new ArrayList<Property>();
	private ArrayList<Property> filterPropList = new ArrayList<Property>();
	private byte[] img;
	private String imgName;
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
	@Override
	public void sendPhoto() {		
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
}
