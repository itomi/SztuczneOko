package pl.pwr.sztuczneoko.ui;
import pl.pwr.sztuczneoko.camera.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

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

    Button captureButton;
    Button filterButton;
    Button againPhotoButton;

    public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
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
        
        cameraholder = (FrameLayout)findViewById(R.id.camera_preview);
        
        setupPictureMode();
        filterButton.setEnabled(false);
        againPhotoButton.setEnabled(false);        
    }
	public void againPhotoClick(View view){
		filterButton.setEnabled(false);
        againPhotoButton.setEnabled(false);
        captureButton.setEnabled(true);
        camerasurface.startPreview();
	}
	public void captureClick(View view){
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
	    camerasurface = new CameraSurface(this,core);
	    
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
    
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("openCV", "OpenCV loaded successfully");

                    /* Now enable camera view to start receiving frames */
                    
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
}