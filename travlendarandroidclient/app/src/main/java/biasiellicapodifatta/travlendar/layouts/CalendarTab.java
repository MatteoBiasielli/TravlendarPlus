package biasiellicapodifatta.travlendar.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biasiellicapodifatta.travlendar.R;

/**
 * Created by Emilio on 29/11/2017.
 */

public class CalendarTab extends Fragment {
    FloatingActionButton mAddButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View calendarView = inflater.inflate(R.layout.calendar_tab_layout, container, false);
        mAddButton = (FloatingActionButton) calendarView.findViewById(R.id.new_act_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewActActivity.class);
                startActivity(intent);
            }
        });

        return calendarView;
    }
}
