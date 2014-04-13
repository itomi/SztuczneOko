package pl.pwr.sztuczneoko.communication;

import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

public class Device {
	
	private BluetoothDevice device;
	
	private Set<Service> services;
	
	boolean servicesChecked = false;
	
	public Device(BluetoothDevice device) {
		this.device = device;
	}

	public String getName() {
		return device.getName();
	}
	
	public String getAddress() {
		return device.getAddress();
	}
	
	public String getDescription() {
		return device.getName() + " " + device.getAddress() + " " + device.getBluetoothClass();
	}
	
	void addService(Service service) {
		this.services.add(service);
	}
	
	boolean isAbleToServe(Service service) {
		return this.services.contains(service);
	}
	
	Set<Service> getDeviceServices() {
		if(!servicesChecked){
			ParcelUuid[] services = device.getUuids();
			for( ParcelUuid parceledUuid : services ) {
				UUID uuid = parceledUuid.getUuid();
				Service foundService = Service.getServiceByUUID(uuid);
				if( foundService == null ) {
					this.services.add(Service.UNKNOWN);
				} else {
					this.services.add(foundService);
				}
			}
		}
		return services;

	}
}
