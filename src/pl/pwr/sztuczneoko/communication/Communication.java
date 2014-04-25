package pl.pwr.sztuczneoko.communication;

import java.util.Set;

import android.app.Activity;

public interface Communication {
	/**
	 * Method that is going to return cached devices, after the first inquiry ,
	 * when called as a first time in application will return paired devices only.
	 * 
	 * @return set of {@link pl.pwr.sztuczneoko..communication.Device}
	 */
	Set<Device> getCachedDevices();
	
	/**
	 * Method that ignores cached values and performs even long running inquiry, searching for devices.
	 * @return set of {@link pl.pwr.sztuczneoko..communication.Device}
	 * @throws Exception 
	 */
	Set<Device> getDevicesByInquiry(Activity activity) throws Exception;
	
	/**
	 * Method that checks whether {@link Device} is able to communicate with specific Service protocol.
	 * @param device
	 * @param service
	 * @return true when device is able to communicate, false otherwise
	 */
	boolean isDeviceAbleToCommunicateUsingService(Device device, Service service);
	
	/**
	 * Method checks if the Communication module is busy.
	 * 
	 * @return
	 * 		true if busy, false otherwise
	 */
	boolean isBusy();
	
	
	
	abstract void prepareCommunicationBundle() throws Exception;
}
