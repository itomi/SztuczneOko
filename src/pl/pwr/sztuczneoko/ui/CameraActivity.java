package pl.pwr.sztuczneoko.ui;

import pl.pwr.sztuczneoko.camera.CameraPreview;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    
    @Override
	protected void onDestroy() {
    	mCamera.release();
		super.onDestroy();
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        
        // Create an instance of Camera
        mCamera = Camera.open();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        );
    }
}