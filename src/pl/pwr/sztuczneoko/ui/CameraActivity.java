package pl.pwr.sztuczneoko.ui;
import pl.pwr.sztuczneoko.camera.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.regex.Pattern;

import pl.pwr.sztuczneoko.camera.CameraPreview;
import pl.pwr.sztuczneoko.camera.CameraSurface;
import pl.pwr.sztuczneoko.core.ImageItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class CameraActivity extends soActivity implements CameraCallback{
    private FrameLayout cameraholder = null;
    private CameraSurface camerasurface = null;


    protected static final String MEDIA_TYPE_IMAGE = null;
	//private Camera mCamera;
    //private CameraPreview mPreview;
    /*private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	Log.d("cam", "fotka");
        	core.setCurrentImg(data);
        }
    };*/

    Button captureButton;
    Button filterButton;
    Button againPhotoButton;
    
    @Override
	protected void onDestroy() {
    //	mCamera.release();
		super.onDestroy();
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        captureButton = (Button) findViewById(R.id.button_capture);
        filterButton = (Button) findViewById(R.id.button_filter_photo);
        againPhotoButton = (Button) findViewById(R.id.button_photo_again);
        
        //((ImageButton)findViewById(R.id.coloreffect)).setOnClickListener(onButtonClick);
        //((ImageButton)findViewById(R.id.whitebalance)).setOnClickListener(onButtonClick);
        // Create an instance of Camera
        //mCamera = Camera.open();
        
        cameraholder = (FrameLayout)findViewById(R.id.camera_preview);
        
        setupPictureMode();
        
        // Create our Preview view and set it as the content of our activity.
        //mPreview = new CameraPreview(this, mCamera);
        //FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        //preview.addView(mPreview);        
        filterButton.setEnabled(false);
        againPhotoButton.setEnabled(false);        
        
    }
	public void againPhotoClick(View view){
		filterButton.setEnabled(false);
        againPhotoButton.setEnabled(false);
        captureButton.setEnabled(true);
        //mCamera.startPreview();
        camerasurface.startPreview();
	}
	public void captureClick(View view){
		//mCamera.takePicture(null, null, mPicture);
		camerasurface.startTakePicture();
		filterButton.setEnabled(true);
        againPhotoButton.setEnabled(true);
        captureButton.setEnabled(false);
	}
	public void filterPhotoClick(View view){		
		core.sendPhoto(this);
	}
	public Dialog onCreateDialog(int dialogId) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("przetwarznie i wysyłanie zdjęcia");
        dialog.setMessage("Proszę czekać....");
        dialog.setCancelable(true);
        return dialog;
    }
 

	private void setupPictureMode(){
	    camerasurface = new CameraSurface(this);
	    
	    cameraholder.addView(camerasurface, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    
	    camerasurface.setCallback(this);
	}

    @Override
    public void onJpegPictureTaken(byte[] data, Camera camera) {
            try
            {
            	Log.d("cam", "fotka");
            	core.setCurrentImg(data);
            }
            catch(Exception e)
            {
                    e.printStackTrace();
            }
            
            
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
    }

    @Override
    public void onRawPictureTaken(byte[] data, Camera camera) {
    }

    @Override
    public void onShutter() {
    }
    
    @Override
    public String onGetVideoFilename(){
            String filename = String.format("/sdcard/%d.3gp",System.currentTimeMillis());
            
            return filename;
    }
    
    public void displayAboutDialog()
    {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
	    builder.setTitle(getString(R.string.app_name));
	    builder.setMessage("Sample application to demonstrate the use of Camera in Android\n\nVisit www.krvarma.com for more information.");
	    
	    builder.show();
    }
    
    public void displayColorEffectDialog(View view)
    {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setTitle("efekt kolorow");
    builder.setSingleChoiceItems(camerasurface.getSupportedColorEffects(), 
                    camerasurface.getCurrentColorEffect(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            camerasurface.setColorEffect(which);
                            
                            dialog.dismiss();
                    }
            });
    
    builder.show();
    }
    
    public void displayWhiteBalanceDialog(View view)
    {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setTitle("balans bieli");
    builder.setSingleChoiceItems(camerasurface.getSupportedWhiteBalances(), 
                    camerasurface.getCurrentWhiteBalance(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            camerasurface.setWhiteBalance(which);
                            
                            dialog.dismiss();
                    }
            });
    
    builder.show();
    }
}