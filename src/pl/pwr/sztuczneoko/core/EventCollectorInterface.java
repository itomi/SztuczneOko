package pl.pwr.sztuczneoko.core;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;

public interface EventCollectorInterface {
	public EnrtyMenuEvents getEntryMenuEvents();
	public PropertiesMenuEvents getPropertiesMenuEvents();
	public ArrayList<ExternDevice> getEnableDevices();
	public ArrayList<Property> getFilterProperties();
	public ArrayList<Property> getCamProperties();
	public void connectToDevice(ExternDevice ed);
	public void setCurrentImg(byte[] data);
	public void switchProp(Property property); 
	public void sendPhoto(Activity a);
	public void registerBTActivity(Activity btPropertiesActivity);
	public void unregisterBTActivity(Activity btPropertiesActivity);
	public ArrayList<String> getEnableFilters();
	public ArrayList<String> getFilterProperiesWithDialog();
	public void savePreferences(String name,String value);	
	public String getPreferences(String name);
	public ArrayList<String> getCamProperiesWithDialog();
}
