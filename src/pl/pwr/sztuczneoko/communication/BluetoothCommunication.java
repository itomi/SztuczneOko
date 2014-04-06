package pl.pwr.sztuczneoko.communication;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import junit.framework.Assert;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.common.collect.ImmutableSet;

public class BluetoothCommunication implements Communication{

	private static final int RESULT_CODE = 0xDEADBEEF;

	private static final String PRECONDITION = "Bluetooth adapted preconditions are not checked.";

	private static BluetoothAdapter ADAPTER = BluetoothAdapter.getDefaultAdapter();
	
	private Set<Device> cachedDevices = new ConcurrentSkipListSet<Device>();
	
	BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if(BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				cachedDevices.add(new Device(device));
			}
		}
	};
	
	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	
	private BluetoothCommunication() {
		super();

	}
	
	@Override
	public Set<Device> getCachedDevices() {
		return ImmutableSet.copyOf(cachedDevices);
	}
	
	@Override
	public Set<Device> getDevicesByInquiry(Activity activity) throws Exception {
		activity.registerReceiver(discoveryReceiver, filter);
		
		if(!ADAPTER.isDiscovering()) {
			beginDiscovery();
		} else {
			ADAPTER.cancelDiscovery();
			beginDiscovery();
		}
		
		return this.cachedDevices;
	}

	private void beginDiscovery() throws Exception {
		if(!ADAPTER.startDiscovery()) {
			throw new Exception("Couldn't start discovery!");
		}
	}

	@Override
	public boolean isDeviceAbleToCommunicateUsingService(Device device,
			Service service) {
		Set<Service> deviceKnownServices = device.getDeviceServices();
		return deviceKnownServices.contains(service);
	}

	@Override
	public void prepareCommunicationBundle() throws Exception {
		checkPreconditions();
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
	
	static BluetoothAdapter getAdapter() {
		return ADAPTER;
	}

	@Override
	public boolean isBusy() {
		Assert.assertNotNull(ADAPTER);
		return ADAPTER.isDiscovering();
	}
}
