package biasiellicapodifatta.travlendar.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.network.NetworkLayer;
import biasiellicapodifatta.travlendar.response.responseaddactivity.ResponseAddActivity;

import static java.lang.Integer.parseInt;

public class NewActActivity extends AppCompatActivity {

    private AddFixedActivity mAddTask = null;

    //UI references
    private EditText mActivityNameView;
    private EditText mStartPositionView;
    private EditText mEndPositionView;
    private EditText mNotesView;
    private EditText mDuration;

    private View mNewActFormView;
    private View mProgressView;

    private TimePicker mTimePickerStart;
    private TimePicker mTimePickerEnd;

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

        mTimePickerStart = findViewById(R.id.timePicker_start);
        mTimePickerEnd = findViewById(R.id.timePicker_end);

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

        String act_name = mActivityNameView.getText().toString();
        String start = mStartPositionView.getText().toString();
        String dest = mEndPositionView.getText().toString();
        String notes = mNotesView.getText().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.set(mDatePickerStart.getYear(), mDatePickerStart.getMonth(), mDatePickerStart.getDayOfMonth(), 0, 0, 0);
        Long start_date = calendar.getTimeInMillis();
        calendar.set(mDatePickerEnd.getYear(), mDatePickerEnd.getMonth(), mDatePickerEnd.getDayOfMonth(), 0, 0, 0);
        Long end_date = calendar.getTimeInMillis();

        Integer duration;
        if(mFlexibleSwitch.isChecked())
            duration = parseInt( mDuration.getText().toString() );
        else
            duration = null;


        Long start_hour = (long) mTimePickerStart.getHour();
        Long start_min;
        if(mTimePickerStart.getMinute() <= 12)
            start_min = (long) 0;
        else if(mTimePickerStart.getMinute() > 12 && mTimePickerStart.getMinute() <= 27)
                start_min = (long) 15;
            else if(mTimePickerStart.getMinute() > 27 && mTimePickerStart.getMinute() <= 42)
                    start_min = (long) 30;
                else if(mTimePickerStart.getMinute() > 42 && mTimePickerStart.getMinute() <= 57)
                        start_min = (long) 45;
                    else if(mTimePickerStart.getMinute() > 57) {
                            start_min = (long) 0;
                            start_hour++;
                        }

        start_hour = (long) mTimePickerStart.getHour() * 60 * 60 * 1000;
        start_min = (long) ( mTimePickerStart.getMinute() * 60 * 1000);
        start_date = start_date + start_hour + start_min;

        Long end_hour = (long) mTimePickerEnd.getHour();
        Long end_min;
        if(mTimePickerEnd.getMinute() <= 12)
            end_min = (long) 0;
        else if(mTimePickerEnd.getMinute() > 12 && mTimePickerEnd.getMinute() <= 27)
            end_min = (long) 15;
        else if(mTimePickerEnd.getMinute() > 27 && mTimePickerEnd.getMinute() <= 42)
            end_min = (long) 30;
        else if(mTimePickerEnd.getMinute() > 42 && mTimePickerEnd.getMinute() <= 57)
            end_min = (long) 45;
        else if(mTimePickerEnd.getMinute() > 57) {
            end_min = (long) 0;
            end_hour++;
        }

        end_hour = (long) mTimePickerEnd.getHour() * 60 * 60 * 1000;
        end_min = (long) ( mTimePickerEnd.getMinute() * 60 * 1000);
        end_date = end_date + end_hour + end_min;

        boolean cancel = false;
        View focusView = null;

        if(!isDateValid(start_date)){
            mActivityNameView.setError("This activity begins in the past.");
            cancel = true;
        }

        if(!isDateValid(end_date)){
            mActivityNameView.setError("This activity ends in the past.");
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
                    act_name, notes, start, dest, start_date, end_date, mFlexibleSwitch.isChecked(), duration.toString());
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
        private ResponseAddActivity response;

        AddFixedActivity(String username, String password, String name, String notes, String startpos,
                         String endpos, long startdate, long enddate, Boolean flexible, String duration){
            locUsername = username;
            locPassword = password;
            locName = name;
            locNotes = notes;
            locStartPosition = startpos;
            locEndPosition = endpos;
            locStartDate = new Date (startdate);
            locEndDate = new Date(enddate);
            locFlexible = flexible;
            locDuration = duration;
        }

        @Override
        protected ResponseAddActivity doInBackground(Void... params){

            try{ //TODO: implement tags
                response = NetworkLayer.addActivityRequest(locUsername, locPassword, locName, locNotes,
                            locEndPosition, null, locStartPosition, null, locFlexible, locDuration,locStartDate, locEndDate);
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

            if(response.equals(null)){
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
                    // TODO serve?? :Intent intent4 = new Intent(NewActActivity.this, NewActActivity.class);
                    //startActivity(intent4);
                    break;
                case ADD_ACTIVITY_CONNECTION_ERROR:
                    DialogFragment conn_error = new ConnectionError();
                    conn_error.show(getFragmentManager(), "conn-error");
                    //TODO server?? :Intent intent5 = new Intent(NewActActivity.this, NewActActivity.class);
                    //startActivity(intent5);
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
                    setMessage("A problem with data provided by you occurred, please check all the fields")
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
}
