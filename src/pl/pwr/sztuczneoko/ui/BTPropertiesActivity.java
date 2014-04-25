package pl.pwr.sztuczneoko.ui;

import java.util.ArrayList;

import pl.pwr.sztuczneoko.core.ExternDevice;

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
	
	}
	
	public void findDevice(View v){
		
		devices = core.getEnableDevices(this);
		
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
