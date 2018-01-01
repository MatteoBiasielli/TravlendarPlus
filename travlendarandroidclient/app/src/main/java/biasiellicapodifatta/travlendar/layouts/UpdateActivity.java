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
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.internal.bind.DateTypeAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.activities.Activity;
import biasiellicapodifatta.travlendar.data.activities.Break;
import biasiellicapodifatta.travlendar.data.activities.FixedActivity;
import biasiellicapodifatta.travlendar.network.NetworkLayer;
import biasiellicapodifatta.travlendar.response.responsedeleteactivity.ResponseDeleteActivity;
import biasiellicapodifatta.travlendar.response.responseupdateactivity.ResponseUpdateActivity;


public class UpdateActivity extends AppCompatActivity {

    private UpdateActivityTask mUpdateTask = null;
    private DeleteActivityTask mDeleteTask = null;

    private int mNewStartHour;
    private int mNewStartMin;
    private int mNewEndHour;
    private int mNewEndMin;
    private static String[] userTags = new String[Data.getUser().getFavPositions().size()];
    private static String selectedStartTag = "";
    private static String selectedEndTag = "";
    private static Boolean deletion = false;

    //UI references
    private TextView mHead;

    private EditText mNewStartPosition;
    private EditText mNewEndPosition;
    private EditText mNewNotes;
    private EditText mNewDuration;

    private static EditText mStartTag;
    private static EditText mEndTag;

    private EditText mTimeStart;
    private EditText mTimeEnd;

    private View mUpdateFormView;
    private View mProgressView;

    private DatePicker mDatePickerStart;
    private DatePicker mDatePickerEnd;

    private Switch mFlexibleSwitch;


    /**
     * Initialize UI components and its listeners by retrieving their id-s
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        mHead = findViewById(R.id.head);
        //TODO: settare nome attività
        mHead.setText("Update activity "+intent.getStringExtra("activityName"));

        Activity tmp = null;
        for(FixedActivity a : Data.getUser().getCalendar().getFixedActivities())
            if(a.getLabel().equals(intent.getStringExtra("activityName"))) {
                tmp = a;
                break;
            }

        if(tmp == null){
            for(Break b : Data.getUser().getCalendar().getBreaks())
                if(b.getLabel().equals(intent.getStringExtra("activityName"))) {
                    tmp = b;
                    break;
                }
        }

        mNewStartPosition = findViewById(R.id.start_position_form);
        mNewStartPosition.setText(tmp.getStartPlaceAddress());

        mNewEndPosition = findViewById(R.id.destination_position_form);
        mNewEndPosition.setText(tmp.getLocationAddress());

        mNewDuration = findViewById(R.id.duration_form);
        mNewDuration.setText(""+tmp.getDuration());

        mTimeStart = findViewById(R.id.selectTimeStart);
        //TODO:testare
        mTimeStart.setText(new SimpleDateFormat("HH:mm", Locale.ITALY).format(tmp.getStartDate()));
        mTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinutes) {
                        mNewStartHour = selectedHour;
                        mNewStartMin = selectedMinutes;
                        mTimeStart.setText(selectedHour + ":" + selectedMinutes);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select time");
                mTimePicker.show();
            }
        });

        mTimeEnd = findViewById(R.id.selectTimeEnd);
        //TODO: testare
        mTimeEnd.setText(new SimpleDateFormat("HH:mm", Locale.ITALY).format(tmp.getEndDate()));
        mTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinutes) {
                        mNewEndHour = selectedHour;
                        mNewEndMin = selectedMinutes;
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

        Button updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUpdate();
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, MainTabContainer.class);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment confirm = new DeletionConfirmation();
                confirm.show(getFragmentManager(), "confirmation");
                if(deletion)
                    attemptDelete();

                deletion = false;
            }
        });

        mFlexibleSwitch = (Switch) findViewById(R.id.flex_switch);
        mFlexibleSwitch.setChecked(tmp.isFlexible());

        mNewNotes = findViewById(R.id.notes_form);
        mNewNotes.setText(tmp.getNotes());

        mProgressView = findViewById(R.id.progressBar);
        mUpdateFormView = findViewById(R.id.new_act_view);
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

            mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUpdateFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Retrieves data from each field (if changed) and attempts to update the selected activity
     * connecting to the server
     */
    private void attemptUpdate(){
        if(mUpdateTask != null)
            return;

        //reset errors
        mHead.setError(null);
        mNewStartPosition.setError(null);
        mNewEndPosition.setError(null);
        mNewNotes.setError(null);
        mStartTag.setError(null);
        mEndTag.setError(null);

        Intent intent = getIntent();
        String act_name = intent.getStringExtra("activityName");
        String start_pos = mNewStartPosition.getText().toString();
        String end_pos = mNewEndPosition.getText().toString();
        String start_tag = selectedStartTag;
        String end_tag = selectedEndTag;
        String notes = mNewNotes.getText().toString();

        String duration;
        if(mFlexibleSwitch.isChecked())
            duration = mNewDuration.getText().toString();
        else
            duration = "0";

        int start_hour = mNewStartHour;
        int start_min = 0;
        if(mNewStartMin <= 12)
            start_min =  0;
        else if(mNewStartMin > 12 && mNewStartMin <= 27)
            start_min =  15;
        else if(mNewStartMin > 27 && mNewStartMin <= 42)
            start_min =  30;
        else if(mNewStartMin > 42 && mNewStartMin <= 57)
            start_min =  45;
        else if(mNewStartMin > 57) {
            start_min =  0;
            start_hour++;
        }

        Calendar start_calendar = Calendar.getInstance();
        start_calendar.set(mDatePickerStart.getYear(), mDatePickerStart.getMonth(), mDatePickerStart.getDayOfMonth(), start_hour,     start_min, 0);

        int end_hour =  mNewEndHour;
        int end_min =  0;
        if(mNewEndMin <= 12)
            end_min =  0;
        else if(mNewEndMin > 12 && mNewEndMin <= 27)
            end_min =  15;
        else if(mNewEndMin > 27 && mNewEndMin <= 42)
            end_min =  30;
        else if(mNewEndMin > 42 && mNewEndMin <= 57)
            end_min =  45;
        else if(mNewEndMin > 57) {
            end_min =  0;
            end_hour++;
        }

        Calendar end_calendar = Calendar.getInstance();
        end_calendar.set(mDatePickerEnd.getYear(), mDatePickerEnd.getMonth(), mDatePickerEnd.getDayOfMonth(), end_hour, end_min, 0);

        boolean cancel = false;
        View focusView = null;

        //TODO: controlli

        if(cancel){
            //if one or more errors occur, focus the error view on the first error occured
            focusView.requestFocus();
        }else{
            //show a progress bar,
            showProgress(true);
            mUpdateTask = new UpdateActivityTask(Data.getUser().getUsername(), Data.getUser().getPassword(),
                    act_name, notes, start_pos, end_pos, start_calendar.getTime(), end_calendar.getTime(), mFlexibleSwitch.isChecked(), duration, start_tag, end_tag);
            // ip address set by the login screen
            mUpdateTask.execute((Void)null);
        }
    }

    /**
     * Retrieves data from few fields and attempts to delete the selected activity
     * connecting to the server
     */
    private void attemptDelete(){
        Intent intent = getIntent();
        mDeleteTask = new DeleteActivityTask(Data.getUser().getUsername(), Data.getUser().getPassword(), intent.getStringExtra("activityName"));
        mDeleteTask.execute((Void)null);
    }

    /**
     * Task appointed to connect to the server and update the selected activity
     */
    public class UpdateActivityTask extends AsyncTask<Void, Void, ResponseUpdateActivity>{

        private final String locUsername;
        private int actID;
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
        private ResponseUpdateActivity response;

        UpdateActivityTask(String username, String password, String actName, String notes, String startPos,
                           String endPos, Date startDate, Date endDate, Boolean isFlexible, String duration,
                           String startTag, String endTag){

            locUsername = username;
            locPassword = password;
            locName = actName;
            locNotes = notes;
            locStartPosition = startPos;
            locEndPosition = endPos;
            locStartDate = startDate;
            locEndDate = endDate;
            locFlexible = isFlexible;
            locDuration = duration;
            locStartTag = startTag;
            locEndTag = endTag;
            Integer id = null;
            for(FixedActivity a : Data.getUser().getCalendar().getFixedActivities())
                if(a.getLabel().equals(locName)) {
                    id = a.getKey();
                    break;
                }

            if(id == null){
                for(Break b : Data.getUser().getCalendar().getBreaks())
                    if(b.getLabel().equals(locName)) {
                        id = b.getKey();
                        break;
                    }
            }
            try {
                actID = id;
            }catch(NullPointerException e){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intent = new Intent(UpdateActivity.this, MainTabContainer.class);
                startActivity(intent);
            }

        }

        @Override
        protected ResponseUpdateActivity doInBackground(Void... voids) {

            try{
                response = NetworkLayer.updateActivityRequest(locUsername, locPassword, locName, locNotes,
                        locEndPosition, locEndTag, locStartPosition, locStartTag, locFlexible, locDuration,locStartDate, locEndDate, actID);
            }catch (IOException e){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intent = new Intent(UpdateActivity.this, MainTabContainer.class);
                startActivity(intent);
            }

            return response;
        }

        @Override
        protected void onPostExecute(ResponseUpdateActivity responseUpdateActivity) {

            mUpdateTask = null;
            showProgress(false);

            if(response == null){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intentx = new Intent(UpdateActivity.this, MainTabContainer.class);
                startActivity(intentx);
                return;
            }

            switch (response.getType()){
                case OK:
                    //update local calendar
                    Data.getUser().setCalendar(response.getU().getCalendar());

                    DialogFragment pop_up = new ActivityUpdated();
                    pop_up.show(getFragmentManager(), "updated");
                    Intent intent = new Intent(UpdateActivity.this, MainTabContainer.class);
                    startActivity(intent);
                    break;
                case OK_ESTIMATED_TIME:
                    //update local calendar
                    Data.getUser().setCalendar(response.getU().getCalendar());

                    DialogFragment pop_up_warning = new UpdatedWithCondition();
                    pop_up_warning.show(getFragmentManager(), "warning");
                    Intent intent2 = new Intent(UpdateActivity.this, MainTabContainer.class);
                    startActivity(intent2);
                    break;
                case UPDATE_ACTIVITY_LOGIN_ERROR:
                    DialogFragment login_error = new LoginError();
                    login_error.show(getFragmentManager(), "login-error");
                    Intent intent3 = new Intent(UpdateActivity.this, LoginActivity.class);
                    startActivity(intent3);
                    break;
                case UPDATE_ACTIVITY_WRONG_INPUT:
                    DialogFragment wrong_input = new WrongInput();
                    wrong_input.show(getFragmentManager(), "wrong-input");
                    break;
                case UPDATE_ACTIVITY_CONN_ERROR:
                    DialogFragment conn_error = new ConnectionError();
                    conn_error.show(getFragmentManager(), "conn-error");
                    break;
                case UPDATE_ACTIVITY_OVERLAPPING_ERROR:
                    DialogFragment overlap = new Overlap();
                    overlap.show(getFragmentManager(), "overlap");
                    break;
                case UPDATE_ACTIVITY_PAST_INSERTION:
                    DialogFragment past = new Past();
                    past.show(getFragmentManager(), "past-act");
                    break;
                default:
                    DialogFragment unexp = new UnexpectedError();
                    unexp.show(getFragmentManager(), "unexp-err");
                    Intent intentx = new Intent(UpdateActivity.this, MainTabContainer.class);
                    startActivity(intentx);
                    break;
            }
        }

        @Override
        protected void onCancelled(){
            mUpdateTask = null;
            showProgress(false);
        }
    }

    /**
     * Task appointed to connect to the server and delete the selected activity
     */
    public class DeleteActivityTask extends AsyncTask<Void, Void, ResponseDeleteActivity>{

        private final String username;
        private final String password;
        private String actID;
        private ResponseDeleteActivity response;

        DeleteActivityTask(String user, String pass, String name){

            username = user;
            password = pass;

            Integer id = null;
            for(FixedActivity a : Data.getUser().getCalendar().getFixedActivities())
                if(a.getLabel().equals(name)) {
                    id = a.getKey();
                    break;
                }

            if(id == null){
                for(Break b : Data.getUser().getCalendar().getBreaks())
                    if(b.getLabel().equals(name)) {
                        id = b.getKey();
                        break;
                    }
            }
            try {
                actID = ""+id;
            }catch(NullPointerException e){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intent = new Intent(UpdateActivity.this, MainTabContainer.class);
                startActivity(intent);
            }
        }

        @Override
        protected ResponseDeleteActivity doInBackground(Void... voids) {

            try{
                response = NetworkLayer.deleteActivityRequest(username, password, actID);
            }catch (IOException e){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intent = new Intent(UpdateActivity.this, MainTabContainer.class);
                startActivity(intent);
            }

            return response;
        }

        @Override
        protected void onPostExecute(ResponseDeleteActivity responseDeleteActivity) {

            mDeleteTask = null;
            showProgress(false);

            if(response == null){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
                Intent intentx = new Intent(UpdateActivity.this, MainTabContainer.class);
                startActivity(intentx);
                return;
            }

            switch (response.getRdat()){
                case OK:
                    //update local calendar
                    Data.getUser().setCalendar(response.getCal());

                    DialogFragment pop_up = new ActivityDeleted();
                    pop_up.show(getFragmentManager(), "deleted");
                    Intent intent = new Intent(UpdateActivity.this, MainTabContainer.class);
                    startActivity(intent);
                    break;
                case DELETE_ACTIVITY_LOGIN_ERROR:
                    DialogFragment login_error = new LoginError();
                    login_error.show(getFragmentManager(), "login-error");
                    Intent intent3 = new Intent(UpdateActivity.this, LoginActivity.class);
                    startActivity(intent3);
                    break;
                case DELETE_ACTIVITY_WRONG_INPUT:
                    DialogFragment wrong_input = new WrongInput();
                    wrong_input.show(getFragmentManager(), "wrong-input");
                    break;
                case DELETE_ACTIVITY_CONN_ERROR:
                    DialogFragment conn_error = new ConnectionError();
                    conn_error.show(getFragmentManager(), "conn-error");
                    break;
                default:
                    DialogFragment unexp = new UnexpectedError();
                    unexp.show(getFragmentManager(), "unexp-err");
                    Intent intentx = new Intent(UpdateActivity.this, MainTabContainer.class);
                    startActivity(intentx);
                    break;
            }
        }
    }

    /**
     * Dialog used to make the user aware that the selected activity has been updated successfully
     */
    public static class ActivityUpdated extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Activity updated")
                    .setMessage("The activity has been successfully updated.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware that the selected activity has been deleted successfully
     */
    public static class ActivityDeleted extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Activity deleted")
                    .setMessage("The activity has been successfully deleted.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware that the selected activity has been updated but with
     * some limitation he should take into account
     */
    public static class UpdatedWithCondition extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Warning").
                    setMessage("The activity has been added/updated to your calendar " +
                            "but due to your too strict travel preferences it was not possible " +
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

    /**
     * Dialog used to make the user aware of some problems in the credentials used for the request
     */
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

    /**
     * Dialog used to make the user aware of some problems in the data provided
     */
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

    /**
     * Dialog used to make the user aware of a connection problem during the processing
     */
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

    /**
     * Dialog making the user aware of problems in the dates
     */
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

    /**
     * Dialog making the user aware of problems in the dates
     */
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

    /**
     * Dialog making the user aware of an unexpected error occurred during the processing
     */
    public static class UnexpectedError extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Unexpected error")
                    .setMessage("An unexpected error occurred. You'll be directed to the last valid screen.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //back to calendar
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog popping-up to be sure the user really wants to delete the selected activity
     */
    public static class DeletionConfirmation  extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("WARNING")
                    .setMessage("By clicking 'Delete' the selected activity will be permanently delete from your calendar")
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateActivity.deletion = true;
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateActivity.deletion = false;
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog showing user's tags and handling his choice
     */
    public static class StartTagList extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a tag")
                    .setItems(UpdateActivity.userTags, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateActivity.selectedStartTag = UpdateActivity.userTags[i];
                            mStartTag.setText(UpdateActivity.selectedStartTag);
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

    /**
     * Dialog showing user's tags and handling his choice
     */
    public static class EndTagList extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a tag")
                    .setItems(UpdateActivity.userTags, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateActivity.selectedEndTag = UpdateActivity.userTags[i];
                            mEndTag.setText(UpdateActivity.selectedEndTag);
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
