package pl.pwr.sztuczneoko.ui;

import java.util.ArrayList;
import java.util.Arrays;

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
	MenuAdapter mAdapter;
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
		ArrayList<String> contentDescArrayList = new ArrayList<>(); 
		for(String item : array)
			contentDescArrayList.add(item+"Desc");			
		String[] arrayContentDesc = contentDescArrayList.toArray(new String[contentDescArrayList.size()]);
		mAdapter = new MenuAdapter(this,array,arrayContentDesc,R.layout.row);
		secListView = (ListView) findViewById(R.id.filterDialogPropList);
		secListView.setAdapter(mAdapter);	    
		secListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				     int position, long id) {					
				switch (position) {
				case 0:
						displayFilterChooseDialog(view);
					break;
				case 1:
						filterParamChooseDialog(view,false);
					break;
				case 2:
						filterParamChooseDialog(view,true);
				break;
				case 3:
						setTargetImageResolutiuon(view);
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
	                            finish();
	                            startActivity(getIntent());
	                    }
	            });
	    
	    
	    builder.show();
    }
	public void filterParamChooseDialog(View view,final boolean secondParam)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList<String> filterArrayList;
        String[] filterArray;
        String tmp;
	    builder.setTitle("parametry filtracji");
	    if(!secondParam){
		    filterArrayList = ((EventCollector)core).getParamFilter();
		    filterArray = filterArrayList.toArray(new String[filterArrayList.size()]);;
		    tmp = core.getPreferences("filterParam");
	    }else{
	    	filterArrayList = new ArrayList<>(Arrays.asList("0","1"));
		    filterArray = filterArrayList.toArray(new String[filterArrayList.size()]);;
		    tmp = core.getPreferences("secondFilterParam");
	    }
	    builder.setSingleChoiceItems(filterArray,(tmp=="")?0:filterArrayList.indexOf(tmp) , 
	    		new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int index) {	                            
	                            if(!secondParam)core.savePreferences("filterParam",filterArrayList.get(index));	                            
	                            else core.savePreferences("secondFilterParam",filterArrayList.get(index));
	                            dialog.dismiss();
	                    }
	            });
	    
	    builder.show();
    }
	public void setTargetImageResolutiuon(View view){
		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        final ArrayList<String> resArrayList;
	        String[] resArray;
	        String tmp;
		    builder.setTitle("rozdzielczość");
		   
		    	resArrayList = new ArrayList<>(Arrays.asList("1024x860","800x600","250x250","128x64","100x100","32x32"));
			    resArray = resArrayList.toArray(new String[resArrayList.size()]);;
			    tmp = core.getPreferences("targetResolution");
		    
		    builder.setSingleChoiceItems(resArray,(tmp=="")?resArrayList.indexOf("100x100"):resArrayList.indexOf(tmp) , 
		    		new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int index) {	                            
		                            core.savePreferences("targetResolution",resArrayList.get(index));	                            		                          
		                            dialog.dismiss();
		                    }
		            });
		    
		    builder.show();
	}
	

}
