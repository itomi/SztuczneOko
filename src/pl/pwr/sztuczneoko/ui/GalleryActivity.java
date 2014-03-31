package pl.pwr.sztuczneoko.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import pl.pwr.sztuczneoko.core.EventCollector;
import pl.pwr.sztuczneoko.core.ImageItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.os.Build;


public class GalleryActivity extends soActivity {

	private GridView gridView;
	private GalleryGridViewAdapter GridAdapter;
	private ArrayList imageItems = new ArrayList(); 
	private ImageItem selectedImg; 
	public static final int PLEASE_WAIT_DIALOG = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		new getData(this).execute();
		gridView = (GridView) findViewById(R.id.gridView);		
        GridAdapter = new GalleryGridViewAdapter(this, R.layout.row_grid, imageItems);
        gridView.setAdapter(GridAdapter);
        
    }	
	public void sendButtonClick(View view){
		core.sendPhoto();
	}
	public void setSelectedImg(ImageItem selectedImg) {
		this.selectedImg = selectedImg;
		((EventCollector)core).setCurrentImg(selectedImg);		
	}
	
	public Dialog onCreateDialog(int dialogId) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Wczytywanie galerii");
        dialog.setMessage("Proszę czekać....");
        dialog.setCancelable(true);
        return dialog;
    }
 
    class getData extends AsyncTask<Void, Void, Void> {
    	 
        Activity activity;
     
        public getData(Activity activity) {
            this.activity = activity;
        }
     
        @Override
        protected void onPreExecute() {
           activity.showDialog(GalleryActivity.PLEASE_WAIT_DIALOG);
        }
     
        @Override
        protected Void doInBackground(Void... arg0) {
        	final Pattern p = Pattern.compile(".*\\.jpeg");

            File soDir = new File(Environment
                    .getExternalStorageDirectory()
                    .getAbsolutePath()+"/soAppDir/myImages/");
            
            final File[] files = soDir.listFiles(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return p.matcher(file.getName()).matches();
                }
            });
            for (File file : files) {
            	Bitmap bitmap = decodeBitmapFromFile(file.getAbsolutePath(), 100, 100);
    		    imageItems.add(new ImageItem(bitmap, file.getName()));    		    
    		}   
            return null;
        }
     
        @Override
        protected void onPostExecute(Void result) {
            activity.removeDialog(GalleryActivity.PLEASE_WAIT_DIALOG);            
            ((GalleryGridViewAdapter)gridView.getAdapter()).notifyDataSetChanged();
        }
     
    }
    public static int calculateInSampleSize(
    		BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
    }
    public static Bitmap decodeBitmapFromFile(String file, 
    		int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }
}
