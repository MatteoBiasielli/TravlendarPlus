package biasiellicapodifatta.travlendar.layouts;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.SimpleFormatter;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.adapters.ActListAdapter;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.activities.Activity;
import biasiellicapodifatta.travlendar.data.activities.Break;
import biasiellicapodifatta.travlendar.data.activities.Calendar;
import biasiellicapodifatta.travlendar.data.activities.FixedActivity;
import biasiellicapodifatta.travlendar.data.user.User;

/**
 * Created by Emilio on 29/11/2017.
 */

public class CalendarTab extends Fragment implements DatePickerDialog.OnDateSetListener{
    User myUser = Data.getUser();

    ImageButton mDateButton;
    TextView mDateView;
    FloatingActionButton mAddButton;
    ListView mActivityList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @TargetApi(24)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View calendarView = inflater.inflate(R.layout.calendar_tab_layout, container, false);

        mDateButton = (ImageButton) calendarView.findViewById(R.id.date_picker_button);
        mDateView = (TextView) calendarView.findViewById(R.id.date_textView);
        mAddButton = (FloatingActionButton) calendarView.findViewById(R.id.new_act_button);
        mActivityList = (ListView) calendarView.findViewById(R.id.activities_list);

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        String time = fm.format(System.currentTimeMillis());
        String[] fields = time.split("-", 3);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(), this, Integer.parseInt(fields[0]), Integer.parseInt(fields[1])-1, Integer.parseInt(fields[2]));

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewActActivity.class);
                startActivity(intent);
            }
        });

        return calendarView;
    }

    @TargetApi(24)
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = myUser.getCalendar();
        ArrayList<FixedActivity> myFixed = calendar.getFixedActivities();
        ArrayList<Break> myBreaks = calendar.getBreaks();
        ArrayList<Activity> myActivities = new ArrayList<>();
        myActivities.addAll(myFixed);
        myActivities.addAll(myBreaks);
        myActivities.sort(null);
        int j;
/*
        ActListAdapter adapter = new ActListAdapter(getContext(), R.layout.actlist_layout, myActivities);
        mActivityList.setAdapter(adapter);

        for(j = 0; j < myActivities.size(); j++){
            adapter.getView(j, null, null);
        }
        */

    }

    private void addToListView(Activity act){

    }

    private void clearListView(){

    }
}
