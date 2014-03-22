package pl.pwr.sztuczneoko.ui;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<String>{

	private final Context context;
    private final String[] itemsTexts;
    private final String[] itemsContentDesc;
    
    public MenuAdapter(Context context, String[] itemsTexts, String[] itemsContentDesc) {

        super(context, R.layout.row, itemsTexts);

        this.context = context;
        this.itemsTexts = itemsTexts;
        this.itemsContentDesc = itemsContentDesc;
    }
    
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
