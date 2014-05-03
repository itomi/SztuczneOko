package pl.pwr.sztuczneoko.imageProcessor;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageFilter {
	private Mat mRgba;
	private Mat mGray;
	private Mat mGaussian;
	private Mat mThreshold;
	private Mat mIntermediateMat;
	private Mat mMat;
	private Bitmap mPicture;
	private Bitmap mBitmap;
	private Bitmap mRet;
	
	

	public ImageFilter(Bitmap bitmap) {
		super();			
		mPicture = bitmap;
		mBitmap = bitmap;
		mMat = new Mat();
		mGray = new Mat();
		//mBitmap = new Bitmap();
	}
	
	public void setImage(Bitmap image) {
        this.mPicture = image;
    }
	
	public Mat convToMat(Bitmap bitmap) {
	    Utils.bitmapToMat(bitmap, mMat);
	    return mMat;
	}
	
	public Bitmap convToBitmap(Mat mat) {
	    Utils.matToBitmap(mat, mBitmap);
	    return mBitmap;
	}
	
    public Bitmap rgbFilter(Bitmap bitmap) {
        // input frame has RGBA scale format
        //mRgba = convToMat(bitmap);
        return mRet;
    }
    
    public Bitmap grayFilter(Bitmap bitmap) {
        // input frame has Gray forma
    	//cvtColor(*image, character, CV_BGR2GRAY);t
    	mRgba = convToMat(bitmap);
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        mRet = convToBitmap(mGray);
        return mRet;
    }
            
    public Bitmap cannyFilter(Bitmap bitmap) {
        // input frame has gray scale format
    	mRgba = convToMat(bitmap);
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_GRAY2RGBA, 4);
        Imgproc.Canny(mGray, mIntermediateMat, 80, 100);
        Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap thresholdFilter(Bitmap bitmap) {
        // input frame has threshold format
    	mRgba = convToMat(bitmap);
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_GRAY2RGBA, 4);
        Imgproc.GaussianBlur(mGray, mGaussian, new org.opencv.core.Size(3, 3), 0, 0);
        Imgproc.adaptiveThreshold(mGaussian, mThreshold, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);
        mRet = convToBitmap(mThreshold);
        return mRet;
    }
    
}
