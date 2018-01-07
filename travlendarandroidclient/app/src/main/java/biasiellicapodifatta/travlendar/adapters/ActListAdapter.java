package biasiellicapodifatta.travlendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.activities.Activity;

/**
 * Created by Emilio on 02/01/2018.
 */

/**
 * The objective of this class is to inject the given Activity objects into the ListView of the CalendarTab class.
 */
public class ActListAdapter extends ArrayAdapter<Activity>{

    public ActListAdapter(Context context, int resource, List<Activity> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.actlist_layout, null);
        }

        Activity act = getItem(position);

        if (act != null) {
            TextView actStartDate = (TextView) v.findViewById(R.id.act_time);
            TextView actLabel = (TextView) v.findViewById(R.id.act_label);

            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");

            if (actStartDate != null) {
                actStartDate.setText(localDateFormat.format(act.getStartDate()) + " -> " + localDateFormat.format(act.getEndDate()));
                actStartDate.setTextColor(getContext().getResources().getColor(R.color.holo_orange, null));
            }

            if (actLabel != null) {
                actLabel.setText(act.getLabel());
                actLabel.setTextColor(getContext().getResources().getColor(R.color.holo_orange, null));
            }
        }

        return v;
    }
}
