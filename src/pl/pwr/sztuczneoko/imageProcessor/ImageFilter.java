package pl.pwr.sztuczneoko.imageProcessor;

//package pl.pwr.sztuczneoko.imageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
//import org.opencv.samples.imagemanipulations.ImageManipulationsActivity;




import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class ImageFilter {
	private Mat mRgba;
	private Mat mGray;
	private Mat mTmp;
	private Mat mMat;
	private Mat hierarchy;
	private Bitmap mPicture;
	private Bitmap mBitmap;
	private Bitmap mRet;
	          
	ArrayList<MatOfPoint> contours;
	List<MatOfPoint> contours3 = new ArrayList<MatOfPoint>();
	Mat hierarchy2 = new Mat();

	public ImageFilter(Bitmap bitmap) {
		super();			
		mPicture = bitmap;
		mBitmap = bitmap;
		mMat = new Mat();
		mGray = new Mat();
		mTmp = new Mat();
		//mBitmap = new Bitmap();
	}
	
	//public void setImage(Bitmap image) {
	public void setImage() {
        this.mPicture = mBitmap;
    }
	
	public Mat convToMat() {
	    Utils.bitmapToMat(mBitmap, mMat);
	    return mMat;
	}
	
	public Bitmap convToBitmap(Mat mat) {
	    Utils.matToBitmap(mat, mBitmap);
	    return mBitmap;
	}
	
    public Bitmap rgbFilter() {
        // input frame has RGBA scale format
        //mRgba = convToMat(bitmap);
        return mRet;
    }
    
    public Bitmap grayFilter() {
        // input frame has Gray forma
    	//cvtColor(*image, character, CV_BGR2GRAY);t
    	mRgba = convToMat();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        mRet = convToBitmap(mGray);
        return mRet;
    }
            
    public Bitmap cannyFilter(int min, int max) { //(min, max) (70,90),(30,50),(50,70),(90,110)
        // input frame has gray scale format
    	mRgba = convToMat();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.Canny(mGray, mTmp, min, max);
        Imgproc.cvtColor(mTmp, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap thresholdFilter(int blockSize, int color) { //blockSize 3,5,7; color 1 lub 0
        // input frame has threshold format
    	mRgba = convToMat();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.GaussianBlur(mGray, mTmp, new org.opencv.core.Size(3, 3), 0, 0);
        Imgproc.adaptiveThreshold(mTmp, mRgba, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, color, blockSize, 4);
        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap binaryFilter(int bin, int color) { //bin -1, 20, 120, 220; color 1 lub 0
        // input frame has threshold format
    	mRgba = convToMat();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.GaussianBlur(mGray, mTmp, new org.opencv.core.Size(3, 3), 0, 0);
        if (bin == -1)
        Imgproc.threshold(mTmp, mRgba, bin, 255, color | Imgproc.THRESH_OTSU);
        else
        Imgproc.threshold(mTmp, mRgba, bin, 255, color);
        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap blur(int bl){ //bl 3,5,7
    	mRgba = convToMat();
        Imgproc.blur(mRgba, mTmp, new Size(bl, bl));
        mRet = convToBitmap(mTmp);
        return mRet;
    }
    
    public Bitmap sobel(double minVal, double maxVal){ //(minVal, maxVal) (-100, 100),(-200,200), (-400,400)
    	mRgba = convToMat();
    	Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.Sobel(mGray, mTmp, CvType.CV_32F, 1, 0);
        mTmp.convertTo(mRgba, CvType.CV_8U, 255.0/(maxVal - minVal), -minVal * 255.0/(maxVal - minVal));

        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap something(int resize){
    	double maxArea=0;
        MatOfPoint maxiArea=null;
    	mRgba = convToMat();
    	mGray = new Mat(mRgba.size(), Core.DEPTH_MASK_8U);
        mTmp = new Mat(mRgba.size(), Core.DEPTH_MASK_8U);
        mMat = new Mat(mRgba.size(), Core.DEPTH_MASK_ALL);
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(mGray, mTmp, new Size(5,5), 0);
        Imgproc.adaptiveThreshold(mTmp, mMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
        Imgproc.findContours(mMat, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        for(int i=0; i< contours.size();i++){
        	if (Imgproc.contourArea(contours.get(i)) > maxArea )
        	{
        		maxArea = Imgproc.contourArea(contours.get(i));
        		maxiArea = contours.get(i);
        	}
        }
        Rect rect = Imgproc.boundingRect(maxiArea);
      //Core.rectangle(mRgba, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
        int control = rect.height - rect.width;
        Mat mZoomWindow;
        if (resize == 1)
        {
        	int addsub = control / 2;
	        if (control>=0)
	        {
	        	if (rect.x - addsub < 0)
	        		mZoomWindow = mRgba.submat(rect.y , rect.y + rect.height, 0, rect.x + rect.width + addsub);
	        	else if (rect.x + rect.width + addsub > mRgba.width())
		        	mZoomWindow = mRgba.submat(rect.y , rect.y + rect.height, rect.x - addsub, mRgba.width());
	        	else
		        	mZoomWindow = mRgba.submat(rect.y , rect.y + rect.height, rect.x - addsub, rect.x + rect.width + addsub);
	        }
	        else
	        {
	        	if (rect.y - addsub < 0)
	        		mZoomWindow = mRgba.submat(0 , rect.y + rect.height, rect.x, rect.x + rect.width + addsub);
	        	else if (rect.x + rect.width + addsub > mRgba.cols())
		        	mZoomWindow = mRgba.submat(rect.y , mRgba.cols(), rect.x - addsub, mRgba.width());
	        	else
		        	mZoomWindow = mRgba.submat(rect.y , rect.y + rect.height, rect.x - addsub, rect.x + rect.width + addsub);
	        }
        }
        else 
        	mZoomWindow = mRgba.submat(rect.y , rect.y + rect.height, rect.x, rect.x + rect.width);
        Imgproc.resize(mZoomWindow, mTmp, mRgba.size());
        Imgproc.Canny(mTmp, mGray, 70, 90);
        mRet = convToBitmap(mGray);
    	return mRet;
    }

    /*
    public Bitmap cropp(){ 
       
     	mRgba = convToMat();
		Imgproc.cvtColor( mRgba, mTmp, Imgproc.COLOR_RGB2HSV, 3 );
		Scalar lowerThreshold = new Scalar ( 0, 0, 0 ); // Blue color  lower hsv values
		Scalar upperThreshold = new Scalar ( 100, 100, 100 ); // Blue color  higher hsv values
		Core.inRange ( mTmp, lowerThreshold , upperThreshold, mTmp );
		Imgproc.dilate ( mTmp, mTmp, new Mat() );
		Scalar coloreded = new Scalar ( 100, 255, 255 );
		Imgproc.findContours ( mTmp, contours3, hierarchy2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE );
		mTmp = mRgba;
		 for ( int contourIdx=0; contourIdx < contours3.size(); contourIdx++ )	//pêtla do wywietlania
		  {			
		    // if( contours2.get(contourIdx).size()>100)  // Minimum size allowed for consideration
		   //  { 
			 //zamiast contourIdx mo¿na daæ -1 to bêdzie rysowa³o wszystkie kontury
		         Imgproc.drawContours ( mTmp, contours3, contourIdx, coloreded, 1);	
		    // }
		  }
		
		mRet = convToBitmap(mTmp);
		return mRet;
    }*/
}