package pl.pwr.sztuczneoko.ui;

import java.util.ArrayList;

import pl.pwr.sztuczneoko.core.EventCollector;
import pl.pwr.sztuczneoko.core.Property;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

	ListView listView,secListView;
	ArrayList<Property> propList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_properties);
		propList = core.getFilterProperties();
		listView = (ListView) findViewById(R.id.filterPropList);
		listView.setAdapter(new PropertiesListAdapter(this,propList));	    
		((EventCollector)core).restorePreferences(propList);
		((PropertiesListAdapter)listView.getAdapter()).notifyDataSetChanged();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				     int position, long id) {					
				    core.switchProp(propList.get(position));
				    ((PropertiesListAdapter)listView.getAdapter()).notifyDataSetChanged(); 
			}
		});
		
		ArrayList<String> propWithDial = core.getFilterProperiesWithDialog();
		String[] array = propWithDial.toArray(new String[propWithDial.size()]);
		secListView = (ListView) findViewById(R.id.filterDialogPropList);
		secListView.setAdapter(new MenuAdapter(this,array,array,R.layout.row));	    
		secListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				     int position, long id) {					
				switch (position) {
				case 0:
						displayFilterChooseDialog(view);
					break;
				default:
					break;
				}
			}
		});
		
	}	
	public void displayFilterChooseDialog(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("wybór filtra");
	    final ArrayList<String> filterArrayList = core.getEnableFilters();
	    String[] filterArray = filterArrayList.toArray(new String[filterArrayList.size()]);;
	    String tmp = core.getPreferences("currentFilter");
	    builder.setSingleChoiceItems(filterArray,filterArrayList.indexOf((tmp=="")?"gray":tmp), 
	    		new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int index) {	                            
	                            core.savePreferences("currentFilter",filterArrayList.get(index));
	                            dialog.dismiss();
	                    }
	            });
	    
	    builder.show();
    }
	

}
