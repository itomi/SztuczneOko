package pl.pwr.sztuczneoko.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.pwr.sztuczneoko.camera.CameraSurface;
import pl.pwr.sztuczneoko.core.EventCollector;
import pl.pwr.sztuczneoko.core.ExternDevice;
import pl.pwr.sztuczneoko.core.Property;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class CamPropertiesActivity extends soActivity{
	Camera cam = null;
	ListView listView;
	ArrayList<Property> propList;
	ListView secListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		try{
		cam = Camera.open();
		}catch(Exception e){
		}
		setContentView(R.layout.activity_cam_properties);
		propList = core.getCamProperties();
		listView = (ListView) findViewById(R.id.camPropList);
		listView.setAdapter(new PropertiesListAdapter(this,propList));
		((EventCollector)core).restorePreferences(propList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				     int position, long id) {					
				    core.switchProp(propList.get(position));
				    ((PropertiesListAdapter)listView.getAdapter()).notifyDataSetChanged(); 
			}
		});	
		ArrayList<String> propWithDial = core.getCamProperiesWithDialog();
		String[] array = propWithDial.toArray(new String[propWithDial.size()]);
		ArrayList<String> contentDescArrayList = new ArrayList<>(); 
		for(String item : array)
			contentDescArrayList.add(item+"Desc");			
		String[] arrayContentDesc = contentDescArrayList.toArray(new String[contentDescArrayList.size()]);
		
		secListView = (ListView) findViewById(R.id.camDialogPropList);
		secListView.setAdapter(new MenuAdapter(this,array,arrayContentDesc,R.layout.row));	    
		secListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				     int position, long id) {	
				if(cam==null)cam = Camera.open();
				switch (position) {
				case 0:						
						displayDialog(view,cam.getParameters().getSupportedColorEffects(),"collorEfect");
					break;
				case 1:
						displayDialog(view,cam.getParameters().getSupportedWhiteBalance(),"whiteBalance");
					break;
				default:
					break;
				}
			}
		});
	}	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		cam.release();
	}
	
    public void displayDialog(View view,final List<String> options,final String preferenceKey)
    {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] optionsArray = options.toArray(new String[options.size()]); 
            builder.setTitle(getResources().getIdentifier(preferenceKey, "string", 
            		getPackageName()));
            String tmp = core.getPreferences(preferenceKey);
            builder.setSingleChoiceItems(optionsArray, 
            		(tmp=="")?0:options.indexOf(tmp)
    				, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                            core.savePreferences(preferenceKey, options.get(position));
                            dialog.dismiss();
                    }
            });
    
            builder.show();
    }
	
}
