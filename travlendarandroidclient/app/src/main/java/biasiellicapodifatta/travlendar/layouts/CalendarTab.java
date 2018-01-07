package biasiellicapodifatta.travlendar.layouts;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.adapters.ActListAdapter;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.activities.Activity;
import biasiellicapodifatta.travlendar.data.user.User;

/**
 * Created by Emilio on 29/11/2017.
 */

/**
 * This class represents a tab in which the user can check his activities and add/update/delete them.
 */
public class CalendarTab extends Fragment implements DatePickerDialog.OnDateSetListener{
    User myUser = Data.getUser();

    ArrayList<Activity> actSet;
    ArrayList<Activity> selSet;

    // UI references.
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

        // Get UI references.
        mDateRow = (TableRow) calendarView.findViewById(R.id.date_row);
        mDateButton = (ImageButton) calendarView.findViewById(R.id.date_picker_button);
        mDateView = (TextView) calendarView.findViewById(R.id.date_textView);
        mDescription = (TextView) calendarView.findViewById(R.id.description);
        mAddButton = (FloatingActionButton) calendarView.findViewById(R.id.new_act_button);
        mActivityList = (ListView) calendarView.findViewById(R.id.activities_list);

        // Get and format the current date.
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        final String date = fm.format(System.currentTimeMillis());
        String[] fields = date.split("-", 3);

        // Set the current date into the DatePickerDialog.
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(), this, Integer.parseInt(fields[0]), Integer.parseInt(fields[1])-1, Integer.parseInt(fields[2]));

        // Set the buttons' listeners.
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the "create new activity" activity.
                Intent intent = new Intent(getActivity(), NewActActivity.class);
                intent.putExtra("currDate", date);
                startActivity(intent);
            }
        });

        // Get the activities for the current date.
        if(myUser.getCalendar() != null) {
            actSet.addAll(myUser.getCalendar().getFixedActivities());
            actSet.addAll(myUser.getCalendar().getBreaks());
            selSet = Activity.getForDate(actSet, Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]));

            // If there's any activity to show.
            if(!selSet.isEmpty()) {
                setItemsListeners();
            }
        }

        // Set the header text properly.
        setHeaderText();

        // Fill the ListView.
        fillActivitiesList(selSet);

        // Set the current date into the ListView.
        mDateView.setText(fields[2] + " " + getMonthFor(Integer.parseInt(fields[1])-1) + " " + fields[0]);

        return calendarView;
    }

    /**
     * Loads the activities corresponding to the date set into the ListView.
     * @param datePicker The datepicker which picks the date.
     * @param i The number corresponding to the year.
     * @param i1 The number corresponding to the month.
     * @param i2 The number corresponding to the day.
     */
    @TargetApi(24)
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        selSet = new ArrayList<>();

        // Get the activities for the selected date.
        if(myUser.getCalendar() != null) {
            selSet = Activity.getForDate(actSet, i, i1, i2);

            // If there's any activity to show.
            if(!selSet.isEmpty()) {
                setItemsListeners();
            }
        }

        // Set the text of the
        setHeaderText();

        // Fill the ListView.
        fillActivitiesList(selSet);

        //Set the date into the TextView.
        mDateView.setText(i2 + " " + getMonthFor(i1) + " " + i);
    }

    private void setHeaderText(){
        if(selSet.isEmpty()){
            mDescription.setText("No activity for today. How lazy! :P");
        }
        else{
            mDescription.setText("Choose an activity to see more details.");
        }
    }

    private void setItemsListeners(){
        mActivityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Activity a = (Activity) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), UpdateActivity.class);
                intent.putExtra("activityID", a.getKey());
                startActivity(intent);
            }
        });
    }

    /**
     * Fills the ListView with the given activities.
     * @param acts The activities to show into the ListView.
     */
    private void fillActivitiesList(ArrayList<Activity> acts){
        ActListAdapter adapter = new ActListAdapter(getContext(), R.layout.actlist_layout, acts);
        mActivityList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * An utility method to convert a month number to its corresponding name.
     * @param m The number to convert into a month (the range is 0-11)
     * @return The String corresponding to the given number, if available; the String "January", otherwise.
     */
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
