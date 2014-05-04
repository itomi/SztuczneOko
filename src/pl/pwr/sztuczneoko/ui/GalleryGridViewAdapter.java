package pl.pwr.sztuczneoko.ui;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;

import pl.pwr.sztuczneoko.core.ImageItem;
import pl.pwr.sztuczneoko.ui.ImageLoader.ImageLoadListener;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewSwitcher;


public class GalleryGridViewAdapter extends ArrayAdapter implements ImageLoadListener{
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();
    private ArrayList<String> fileNames = new ArrayList<String>();
    private HashSet imgSet;
    private static final int IMAGEVIEWINDEX = 1;
    private static final int PROGRESSBARINDEX = 0;
	private Handler mHandler;
	private ImageLoader mImageLoader = null;
	private File mDirectory;
	private View row;
    private ViewHolder holder = null;
    private int selectedId;
    public GalleryGridViewAdapter(Context context, int layoutResourceId,
            ArrayList data,String mPath) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        mDirectory = new File(mPath);
        imgSet = new HashSet<ImageItem>();
		mImageLoader = new ImageLoader(this);
		mImageLoader.start();
		mHandler = new Handler();
        
    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        row = convertView;
        holder = null;
        if(mDirectory.listFiles().length==0)Log.d("gallery", "empty gallery");
		String lPath = (String)getItem(position);

		if (row == null) {			
			 LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.switcher = (ViewSwitcher) row.findViewById(R.id.image);
            row.setTag(holder);
            
			ProgressBar lProgress = new ProgressBar(context);
			lProgress.setLayoutParams(new ViewSwitcher.LayoutParams(80, 80));
			holder.switcher.addView(lProgress);
			ImageView lImage = new ImageView(context);;
			lImage.setLayoutParams(new ViewSwitcher.LayoutParams(100, 100));

			holder.switcher.addView(lImage);
			
	        row.setId(position);
				
		} else {
			holder = (ViewHolder) row.getTag();
		}
		if (holder == null || 
			!holder.imageTitle.equals(lPath)) {	
			holder.imageTitle.setText(new File(lPath).getName());
			if(!fileNames.contains(lPath))fileNames.add(lPath);
			ImageView lImageView = (ImageView) holder.switcher.getChildAt(1);
			holder.switcher.setDisplayedChild(PROGRESSBARINDEX);
			if(data.size()<position+1){
				mImageLoader.queueImageLoad(lPath, lImageView, holder.switcher, data);
			}
			else{
				mImageLoader.mListener.handleImageLoaded(holder.switcher, lImageView, (ImageItem) data.get(position));				
			}
		}
		final int id = position;
		
		row.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(id>data.size()-1){
            		Collections.sort(fileNames);
                    Log.d("select", fileNames.get(id)+ " at row : "+ id );
                    Bitmap lBitmap = ImageLoader.decodeBitmapFromFile(fileNames.get(id), 100, 100);

                    ((GalleryActivity)context).setSelectedImg(
                    		new ImageItem(lBitmap,new File(fileNames.get(id)).getName()),v,id);       
                    v.setSelected(true);            		
            	}
            	else{
	                Log.d("select",((ImageItem)data.get(id)).getTitle() + " at row : "+ id );
	                ((GalleryActivity)context).setSelectedImg((ImageItem)data.get(id),v,id);       
	                v.setSelected(true);	                
            	}
            }
        });	
		return row;

    }
    
    @Override
	protected void finalize() throws Throwable {
		super.finalize();
		mImageLoader.stopThread();
	}
    static class ViewHolder {
        TextView imageTitle;
        ViewSwitcher switcher;
    }

	@Override
	public synchronized void handleImageLoaded(final ViewSwitcher aViewSwitcher,
			final ImageView aImageView, final ImageItem imageItem) {
		
		mHandler.post(new Runnable() {
			public void run() {

				aImageView.setImageBitmap(imageItem.getImage());
				aViewSwitcher.setDisplayedChild(IMAGEVIEWINDEX);	
				((GalleryActivity)context).loadLastView();
			}
		});

	}
	public int getCount() {
		return mDirectory.listFiles().length;
	}

	public Object getItem(int aPosition) {
		String lPath = null;
		TreeSet<File> lFiles = new TreeSet<File>();
		Collections.addAll(lFiles,mDirectory.listFiles());
		
		if(aPosition < lFiles.size()){
			lPath = ((File)lFiles.toArray()[aPosition]).getAbsolutePath();
		}

		return lPath;
	}
}
