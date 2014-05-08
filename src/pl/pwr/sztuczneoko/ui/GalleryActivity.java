package pl.pwr.sztuczneoko.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import pl.pwr.sztuczneoko.core.EventCollector;
import pl.pwr.sztuczneoko.core.ImageItem;
import pl.pwr.sztuczneoko.ui.GalleryGridViewAdapter.ViewHolder;
import pl.pwr.sztuczneoko.ui.MenuAdapter.ViewSwitch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.os.Build;


public class GalleryActivity extends soActivity {

	private GridView gridView;
	private GalleryGridViewAdapter gridAdapter;
	private ArrayList imageItems = new ArrayList(); 
	private ImageItem selectedImg;
	private View lastView;
	private ProgressDialog progressDialog;
    private MenuItem mItemDelete;
    private MenuItem mItemRename;
    private MenuItem mItemSwitchDirectory;
    private String directory;
    private int width;
    private int height;
	private MenuItem mItemShowImg;
	private MenuItem mItemFilterOptions;
	private MenuItem mItemBTOptions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
		directory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/soAppDir/myImages/";
		setContentView(R.layout.activity_gallery);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		gridView = (GridView) findViewById(R.id.gridView);		
        gridAdapter = new GalleryGridViewAdapter(this, R.layout.row_grid, imageItems,directory,height,width);
        gridView.setAdapter(gridAdapter);       
    }
	@Override
	public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}
	public void sendButtonClick(View view){		
		core.sendPhoto(this,directory);
	}
	public void setSelectedImg(ImageItem selectedImg,View v,int index) {
		if(selectedImg.getImage()== null)return;
		if(lastView!=null)lastView.setSelected(false);
		this.selectedImg = selectedImg;
		((EventCollector)core).setCurrentImg(selectedImg);		
		lastView = v;
		
	}
	
	public void showProgressDialog(String title,String message) {
		progressDialog = ProgressDialog.show(this, title, message, true);
		progressDialog.setCancelable(true);
	}
	public void hideProgressDialog(){
		progressDialog.dismiss();
	}
	
	public void loadLastView() {
		if(lastView!=null&&(((ViewHolder)lastView.getTag()).imageTitle.getText().equals(selectedImg.getTitle()))){
			lastView.setSelected(true);
		}else if(lastView!=null&&gridView.getChildCount()>1){
			for(int i = 0; i<gridView.getChildCount();i++){
				View v = gridView.getChildAt(i);
				if(((ViewHolder)v.getTag()).imageTitle.getText().equals(selectedImg.getTitle())){
					v.setSelected(true);
					lastView = v;
				}
			}
		}		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	mItemDelete = menu.add("usuń");
    	mItemRename = menu.add("zmien nazwe");
    	mItemSwitchDirectory = menu.add("zmien folder");
    	mItemShowImg = menu.add("pokaz zdjecie");
    	mItemBTOptions = menu.add("ustawienia bluetooth");
    	mItemFilterOptions = menu.add("ustawienia filtracji");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item == mItemDelete&&selectedImg!=null) {
    		File file = new File(directory+selectedImg.getTitle());
    		Log.d("del","delete file: "+directory+selectedImg.getTitle());
    		file.delete();
    		gridAdapter.notifyDataSetChanged();
        } else if (item == mItemRename&&selectedImg!=null) {
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(GalleryActivity.this);

            alertDialog.setTitle("Zmien nazwe");

            alertDialog.setMessage(selectedImg.getTitle());
            final EditText input = new EditText(GalleryActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                        	File file = new File(directory+selectedImg.getTitle());
                        	file.renameTo(new File(directory+input.getText()+".jpeg"));
                        	gridAdapter.clear();	                        	
                        	gridAdapter = new GalleryGridViewAdapter(GalleryActivity.this, R.layout.row_grid, imageItems,directory,height,width);
            	            gridView.setAdapter(gridAdapter);  
                        	dialog.dismiss();
                        }
                    });
            
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();

        } else if (item == mItemSwitchDirectory) {
        	if(directory.matches(".*myImages.*"))
        		directory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/soAppDir/myFilterImages/";
        	else
        		directory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/soAppDir/myImages/";
        	gridAdapter.clear();
        	gridAdapter = new GalleryGridViewAdapter(this, R.layout.row_grid, imageItems,directory,height,width);
            gridView.setAdapter(gridAdapter);   
        	gridAdapter.notifyDataSetChanged();
        } else if(item == mItemBTOptions) {
        	startActivity(core.getPropertiesMenuEvents().runBTPropertiesActivity(this));
        } else if(item == mItemFilterOptions) {
        	startActivity(core.getPropertiesMenuEvents().runFilterPropertiesActivity(this));
        } else if(item == mItemShowImg&&selectedImg!=null) {
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	File file = new File(directory+selectedImg.getTitle());
        	Log.d("Gallery","show img: "+file.getAbsolutePath());
        	intent.setDataAndType(Uri.fromFile(file),"image/*");
        	startActivity(intent);
        }    
    	return true;
    }
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("openCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
}
