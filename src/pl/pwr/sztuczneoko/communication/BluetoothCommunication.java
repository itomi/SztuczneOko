package pl.pwr.sztuczneoko.communication;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import android.bluetooth.BluetoothAdapter;

public class BluetoothCommunication implements Communication{

	private static BluetoothAdapter ADAPTER = BluetoothAdapter.getDefaultAdapter();
	
	private Set<Device> cachedDevices = new ConcurrentSkipListSet<Device>();
	
	@Override
	public Set<Device> getCachedDevices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Device> getDevicesByInquiry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDeviceAbleToCommunicateUsingService(Device device,
			Service service) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void prepareCommunicationBundle() {
		
	}
	
}
