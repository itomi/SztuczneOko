package pl.pwr.sztuczneoko.communication;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import android.bluetooth.BluetoothAdapter;

public class BluetoothCommunication implements Communication{

	private static final String PRECONDITION = "Bluetooth adapted preconditions are not checked.";

	private static BluetoothAdapter ADAPTER = BluetoothAdapter.getDefaultAdapter();
	
	private Set<Device> cachedDevices = new ConcurrentSkipListSet<Device>();
	
	private BluetoothCommunication() {}
	
	@Override
	public Set<Device> getCachedDevices() {
		return ImmutableSet.copyOf(cachedDevices);
	}

	@Override
	public Set<Device> getDevicesByInquiry() {
		Set<Device> discoveredByInquiry = new HashSet<Device>();
		
		
		return ImmutableSet.copyOf(cachedDevices);
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

	private void checkPreconditions() throws Exception {
		Set<String> preconditions = gatherPreconditions();
		if(!preconditions.isEmpty()) {
			throw new Exception(PRECONDITION);
		}
	}

	private Set<String> gatherPreconditions() {
		Set<String> preconditions = new HashSet<String>();
		if(ADAPTER == null) {
			preconditions.add("Adapter is not present.");
		} else {
			if( !ADAPTER.isEnabled()) {
				preconditions.add("Adapter is not enabled.");
			}
			//more options here in the future
		}
		return preconditions;
	}
	
}
