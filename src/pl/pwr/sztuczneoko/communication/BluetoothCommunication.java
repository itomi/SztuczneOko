package pl.pwr.sztuczneoko.communication;

import java.io.IOException;
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

	private static final String PRECONDITION = "Bluetooth adapted preconditions are not checked.";

	private static BluetoothAdapter ADAPTER = BluetoothAdapter.getDefaultAdapter();
	
	private Set<Device> cachedDevices = new HashSet<Device>();
	
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
	public void getDevicesByInquiry() throws Exception {
				
		if(!ADAPTER.isDiscovering()) {
			beginDiscovery();
		} else {
			ADAPTER.cancelDiscovery();
			beginDiscovery();
		}
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
			StringBuilder builder = new StringBuilder();
			for( final String string : preconditions){
				builder.append("["+string+"]");
			}
			throw new Exception(PRECONDITION + ":" + builder.toString());
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

	@Override
	public void registerBTReceiver(Activity btPropertiesActivity) {
		btPropertiesActivity.registerReceiver(discoveryReceiver, filter);
	}

	@Override
	public void unregisterBTReceiver(Activity btPropertiesActivity) {
		btPropertiesActivity.unregisterReceiver(discoveryReceiver);		
	}

	@Override
	public Session establishConnectionToDevice(Device device, long renewalPeriod) {
		Session session = new Session(device, renewalPeriod);
		
		try {
			session.establishConnection(Service.SP);
		} catch (IOException e) {
			Log.d(this.getClass().toString(), "Could not establish connection to Device:" + device.getDescription(), e);
			return null;
		}
		
		return session;
	}
}
