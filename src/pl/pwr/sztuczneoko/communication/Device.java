package pl.pwr.sztuczneoko.communication;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
	
	public Set<Service> getDeviceServices() {
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
	
	BluetoothSocket connect(final Service service) throws IOException {
		return device.createInsecureRfcommSocketToServiceRecord(service.UUID());
	}

	public void fetchUUID() throws Exception {
		if(!device.fetchUuidsWithSdp()){
			throw new Exception("Could not start service search!");
		}
	}
	@Override
    public int hashCode() {
        return this.getAddress().hashCode();
    }
	@Override
	public boolean equals(Object o) {
		return this.getAddress().equals(((Device)o).getAddress());
	}
}
