package pl.pwr.sztuczneoko.imageProcessor;

//package pl.pwr.sztuczneoko.imageProcessor;

import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
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
	private Bitmap mPicture;
	private Bitmap mBitmap;
	private Bitmap mRet;
	
	

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
            
    public Bitmap cannyFilter(/*int min, int max*/) { //(min, max) (70,90),(30,50),(50,70),(90,110)
        // input frame has gray scale format
    	mRgba = convToMat();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.Canny(mGray, mTmp, 90, 110/*min, max*/);
        Imgproc.cvtColor(mTmp, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap thresholdFilter(/*int blockSize, int color*/) { //blockSize 3,5,7; color 1 lub 0
        // input frame has threshold format
    	mRgba = convToMat();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.GaussianBlur(mGray, mTmp, new org.opencv.core.Size(3, 3), 0, 0);
        Imgproc.adaptiveThreshold(mTmp, mRgba, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY/*color*/, /*blockSize*/3, 4);
        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap binaryFilter(/*int bin, int color*/) { //bin -1, 20, 120, 220; color 1 lub 0
        // input frame has threshold format
    	mRgba = convToMat();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.GaussianBlur(mGray, mTmp, new org.opencv.core.Size(3, 3), 0, 0);
        Imgproc.threshold(mTmp, mRgba, 220, 255, Imgproc.THRESH_BINARY_INV);
        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap blur(/*int bl*/){ //bl 3,5,7
    	mRgba = convToMat();
        Imgproc.blur(mRgba, mTmp, new Size(3, 3));
        mRet = convToBitmap(mTmp);
        return mRet;
    }
    
    public Bitmap sobel(/*double minVal=-799, double maxVal=602*/){ //(minVal, maxVal) (-100, 100),(-200,200), (-400,400)
    	double minVal=-100, maxVal=100;
    	mRgba = convToMat();
    	Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.Sobel(mGray, mTmp, CvType.CV_32F, 1, 0);
        mTmp.convertTo(mRgba, CvType.CV_8U, 255.0/(maxVal - minVal), -minVal * 255.0/(maxVal - minVal));

        mRet = convToBitmap(mRgba);
        return mRet;
    }
    
    public Bitmap cropp(){ //To jeszcze nie jest skonczone
    	
    	mRgba = convToMat();
    	
    	Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
    	//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // reading image 
        //Mat image = Highgui.imread(".\\testing2.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        // clone the image 
        mTmp = mGray.clone();
        // thresholding the image to make a binary image
        //Imgproc.threshold(image, image, 100, 128, Imgproc.THRESH_BINARY_INV);
    	Imgproc.threshold(mGray, mTmp, 100, 128, Imgproc.THRESH_BINARY_INV);
        // find the center of the image
        double[] centers = {(double)mTmp.width()/2, (double)mTmp.height()/2};
        Point image_center = new Point(centers);

        // finding the contours
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mTmp, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // finding best bounding rectangle for a contour whose distance is closer to the image center that other ones
        double d_min = Double.MAX_VALUE;
        Rect rect_min = new Rect();
        for (MatOfPoint contour : contours) {
            Rect rec = Imgproc.boundingRect(contour);
            // find the best candidates
            if (rec.height > mTmp.height()/2 & rec.width > mTmp.width()/2)            
                continue;
            Point pt1 = new Point((double)rec.x, (double)rec.y);
            //Point center = new Point(rec.x+(double)(rec.width)/2, rec.y + (double)(rec.height)/2);
            double d = Math.sqrt(Math.pow((double)(pt1.x-image_center.x),2) + Math.pow((double)(pt1.y -image_center.y), 2));            
            if (d < d_min)
            {
                d_min = d;
                rect_min = rec;
            }                   
        }
        // slicing the image for result region
        int pad = 5;        
        rect_min.x = rect_min.x - pad;
        rect_min.y = rect_min.y - pad;

        rect_min.width = rect_min.width + 2*pad;
        rect_min.height = rect_min.height + 2*pad;

        mGray = mTmp.submat(rect_min);     
        //Highgui.imwrite("result.png", result);
    	
    	mRet = convToBitmap(mGray);
        return mRet;
    }
}
