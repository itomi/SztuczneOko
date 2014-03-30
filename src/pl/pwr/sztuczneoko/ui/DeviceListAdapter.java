package pl.pwr.sztuczneoko.ui;
import java.util.ArrayList;

import pl.pwr.sztuczneoko.core.ExternDevice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeviceListAdapter extends ArrayAdapter<ExternDevice>{

	private final Context context;
    private final ArrayList<ExternDevice> itemsTexts;
    
    private static class ViewHolder {
        TextView name;
        TextView status;
    }
    
    public DeviceListAdapter(Context context, ArrayList<ExternDevice> itemsTexts) {
    
        super(context, R.layout.row_with_desc, itemsTexts);
        this.context = context;
        this.itemsTexts = itemsTexts;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ExternDevice ed = getItem(position);        
        View rowView = inflater.inflate(R.layout.row_with_desc, parent, false);        
        ViewHolder viewHolder = new ViewHolder(); 
        viewHolder.name = (TextView) rowView.findViewById(R.id.label);
        viewHolder.status = (TextView) rowView.findViewById(R.id.label2);

        viewHolder.name.setText(ed.getName());
           
        if(ed.isConnected())viewHolder.status.setText("połączono");
        else {
        	viewHolder.status.setText("");
		}
           
        return rowView;
    }
}
