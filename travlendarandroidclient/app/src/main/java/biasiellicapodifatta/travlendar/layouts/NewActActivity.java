package biasiellicapodifatta.travlendar.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.network.NetworkLayer;
import biasiellicapodifatta.travlendar.response.responseaddactivity.ResponseAddActivity;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class NewActActivity extends AppCompatActivity {

    private AddFixedActivity mAddTask = null;

    private int mStartHour;
    private int mStartMin;
    private int mEndHour;
    private int mEndMin;
    private static String[] userTags = new String[Data.getUser().getFavPositions().size()];
    private static String selectedStartTag = "";
    private static String selectedEndTag = "";

    //UI references
    private EditText mActivityNameView;
    private EditText mStartPositionView;
    private static EditText mStartTag;
    private static EditText mEndTag;
    private EditText mEndPositionView;
    private EditText mNotesView;
    private EditText mDuration;

    private EditText mTimeStart;
    private EditText mTimeEnd;

    private View mNewActFormView;
    private View mProgressView;

    private DatePicker mDatePickerStart;
    private DatePicker mDatePickerEnd;

    private Switch mFlexibleSwitch;

    /**
     * Initialize/build all UI components and, if necessary, set listeners on events.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_act);

        for(int i=0; i < Data.getUser().getFavPositions().size(); i++){
            userTags[i] = Data.getUser().getFavPositions().get(i).getTag();
        }

        mActivityNameView = findViewById(R.id.activity_name_form);
        mActivityNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptAddActivity();
                    return true;
                }
                return false;
            }
        });

        mStartPositionView = findViewById(R.id.start_position_form);
        mStartPositionView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptAddActivity();
                    return true;
                }
                return false;
            }
        });

        mEndPositionView = findViewById(R.id.destination_position_form);
        mEndPositionView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptAddActivity();
                    return true;
                }
                return false;
            }
        });

        mNotesView = findViewById(R.id.notes_form);
        mNotesView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptAddActivity();
                    return true;
                }
                return false;
            }
        });

        mDuration = findViewById(R.id.duration_form);

        mTimeStart = findViewById(R.id.selectTimeStart);
        mTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewActActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinutes) {
                        mStartHour = selectedHour;
                        mStartMin = selectedMinutes;
                        mTimeStart.setText(selectedHour + ":" + selectedMinutes);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select time");
                mTimePicker.show();
            }
        });

        mTimeEnd = findViewById(R.id.selectTimeEnd);
        mTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewActActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinutes) {
                        mEndHour = selectedHour;
                        mEndMin = selectedMinutes;
                        mTimeEnd.setText(selectedHour + ":" + selectedMinutes);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select time");
                mTimePicker.show();
            }
        });

        mStartTag = findViewById(R.id.start_tag);
        mStartTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTagList tags = new StartTagList();
                tags.show(getFragmentManager(), "start-tag");
            }
        });

        mEndTag = findViewById(R.id.end_tag);
        mEndTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndTagList tags = new EndTagList();
                tags.show(getFragmentManager(), "end-tag");
            }
        });

        mDatePickerStart = findViewById(R.id.datePicker_start);
        mDatePickerEnd = findViewById(R.id.datePicker_end);

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAddActivity();
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewActActivity.this, MainTabContainer.class);
                startActivity(intent);
            }
        });

        mFlexibleSwitch = (Switch) findViewById(R.id.flex_switch);

        mProgressView = findViewById(R.id.progressBar);
        mNewActFormView = findViewById(R.id.new_act_view);
    }

    /**
     * Method that tries to  add a new activity after checking that parameters are valid
     */
    private void attemptAddActivity(){
        if(mAddTask != null)
            return;

        //reset errors
        mActivityNameView.setError(null);
        mStartPositionView.setError(null);
        mEndPositionView.setError(null);
        mNotesView.setError(null);
        mStartTag.setError(null);
        mEndTag.setError(null);

        String act_name = mActivityNameView.getText().toString();
        String start = mStartPositionView.getText().toString();
        String dest = mEndPositionView.getText().toString();
        String startTag = selectedStartTag;
        String endTag = selectedEndTag;
        String notes = mNotesView.getText().toString();

        String duration;
        if(mFlexibleSwitch.isChecked())
            duration = mDuration.getText().toString();
        else
            duration = "0";

        int start_hour = mStartHour;
        int start_min = 0;
        if(mStartMin <= 12)
            start_min =  0;
        else if(mStartMin > 12 && mStartMin <= 27)
                start_min =  15;
            else if(mStartMin > 27 && mStartMin <= 42)
                    start_min =  30;
                else if(mStartMin > 42 && mStartMin <= 57)
                        start_min =  45;
                    else if(mStartMin > 57) {
                        start_min =  0;
                        start_hour++;
                        }

        Calendar start_calendar = Calendar.getInstance();
        start_calendar.set(mDatePickerStart.getYear(), mDatePickerStart.getMonth(), mDatePickerStart.getDayOfMonth(), start_hour,     start_min, 0);

        int end_hour =  mEndHour;
        int end_min =  0;
        if(mEndMin <= 12)
            end_min =  0;
        else if(mEndMin > 12 && mEndMin <= 27)
                end_min =  15;
            else if(mEndMin > 27 && mEndMin <= 42)
                    end_min =  30;
                else if(mEndMin > 42 && mEndMin <= 57)
                        end_min =  45;
                    else if(mEndMin > 57) {
                        end_min =  0;
                        end_hour++;
                    }

        Calendar end_calendar = Calendar.getInstance();
        end_calendar.set(mDatePickerEnd.getYear(), mDatePickerEnd.getMonth(), mDatePickerEnd.getDayOfMonth(), end_hour, end_min, 0);

        boolean cancel = false;
        View focusView = null;

        if(!isDateValid(start_calendar.getTimeInMillis())){
            mActivityNameView.setError("This activity begins in the past.");
            focusView = mActivityNameView;
            cancel = true;
        }

        if(!isDateValid(end_calendar.getTimeInMillis())){
            mActivityNameView.setError("This activity ends in the past.");
            focusView = mActivityNameView;
            cancel = true;
        }

        if(!isDateConsistent(start_calendar.getTimeInMillis(), end_calendar.getTimeInMillis())){
            mActivityNameView.setError("This activity start date is after its end date.");
            focusView = mActivityNameView;
            cancel = true;
        }


        if(!isNameValid(act_name)){
            mActivityNameView.setError("This is not a valid name for the activity.");
            focusView = mActivityNameView;
            cancel = true;
        }

        if(!isNotesValid(notes)){
            mNotesView.setError("Too long notes field.");
            focusView = mNotesView;
            cancel = true;
        }

        if(cancel){
            //if one or more errors occur, focus the error view on the first error occured
            focusView.requestFocus();
        }else{
            //show a progress bar,
            showProgress(true);
            mAddTask = new AddFixedActivity(Data.getUser().getUsername(), Data.getUser().getPassword(),
                    act_name, notes, start, dest, start_calendar.getTime(), end_calendar.getTime(), mFlexibleSwitch.isChecked(), duration, startTag, endTag);
            // ip address set by the login screen
            mAddTask.execute((Void)null);
        }

    }

    /**
     * Support method to check if the {@param date} is in the past w.r.t. the moment the addition
     * is requested.
     * @return : true if the date is not in the past
     */
    private boolean isDateValid(Long date){
        Date tmp = new Date(date);
        return tmp.after(new Date());
    }

    private boolean isDateConsistent(Long start, Long end){
        Date date1 = new Date(start);
        Date date2 = new Date(end);
        return date2.after(date1);
    }

    /**
     * Support method to check if the {@param name} is valid name, i.e. contains only lower
     * and upper case letters.
     * @return : true if the name contains only letters
     */
    private boolean isNameValid(String name){
        return name.matches("([a-z]|[A-Z])+");
    }

    /**
     * Support method to check if the {@param notes} field is longer than a threshold.
     * @return : true if the notes are not longer than the threshold.
     */
    private boolean isNotesValid(String notes){
        return notes.length() <= 100;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mNewActFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mNewActFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mNewActFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mNewActFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Task/thread launched to handle the background and post execution of the request.
     */
    public class AddFixedActivity extends AsyncTask<Void, Void, ResponseAddActivity>{

        private final String locUsername;
        private final String locPassword;
        private final String locName;
        private final String locNotes;
        private final String locStartPosition;
        private final String locEndPosition;
        private final Date locStartDate;
        private final Date locEndDate;
        private final Boolean locFlexible;
        private final String locDuration;
        private final String locStartTag;
        private final String locEndTag;
        private ResponseAddActivity response;

        AddFixedActivity(String username, String password, String name, String notes, String startpos,
                         String endpos, Date startdate, Date enddate, Boolean flexible, String duration, String startTag, String endTag){
            locUsername = username;
            locPassword = password;
            locName = name;
            locNotes = notes;
            locStartPosition = startpos;
            locEndPosition = endpos;
            locStartDate = startdate;
            locEndDate = enddate;
            locFlexible = flexible;
            locDuration = duration;
            locStartTag = startTag;
            locEndTag = endTag;
        }

        @Override
        protected ResponseAddActivity doInBackground(Void... params){

            try{
                response = NetworkLayer.addActivityRequest(locUsername, locPassword, locName, locNotes,
                        locEndPosition, locEndTag, locStartPosition, locStartTag, locFlexible, locDuration,locStartDate, locEndDate);
            }catch (IOException e){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intent = new Intent(NewActActivity.this, MainTabContainer.class);
                startActivity(intent);
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseAddActivity response){
            mAddTask = null;
            showProgress(false);

            if(response == null){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intentx = new Intent(NewActActivity.this, MainTabContainer.class);
                startActivity(intentx);
                return;
            }

            switch (response.getType()){
                case OK:
                    DialogFragment pop_up = new ActivityAdded();
                    pop_up.show(getFragmentManager(), "added");
                    Intent intent = new Intent(NewActActivity.this, MainTabContainer.class);
                    startActivity(intent);
                    break;
                case OK_ESTIMATED_TIME:
                    DialogFragment pop_up_warning = new AddedWithCondition();
                    pop_up_warning.show(getFragmentManager(), "warning");
                    Intent intent2 = new Intent(NewActActivity.this, MainTabContainer.class);
                    startActivity(intent2);
                    break;
                case ADD_ACTIVITY_LOGIN_ERROR:
                    DialogFragment login_error = new LoginError();
                    login_error.show(getFragmentManager(), "login-error");
                    Intent intent3 = new Intent(NewActActivity.this, LoginActivity.class);
                    startActivity(intent3);
                    break;
                case ADD_ACTIVITY_WRONG_INPUT:
                    DialogFragment wrong_input = new WrongInput();
                    wrong_input.show(getFragmentManager(), "wrong-input");
                    break;
                case ADD_ACTIVITY_CONNECTION_ERROR:
                    DialogFragment conn_error = new ConnectionError();
                    conn_error.show(getFragmentManager(), "conn-error");
                    break;
                case ADD_ACTIVITY_OVERLAPPING:
                    DialogFragment overlap = new Overlap();
                    overlap.show(getFragmentManager(), "overlap");
                    break;
                case ADD_ACTIVITY_PAST:
                    DialogFragment past = new Past();
                    past.show(getFragmentManager(), "past-act");
                    break;
                default:
                    DialogFragment unexp = new UnexpectedError();
                    unexp.show(getFragmentManager(), "unexp-err");
                    Intent intentx = new Intent(NewActActivity.this, MainTabContainer.class);
                    startActivity(intentx);
                    break;
            }
        }

        @Override
        protected void onCancelled(){
            mAddTask = null;
            showProgress(false);
        }
    }

    public static class ActivityAdded extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Activity added")
                    .setMessage("The activity has been successfully added.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }

    public static class AddedWithCondition extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Warning").
                    setMessage("The activity has been added to your calendar " +
                            "but doe to your too strict travel preferences it was not possible " +
                            "to calculate the estimated travel time. You may not be notified " +
                            "if you'll risk to be late due to the addition of another activity to the calendar.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }

    public static class LoginError extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Login error").
                    setMessage("Who are you?!? A problem with your credentials occured, you'll be disconneted.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to login screen
                        }
                    });

            return builder.create();
        }
    }

    public static class WrongInput extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Wrong input").
                    setMessage("A problem with data you provided occurred, please check all the fields")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to form
                        }
                    });

            return builder.create();
        }
    }

    public static class ConnectionError extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Connection error").
                    setMessage("Seems like our servers are lazy ;) Please try again in a while.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to form
                        }
                    });

            return builder.create();
        }
    }

    public static class Overlap extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Overlapping activities").
                    setMessage("You're too busy! This activity overlaps with others, please check your calendar.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to form
                        }
                    });

            return builder.create();
        }
    }

    public static class Past extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Past activity").
                    setMessage("This activity seems to be in the past...try to ask to Martin McFly.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to form
                        }
                    });

            return builder.create();
        }
    }

    public static class UnexpectedError extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Unexpected error").
                    setMessage("An unexpected error occurred. You'll be directed to the last valid screen.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to calendar
                        }
                    });

            return builder.create();
        }
    }

    public static class StartTagList extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a tag")
                    .setItems(NewActActivity.userTags, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NewActActivity.selectedStartTag = NewActActivity.userTags[i];
                            mStartTag.setText(NewActActivity.selectedStartTag);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedStartTag = "";
                            mStartTag.setText("");
                            //reset and hide list
                        }
                    });
            return builder.create();
        }
    }

    public static class EndTagList extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a tag")
                    .setItems(NewActActivity.userTags, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NewActActivity.selectedEndTag = NewActActivity.userTags[i];
                            mEndTag.setText(NewActActivity.selectedEndTag);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedEndTag = "";
                            mEndTag.setText(R.string.select_tag);
                            //reset and hide list
                        }
                    });
            return builder.create();
        }
    }
}
