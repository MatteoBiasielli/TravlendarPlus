package biasiellicapodifatta.travlendar.layouts;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.adapters.ActListAdapter;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.activities.Activity;
import biasiellicapodifatta.travlendar.data.activities.ActivityStatus;
import biasiellicapodifatta.travlendar.data.activities.Break;
import biasiellicapodifatta.travlendar.data.activities.Calendar;
import biasiellicapodifatta.travlendar.data.activities.FixedActivity;
import biasiellicapodifatta.travlendar.data.user.User;

/**
 * Created by Emilio on 29/11/2017.
 */

public class CalendarTab extends Fragment implements DatePickerDialog.OnDateSetListener{
    User myUser = Data.getUser();

    ArrayList<Activity> actSet;
    ArrayList<Activity> selSet;

    //UI references
    ImageButton mDateButton;
    TableRow mDateRow;
    TextView mDateView;
    TextView mDescription;
    ListView mActivityList;
    FloatingActionButton mAddButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        actSet = new ArrayList<>();
        selSet = new ArrayList<>();
    }

    @TargetApi(24)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View calendarView = inflater.inflate(R.layout.calendar_tab_layout, container, false);

        mDateRow = (TableRow) calendarView.findViewById(R.id.date_row);
        mDateButton = (ImageButton) calendarView.findViewById(R.id.date_picker_button);
        mDateView = (TextView) calendarView.findViewById(R.id.date_textView);
        mDescription = (TextView) calendarView.findViewById(R.id.description);
        mAddButton = (FloatingActionButton) calendarView.findViewById(R.id.new_act_button);
        mActivityList = (ListView) calendarView.findViewById(R.id.activities_list);

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        final String date = fm.format(System.currentTimeMillis());
        String[] fields = date.split("-", 3);

        mDateView.setText(fields[2] + " " + getMonthFor(Integer.parseInt(fields[1])-1) + " " + fields[0]);
        mDateRow.setGravity(Gravity.CENTER | Gravity.BOTTOM);

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
                intent.putExtra("currDate", date);
                startActivity(intent);
            }
        });

        if(myUser.getCalendar() != null) {
            actSet.addAll(myUser.getCalendar().getFixedActivities());
            actSet.addAll(myUser.getCalendar().getBreaks());

            selSet = Activity.getForDate(actSet, Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]));
            if(!selSet.isEmpty()){
                mDescription.setText("Choose an activity to see more details.");
            }
        }

        if(selSet.isEmpty()){
            mDescription.setText("No activity for today. How lazy! :P");
        }

        return calendarView;
    }

    @TargetApi(24)
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if(myUser.getCalendar() != null) {
            selSet = Activity.getForDate(actSet, i, i1, i2);

            if(!selSet.isEmpty()) {
                ActListAdapter adapter = new ActListAdapter(getContext(), R.layout.actlist_layout, selSet);
                mActivityList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mActivityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Activity a = (Activity) adapterView.getItemAtPosition(i);
                        Intent intent = new Intent(getContext(), UpdateActivity.class);
                        intent.putExtra("activityID", a.getKey());
                        startActivity(intent);
                    }
                });
                mDescription.setText("Choose an activity to see more details.");
            }
        }

        if(selSet.isEmpty()){
            ActListAdapter adapter = new ActListAdapter(getContext(), R.layout.actlist_layout, selSet);
            mActivityList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mDescription.setText("No activity for today. How lazy! :P");
        }

        mDateView.setText(i2 + " " + getMonthFor(i1) + " " + i);
        mDateRow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
    }

    private String getMonthFor(int m){
        String month;

        switch(m){
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
            default:
                month = "January";
        }

        return month;
    }
}
