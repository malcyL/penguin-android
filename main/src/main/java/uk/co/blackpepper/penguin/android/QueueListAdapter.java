package uk.co.blackpepper.penguin.android;

import java.util.List;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.Queue;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QueueListAdapter extends ArrayAdapter<Queue> {

    private final LayoutInflater inflater;

	public QueueListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<Queue> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }
	
    @Override 
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.queue_list_item, parent, false);
        } else {
            view = convertView;
        }

        Queue item = getItem(position);
        ((TextView)view.findViewById(R.id.name)).setText(item.getName());

        return view;
    }
	
}
