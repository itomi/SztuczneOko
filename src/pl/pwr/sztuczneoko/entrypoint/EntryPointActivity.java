/**
 * 
 * 
 * 
 */
package pl.pwr.sztuczneoko.entrypoint;

import pl.pwr.sztuczneoko.baseapplication.R;
import android.app.Activity;

/**
 * 
 * EntryPoint of BaseApplication, here the journey begins.
 * 
 * @author Karol Kulesza
 *
 */
public class EntryPointActivity extends Activity {
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrylayout);
		
	};
	
}
