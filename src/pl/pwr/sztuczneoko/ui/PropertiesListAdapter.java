package pl.pwr.sztuczneoko.ui;
import java.util.ArrayList;

import pl.pwr.sztuczneoko.core.ExternDevice;
import pl.pwr.sztuczneoko.core.Property;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PropertiesListAdapter extends ArrayAdapter<Property>{

	private final Context context;
    private final ArrayList<Property> itemsTexts;
    
    private static class ViewHolder {
        TextView name;
        TextView status;
    }
    
    public PropertiesListAdapter(Context context, ArrayList<Property> itemsTexts) {
    
        super(context, R.layout.row_with_desc, itemsTexts);
        this.context = context;
        this.itemsTexts = itemsTexts;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Property p = getItem(position);                
        View rowView = inflater.inflate(R.layout.row_with_desc, parent, false);        
        ViewHolder viewHolder = new ViewHolder(); 
        viewHolder.name = (TextView) rowView.findViewById(R.id.label);
        viewHolder.status = (TextView) rowView.findViewById(R.id.label2);
        
        viewHolder.name.setText(getContext().getResources().getIdentifier(
        		p.getName(), "string", getContext().getPackageName()));
        
        viewHolder.name.setContentDescription(
        		getContext().getText(
        				getContext().getResources().getIdentifier(
        						p.getName()+"Desc","string", getContext().getPackageName())));
        
        if(p.isState()){
        	viewHolder.status.setText(R.string.onProp);
        	viewHolder.status.setContentDescription(getContext().getText(R.string.onPropDesc));
        }
        else{
        	viewHolder.status.setText(R.string.offProp);
        	viewHolder.status.setContentDescription(getContext().getText(R.string.offPropDesc));
        }        
           
        return rowView;
    }
    
}
