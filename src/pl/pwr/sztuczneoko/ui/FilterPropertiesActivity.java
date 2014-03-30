package pl.pwr.sztuczneoko.ui;

import java.util.ArrayList;

import pl.pwr.sztuczneoko.core.Property;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class FilterPropertiesActivity extends soActivity{

	ListView listView;
	ArrayList<Property> propList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_properties);
		propList = core.getFilterProperties();
		listView = (ListView) findViewById(R.id.filterPropList);
		listView.setAdapter(new PropertiesListAdapter(this,propList));	    
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				     int position, long id) {					
				    core.switchProp(propList.get(position));
				    ((PropertiesListAdapter)listView.getAdapter()).notifyDataSetChanged(); 
			}
		});	
	}
	

}
