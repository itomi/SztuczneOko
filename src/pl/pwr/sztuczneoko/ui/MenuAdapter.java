package pl.pwr.sztuczneoko.ui;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class MenuAdapter extends ArrayAdapter<String>{

	private Context context;
    private String[] itemsTexts;
    private String[] itemsContentDesc;
    private final ViewSwitch vs;
    
    public MenuAdapter(Context context, String[] itemsTexts, String[] itemsContentDesc) {    	    	    	
        super(context, R.layout.row, itemsTexts);
        vs = new getViewStandard();
        initAdapter(context, itemsTexts, itemsContentDesc);    
    }
    
    public MenuAdapter(Context context, String[] itemsTexts, String[] itemsContentDesc,int res) {    	    	    	
        super(context,res, itemsTexts);
        vs = new getViewForPropAct();
        initAdapter(context, itemsTexts, itemsContentDesc);
    }
    
    private void initAdapter(Context context, String[] itemsTexts, String[] itemsContentDesc) {
    	this.context = context;
        this.itemsTexts = itemsTexts;
        this.itemsContentDesc = itemsContentDesc;
	}
    
    interface ViewSwitch{
    	public View getView(int position, View convertView, ViewGroup parent);
    }
    
    class getViewStandard implements ViewSwitch{
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = (LayoutInflater) context
    	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	        
    	        View rowView = inflater.inflate(R.layout.row, parent, false);        
    	        TextView labelView = (TextView) rowView.findViewById(R.id.label);        
    	        labelView.setText(itemsTexts[position]);        
    	        labelView.setContentDescription(itemsContentDesc[position]);
    	        return rowView;
    	}
    }

    class getViewForPropAct implements ViewSwitch{
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = (LayoutInflater) context
    	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	        
    	        View rowView = inflater.inflate(R.layout.row, parent, false);        
    	        TextView labelView = (TextView) rowView.findViewById(R.id.label);        
    	        labelView.setText(getContext().getResources().getIdentifier(
    	        		itemsTexts[position], "string", getContext().getPackageName()));        
    	        labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    	        labelView.setContentDescription(getContext().getText(
    	        		getContext().getResources().getIdentifier(
    	        				itemsContentDesc[position], "string", getContext().getPackageName())));
    	        return rowView;
    	}
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	return vs.getView(position, convertView, parent);
    }
}


