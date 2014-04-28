package pl.pwr.sztuczneoko.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import pl.pwr.sztuczneoko.core.ExternDevice;
import pl.pwr.sztuczneoko.core.ImageItem;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
		new EnableDevice(this).execute();
	}
	
	public void turnOnOff(View view){
		// TODO switch bt status
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, 1);
		}
	}
	
	private class EnableDevice extends AsyncTask<Void, Void, Void> {
	   	 
        Activity activity;
     
        public EnableDevice(Activity activity) {
            this.activity = activity;
        }
     
        @Override
        protected void onPreExecute() {
           activity.showDialog(GalleryActivity.PLEASE_WAIT_DIALOG);
        }
     
        @Override
        protected Void doInBackground(Void... arg0) {
        	devices = core.getEnableDevices();        	
        	return null;
        	
        }
     
        @Override
        protected void onPostExecute(Void result) {
            activity.removeDialog(GalleryActivity.PLEASE_WAIT_DIALOG);
            listView = (ListView) findViewById(R.id.BTDeviceList);
    		
    		listView.setAdapter(new DeviceListAdapter(activity,devices));	   
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
     
    }
	
	public Dialog onCreateDialog(int dialogId) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("szukanie urządzeń bluethoot");
        dialog.setMessage("Proszę czekać....");
        dialog.setCancelable(true);
        return dialog;
    }
}
