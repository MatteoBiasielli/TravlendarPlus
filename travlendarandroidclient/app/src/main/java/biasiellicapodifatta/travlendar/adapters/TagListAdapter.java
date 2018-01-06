package biasiellicapodifatta.travlendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.user.FavouritePosition;

/**
 * Created by Emilio on 06/01/2018.
 */

/**
 * The objective of this class is to inject the given FavouritePosition objects managed into the SettingsMenu class.
 */
public class TagListAdapter extends ArrayAdapter<FavouritePosition>{
    public TagListAdapter(Context context, int resource, List<FavouritePosition> items) {
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

        FavouritePosition fp = getItem(position);

        if (fp != null) {
            TextView fpTag = (TextView) v.findViewById(R.id.act_time);
            TextView fpAddress = (TextView) v.findViewById(R.id.act_label);

            if (fpTag != null) {
                fpTag.setText(fp.getTag());
                fpTag.setTextColor(getContext().getResources().getColor(R.color.holo_orange));
            }

            if (fpAddress != null) {
                fpAddress.setText(fp.getAddress());
                fpAddress.setTextColor(getContext().getResources().getColor(R.color.holo_orange));
            }
        }

        return v;
    }
}
