package pl.pwr.sztuczneoko.communication;

import java.util.Set;

public class DeviceManager {
	
	public Set<Service> getServicesOfGivenDevice(final Device device) {
		return device.getDeviceServices();
	}
	
}
