package pl.pwr.sztuczneoko.ui;
import pl.pwr.sztuczneoko.core.*;
import android.app.Activity;
import android.content.SharedPreferences;

public class soActivity extends Activity{	
	protected EventCollectorInterface core = new EventCollector(this);	
}
