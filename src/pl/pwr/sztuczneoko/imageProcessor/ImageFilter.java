package pl.pwr.sztuczneoko.imageProcessor;

import pl.pwr.sztuczneoko.core.ImageItem;

import org.opencv.android.Utils;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;

public class ImageFilter {
	private Mat mRgba;
	private Mat mGray;
	private Mat mGaussian;
	private Mat mThreshold;
	private Mat mIntermediateMat;
	private Mat mTake;
	private Mat image;
	
	public Mat takePicture(){
			//Mat image;
			
			return mTake;
	}
    public Mat rgbFilter() {
            // input frame has RGBA scale format
        	mRgba = takePicture();
            return mRgba;
    }
    
    public Mat grayFilter() {
            // input frame has Gray format
        	//mRgba = takePistre();
        	Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_GRAY2RGBA, 4);
        	return mGray;
    }
            
    public Mat cannyFilter() {
            // input frame has gray scale format
        	//mRgba = bitmapToMat(inputFrame);
        	Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_GRAY2RGBA, 4);
            Imgproc.Canny(mGray, mIntermediateMat, 80, 100);
            Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
            return mRgba;
    }
    
    public Mat thresholdFilter() {
            // input frame has threshold format
        	//mRgba = bitmapToMat(inputFrame);
        	Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_GRAY2RGBA, 4);
        	Imgproc.GaussianBlur(mGray, mGaussian, new org.opencv.core.Size(3, 3), 0, 0);
        	Imgproc.adaptiveThreshold(mGaussian, mThreshold, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);
            return mThreshold;
    }
}

