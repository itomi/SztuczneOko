package pl.pwr.sztuczneoko.ui;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class PropertiesActivity extends soActivity implements OnItemClickListener{
	
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_properties);
		listView = (ListView) findViewById(R.id.propertiesListMenu);
		listView.setAdapter(new MenuAdapter(this,
				getResources().getStringArray(R.array.propertiesListMenuArray),
				getResources().getStringArray(R.array.propertiesListMenuContentDesc)));
	    
		listView.setOnItemClickListener(this);			
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		switch(position){
		
			case 0:
				startActivity(core.getPropertiesMenuEvents().runFilterPropertiesActivity(this)); 
				break;				
			case 1:
				startActivity(core.getPropertiesMenuEvents().runBTPropertiesActivity(this));				
				break;
			case 2:
				startActivity(core.getPropertiesMenuEvents().runCamPropertiesActivity(this));
				break;
			case 3:
				//startActivity(core.getPropertiesMenuEvents().runAboutActivity(this));
				//test opencv lib
				startActivity(new Intent((Context)this,Puzzle15Activity.class));
				
				break;
		}
	}
}
