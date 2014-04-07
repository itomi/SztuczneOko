package pl.pwr.sztuczneoko.core;

import java.util.ArrayList;

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
	public void sendPhoto();
}
