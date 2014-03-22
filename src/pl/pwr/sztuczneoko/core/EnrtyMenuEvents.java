package pl.pwr.sztuczneoko.core;

import android.content.Context;
import android.content.Intent;

public interface EnrtyMenuEvents {
	public Intent runPhotoActivity(Context c);
	public void runBrowsePhotoActivity();
	public void runPreferenceActivity();
}
