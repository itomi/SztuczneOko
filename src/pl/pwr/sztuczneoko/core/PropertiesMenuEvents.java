package pl.pwr.sztuczneoko.core;

import android.content.Context;
import android.content.Intent;

public interface PropertiesMenuEvents {
	
	public Intent runBTPropertiesActivity(Context c);
	public Intent runCamPropertiesActivity(Context c);
	public Intent runFilterPropertiesActivity(Context c);
	public Intent runAboutActivity(Context c);
}
