package pl.pwr.sztuczneoko.communication;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.common.collect.ImmutableSet;

public class BluetoothCommunication extends Activity implements Communication{

	private static final int RESULT_CODE = 0xDEADBEEF;

	private static final String PRECONDITION = "Bluetooth adapted preconditions are not checked.";

	private static BluetoothAdapter ADAPTER = BluetoothAdapter.getDefaultAdapter();
	
	private Set<Device> cachedDevices = new ConcurrentSkipListSet<Device>();
	
	private BroadcastReceiver discoveryReceiver;
	
	private BluetoothCommunication() {
		discoveryReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				
				if(BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					cachedDevices.add(new Device(device));
				}
			}
		};
	}
	
	@Override
	public Set<Device> getCachedDevices() {
		return ImmutableSet.copyOf(cachedDevices);
	}

	@Override
	public Set<Device> getDevicesByInquiry() throws Exception {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void prepareCommunicationBundle() {
	    if (!ADAPTER.isEnabled()){
	        Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        startActivityForResult(enableBT, RESULT_CODE);
	    }
		
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
	
}
