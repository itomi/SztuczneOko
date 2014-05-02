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
	private int imgCounter;
	private View lastView;
	public static final int PLEASE_WAIT_DIALOG = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		imgCounter = 0;
		setContentView(R.layout.activity_gallery);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		gridView = (GridView) findViewById(R.id.gridView);		
        GridAdapter = new GalleryGridViewAdapter(this, R.layout.row_grid, imageItems,Environment.getExternalStorageDirectory().getAbsolutePath()+"/soAppDir/myImages/");
        gridView.setAdapter(GridAdapter);
        
    }	
	public void sendButtonClick(View view){
		core.sendPhoto(this);
	}
	public void setSelectedImg(ImageItem selectedImg,View v) {
		if(lastView!=null)lastView.setSelected(false);
		this.selectedImg = selectedImg;
		((EventCollector)core).setCurrentImg(selectedImg);		
		lastView = v;
		
	}
	
	public Dialog onCreateDialog(int dialogId) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("filtracja i wysyłanie zdjęcia");
        dialog.setMessage("Proszę czekać....");
        dialog.setCancelable(true);
        return dialog;
    }
	public void loadLastView() {
		if(lastView!=null)
			lastView.setSelected(true);
	}
}
