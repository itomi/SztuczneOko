package pl.pwr.sztuczneoko.communication;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import android.os.Parcelable;
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
				Device deviceWrapper = new Device(device);				
				cachedDevices.add(deviceWrapper);
			} else if(BluetoothDevice.ACTION_UUID.equals(action)) {
				BluetoothDevice d = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
				for(final Parcelable data : uuidExtra) {
					Log.d(this.getClass().toString(), "Service: " + data.toString() + " Device: " + d.getName());
				}
			}
		}
	};
	
	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND) {
		{
			addAction(BluetoothDevice.ACTION_UUID);
		}
	};
	
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
			session.establishConnection(Service.AEYE);
			SessionsHolder.addNewSessionAndRegisterForActivityChecks(session, renewalPeriod);
		} catch (Exception e) {
			Log.d(this.getClass().toString(), "Could not establish connection to Device:" + device.getDescription(), e);
			return null;
		}
		
		return session;
	}

	@Override
	public void sendToAllConnectedDevices(byte[] photo) {
		for( final Session session : SessionsHolder.getActiveSessions()) {
			try {
				session.send(photo);
			} catch (IOException e) {
				Log.i(this.getClass().toString(), "Could not send data to " + session.getDevice().getAddress());
			}
		}
	}
	
	public boolean isConnect(){
		byte[] testByte = {(byte)0};
		for( final Session session : SessionsHolder.getActiveSessions())			
			try {			
					session.send(testByte);
			} catch (IOException e) {
				Log.i(this.getClass().toString(), "Could not send data to " + session.getDevice().getAddress());
				return false;
			}
		
		return (SessionsHolder.getActiveSessions().size()!=0);
	}
}
