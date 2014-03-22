package pl.pwr.sztuczneoko.ui;

import pl.pwr.sztuczneoko.camera.CameraPreview;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends soActivity {

    protected static final String MEDIA_TYPE_IMAGE = null;
	private Camera mCamera;
    private CameraPreview mPreview;
    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	Log.d("cam", "fotka");
        	core.setCurrentImg(data);
        }
    };

    Button captureButton;
    Button filterButton;
    Button againPhotoButton;
    
    @Override
	protected void onDestroy() {
    	mCamera.release();
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

        // Create an instance of Camera
        mCamera = Camera.open();
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);        
        filterButton.setEnabled(false);
        againPhotoButton.setEnabled(false);        
        
    }
	public void againPhotoClick(View view){
		filterButton.setEnabled(false);
        againPhotoButton.setEnabled(false);
        mCamera.startPreview();
	}
	public void captureClick(View view){
		mCamera.takePicture(null, null, mPicture);
		filterButton.setEnabled(true);
        againPhotoButton.setEnabled(true);
	}
	public void filterPhotoClick(View view){
		/*
		 * tu sie zrobi przejscie do nastepnej aktywnosci przez core z przeslaniem gdzies 
		 * zdjecia dalej
		 */
	}
}