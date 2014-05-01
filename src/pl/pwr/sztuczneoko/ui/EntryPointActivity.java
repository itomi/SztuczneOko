/**
 * 
 * 
 * 
 */
package pl.pwr.sztuczneoko.ui;
import pl.pwr.sztuczneoko.ui.R;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * EntryPoint of BaseApplication, here the journey begins.
 * 
 * @author Karol Kulesza
 *
 */

public class EntryPointActivity extends soActivity implements OnItemClickListener{
    ListView listView;
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrylayout);		
		
		
		listView = (ListView) findViewById(R.id.entryListMenu);
		listView.setAdapter(new MenuAdapter(this,
				getResources().getStringArray(R.array.entryListMenuArray),
				getResources().getStringArray(R.array.entryListMenuContentDesc)));
	    
		listView.setOnItemClickListener(this);		
	}

	/*
	 * Parameters:
	    adapter - The AdapterView where the click happened.
	    view - The view within the AdapterView that was clicked
	    position - The position of the view in the adapter.
	    id - The row id of the item that was clicked.
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {		
		switch(position){
		
			case 0:
				startActivity(core.getEntryMenuEvents().runPhotoActivity(this)); 
				break;				
			case 1:
				startActivity(core.getEntryMenuEvents().runPropertiesActivity(this));				
				break;
			case 2:
				startActivity(core.getEntryMenuEvents().runBrowsePhotoActivity(this));
				break;	
			
		}
		
		/*String selectedValue = listView.getAdapter().getItem(position).toString();
	    Toast.makeText(getApplicationContext(), selectedValue,
	              Toast.LENGTH_SHORT).show();
	              */		
	}
	
}
