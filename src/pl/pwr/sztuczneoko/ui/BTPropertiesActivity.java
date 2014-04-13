package pl.pwr.sztuczneoko.ui;

import java.util.ArrayList;

import pl.pwr.sztuczneoko.core.ExternDevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.os.Build;
import android.preference.PreferenceActivity;

public class BTPropertiesActivity extends soActivity{

	ListView listView;
	ArrayList<ExternDevice> devices;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_btproperties);
		//core.registerBTActivity(this);
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  core.unregisterBTActivity(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	core.registerBTActivity(this);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	core.unregisterBTActivity(this);
    }
    
	public void findDevice(View v){
		
		devices = core.getEnableDevices();
		
		listView = (ListView) findViewById(R.id.BTDeviceList);
		
		listView.setAdapter(new DeviceListAdapter(this,devices));	   
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				     int position, long id) {					
				    core.connectToDevice(devices.get(position));
				    Log.d("conn", "polaczono z " + devices.get(position));
				    ((DeviceListAdapter)listView.getAdapter()).notifyDataSetChanged(); 
			}
		});
		
	}
	
	
	public void turnOnOff(View view){
		// TODO switch bt status
	}
}
