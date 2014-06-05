package pl.pwr.sztuczneoko.ui;
import pl.pwr.sztuczneoko.core.*;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class soActivity extends Activity{	
	protected ActionBar actionBar;
	protected EventCollectorInterface core = new EventCollector(this);	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		actionBar = getActionBar();
		super.onCreate(savedInstanceState);
		checkBT();
	}
	@Override
	protected void onResume() {
		super.onResume();
		checkBT();
	}
	
	protected void checkBT(){
		if(((EventCollector)core).checkBTconnection()==false)
			actionBar.setSubtitle("status połączenie : brak");
		else
			actionBar.setSubtitle("status połączenie : połączono");
	}
}
