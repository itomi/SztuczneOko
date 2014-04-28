package pl.pwr.sztuczneoko.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import pl.pwr.sztuczneoko.core.ImageItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class ImageLoader extends Thread {

	private ImageItem imageItem;
	
	public interface ImageLoadListener {

		void handleImageLoaded(ViewSwitcher aViewSwitcher, ImageView aImageView, ImageItem imageItem);
	}
	private HashSet imgSet = new HashSet<String>();
	private static final String TAG = ImageLoader.class.getSimpleName();
	ImageLoadListener mListener = null;
	private Handler handler;

	ImageLoader(ImageLoadListener lListener){
		mListener = lListener;
	}

	@Override
	public void run() {
		try {
			Looper.prepare();
			handler = new Handler();
			Looper.loop();

		} catch (Throwable t) {
			Log.e(TAG, "ImageLoader halted due to a error: ", t);
		} 
	}

	public synchronized void stopThread() {
		handler.post(new Runnable() {
			public void run() {
				Looper.myLooper().quit();
			}
		});
	}

	public synchronized void queueImageLoad(final String aPath,	
			final ImageView aImageView,	final ViewSwitcher aViewSwitcher,final ArrayList data) {

		handler.post(new Runnable() {
			public void run() {
				try {
					if(!imgSet.contains(aPath))
					synchronized (aImageView){
						BitmapFactory.Options lOptions = new BitmapFactory.Options();
						lOptions.inSampleSize = 1;
						Bitmap lBitmap = decodeBitmapFromFile(aPath, 100, 100);

						Log.d("imgLoading :", aPath);
						imageItem = new ImageItem(lBitmap,new File(aPath).getName());

						data.add(imageItem);
						signalUI(aViewSwitcher, aImageView, imageItem);
						imgSet.add(aPath);
					}
				} 
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	private void signalUI(ViewSwitcher aViewSwitcher,ImageView aImageView,ImageItem imageItem){
		if(mListener != null){
			mListener.handleImageLoaded(aViewSwitcher, aImageView, imageItem);
		}
	}
	
	
	public static int calculateInSampleSize(
    	BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	        
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }	
	    return inSampleSize;
    }
    public static Bitmap decodeBitmapFromFile(String file, 
    		int reqWidth, int reqHeight) {
    	
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }
}