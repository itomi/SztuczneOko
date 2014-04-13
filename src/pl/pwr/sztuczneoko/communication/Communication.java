package pl.pwr.sztuczneoko.communication;

import java.util.Set;

public interface Communication {
	/**
	 * Method that is gogin to return cached devices, after the first inquiry ,
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
	void getDevicesByInquiry() throws Exception;
	
	/**
	 * Method that checks whether {@link Device} is able to communicate with specific Service protocol.
	 * @param device
	 * @param service
	 * @return true when device is able to communicate, false otherwise
	 */
	boolean isDeviceAbleToCommunicateUsingService(Device device, Service service);
	
	abstract void prepareCommunicationBundle();

	void registerBTReceiver(Activity btPropertiesActivity);

	void unregisterBTReceiver(Activity btPropertiesActivity);
}
