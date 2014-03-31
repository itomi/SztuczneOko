package pl.pwr.sztuczneoko.ui;

import java.util.ArrayList;

import pl.pwr.sztuczneoko.core.ImageItem;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class GalleryGridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();
 
    public GalleryGridViewAdapter(Context context, int layoutResourceId,
            ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
 
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
 
        final ImageItem item = (ImageItem) data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        row.setId(position);
        row.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("select", item.getTitle());
                ((GalleryActivity)context).setSelectedImg(item,v);
                //v.setBackgroundColor(context.getResources().getColor(R.color.Black));                
                v.setSelected(true);                
            }
        });
        return row;
    }
 
    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}