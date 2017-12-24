package biasiellicapodifatta.travlendar.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.user.User;
import biasiellicapodifatta.travlendar.data.user.preferences.BooleanPreferencesSet;
import biasiellicapodifatta.travlendar.data.user.preferences.Modality;
import biasiellicapodifatta.travlendar.data.user.preferences.RangedPreference;
import biasiellicapodifatta.travlendar.data.user.preferences.RangedPreferenceType;
import biasiellicapodifatta.travlendar.network.NetworkLayer;
import biasiellicapodifatta.travlendar.response.responsedeleterangedpreferences.ResponseDeleteRangedPreferences;
import biasiellicapodifatta.travlendar.response.responsedeleterangedpreferences.ResponseDeleteRangedPreferencesType;
import biasiellicapodifatta.travlendar.response.responseupdatebooleanpreferences.ResponseUpdateBooleanPreferences;
import biasiellicapodifatta.travlendar.response.responseupdatebooleanpreferences.ResponseUpdateBooleanPreferencesType;
import biasiellicapodifatta.travlendar.response.responseupdaterangedpreferences.ResponseUpdateRangedPreferences;
import biasiellicapodifatta.travlendar.response.responseupdaterangedpreferences.ResponseUpdateRangedPreferencesType;

/**
 * Created by Emilio on 03/12/2017.
 */

public class PreferencesTab extends Fragment {
    private User myUser;
    private boolean isBoolUpdFinished;
    private boolean isRangUpdFinished;
    private boolean isRangDelFinished;
    private boolean isTaskErrorDetected;

    private UpdateBooleanPreferencesTask mBoolUpdTask = null;
    private UpdateRangedPreferencesTask mRangUpdTask = null;
    private DeleteRangedPreferencesTask mRangDelTask = null;

    private BooleanPreferencesSet boolSet;
    private ArrayList<RangedPreference> rangSet;

    //UI references
    private View mPreferencesFormView;
    private View mProgressView;
    private ArrayList<ImageButton> mModesList;
    private ArrayList<EditText> mEditTextsList;
    private ArrayList<ImageButton> mMeansList;
    private Button mSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myUser = Data.getUser();
        isBoolUpdFinished = true;
        isRangUpdFinished = true;
        isRangDelFinished = true;
        isTaskErrorDetected = false;
        mModesList = new ArrayList<>();
        mEditTextsList = new ArrayList<>();
        mMeansList = new ArrayList<>();
        mSaveButton = null;
        getCurrentData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        int i;
        View preferencesView = inflater.inflate(R.layout.preferences_tab_layout, container, false);
        mPreferencesFormView = preferencesView.findViewById(R.id.pref_const);
        mProgressView = preferencesView.findViewById(R.id.save_progress);

        //Getting references for modes
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_1));
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_2));
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_3));
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_4));

        //Getting references for ranged parameters
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.walking_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.biking_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.publicTransport_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.car_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.cost_editText));
        //Getting references for boolean parameters
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_1));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_2));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_3));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_4));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_5));
        //Getting save button and setting listener
        mSaveButton = (Button) preferencesView.findViewById(R.id.save_prefs);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
            }
        });

        for(ImageButton b : mModesList){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setModes(view);
                }
            });
        }

        for(ImageButton b : mMeansList){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMeans(view);
                }
            });
        }

        return preferencesView;
    }

    private void savePreferences(){
        boolean isBoolUpdDetected;
        boolean isRangUpdDetected = false;
        boolean isRangDelDetected = false;
        DialogFragment f;

        if(mRangUpdTask != null && mBoolUpdTask != null && mRangDelTask != null){
            return;
        }

        ArrayList<RangedPreference> newRangSet = new ArrayList<>();
        RangedPreferenceType currType;
        RangedPreference tmp;

        isBoolUpdDetected = Data.getUser().getBoolPreferences().equalTo(boolSet);
        for (EditText t : mEditTextsList) {
            switch (t.getId()) {
                case R.id.walking_editText:
                    currType = RangedPreferenceType.WALKING_TIME_LIMIT;
                    break;
                case R.id.biking_editText:
                    currType = RangedPreferenceType.BIKING_TIME_LIMIT;
                    break;
                case R.id.publicTransport_editText:
                    currType = RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT;
                    break;
                case R.id.car_editText:
                    currType = RangedPreferenceType.CAR_TIME_LIMIT;
                    break;
                case R.id.cost_editText:
                    currType = RangedPreferenceType.COST_LIMIT;
                    break;
                default:
                    currType = RangedPreferenceType.WALKING_TIME_LIMIT;
                    break;
            }

            if(t.getText().equals(null)) {
                t.setText(0);
            }
            tmp = new RangedPreference(currType, Integer.parseInt(t.getText().toString()));
            if(!rangSet.contains(tmp)) {
                newRangSet.add(tmp);
                if(tmp.getValue() == 0)
                    isRangDelDetected = true;
                else
                    isRangUpdDetected = true;
            }
        }

        if(!isRangUpdDetected && !isBoolUpdDetected && !isRangDelDetected){
            f = new SavePreferencesNotRequiredMessage();
            f.show(getActivity().getFragmentManager(), "savepreferences_notrequired");
        }
        else {
            if(isRangUpdDetected) {
                mRangUpdTask = new UpdateRangedPreferencesTask(rangSet);
                mRangUpdTask.execute((Void) null);
            }
            if(isBoolUpdDetected) {
                mBoolUpdTask = new UpdateBooleanPreferencesTask(boolSet);
                mBoolUpdTask.execute((Void) null);
            }
            if(isRangDelDetected) {
                mRangDelTask = new DeleteRangedPreferencesTask(rangSet);
                mRangDelTask.execute((Void) null);
            }
        }
    }

    private void setModes(View b){
        int mod;

        switch(b.getId()){
            case R.id.mod_1:
                mod = 2;
                break;
            case R.id.mod_2:
                mod = 3;
                break;
            case R.id.mod_3:
                mod = 4;
                break;
            case R.id.mod_4:
                mod = 1;
                break;
            default:
                mod = 1;
        }

        //Change button image
        b.setSelected(!b.isSelected());

        if (b.isSelected()) {
            ((ImageButton) b).setImageResource(R.drawable.common_full_open_on_phone);
        }
        else {
            ((ImageButton) b).setImageResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
        }

        boolSet.setMode(Modality.getModalityfor(mod));
    }

    private void setMeans(View b){
        switch(b.getId()){
            case R.id.mean_1:
                boolSet.setPersonalBike(!boolSet.personalBike());
                break;
            case R.id.mean_2:
                boolSet.setPersonalCar(!boolSet.personalCar());
                break;
            case R.id.mean_3:
                boolSet.setPublicTransport(!boolSet.publicTrasport());
                break;
            case R.id.mean_4:
                boolSet.setBikeSharing(!boolSet.bikeSharing());
                break;
            case R.id.mean_5:
                boolSet.setCarSharing(!boolSet.carSharing());
                break;
            case R.id.mean_6:
                boolSet.setUberTaxi(!boolSet.uberTaxi());
                break;
            default:
                break;
        }

        //Change button image
        b.setSelected(!b.isSelected());

        if (!b.isSelected()) {
            ((ImageButton) b).setImageResource(R.drawable.common_full_open_on_phone);
        }
        else {
            ((ImageButton) b).setImageResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
        }

    }

    private void getCurrentData(){
        ArrayList<RangedPreference> myRang = myUser.getRangedPreferences();
        BooleanPreferencesSet myBool = myUser.getBoolPreferences();

        this.rangSet = myRang;
        if(!myBool.equals(null)) {
            this.boolSet = myBool;
        }
        else{
            this.boolSet = new BooleanPreferencesSet();
        }
    }

    private synchronized void updateView(){
        if(isRangUpdFinished && isBoolUpdFinished && isRangDelFinished){
            DialogFragment f;

            updateImageButtons(mModesList);
            updateImageButtons(mMeansList);
            updateEditText(mEditTextsList);

            showProgress(false);

            if(!isTaskErrorDetected){
                f = new SavePreferencesOKMessage();
                f.show(getActivity().getFragmentManager(), "savepreferences_ok");
            }
            else{
                f = new SavePreferencesErrorMessage();
                f.show(getActivity().getFragmentManager(), "savepreferences_error");
            }
        }
    }

    private void updateImageButtons(ArrayList<ImageButton> buttons){
        for(ImageButton b : buttons){
            if(b.isSelected())
                b.setSelected(true);
            else
                b.setSelected(false);
        }
    }

    private void updateEditText(ArrayList<EditText> texts) {
        RangedPreferenceType currType;

        for (EditText t : texts) {
            switch (t.getId()) {
                case R.id.walking_editText:
                    currType = RangedPreferenceType.WALKING_TIME_LIMIT;
                    break;
                case R.id.biking_editText:
                    currType = RangedPreferenceType.BIKING_TIME_LIMIT;
                    break;
                case R.id.publicTransport_editText:
                    currType = RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT;
                    break;
                case R.id.car_editText:
                    currType = RangedPreferenceType.CAR_TIME_LIMIT;
                    break;
                case R.id.cost_editText:
                    currType = RangedPreferenceType.COST_LIMIT;
                    break;
                default:
                    currType = RangedPreferenceType.WALKING_TIME_LIMIT;
                    break;
            }

            for (RangedPreference rp : rangSet) {
                if (rp.getType().equals(currType)) {
                    t.setText(rp.getValue());
                    break;
                }
            }
        }
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

            mPreferencesFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mPreferencesFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPreferencesFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mPreferencesFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static class SavePreferencesOKMessage extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Save OK").
                    setMessage("Your preferences have been saved.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Close pop-up
                        }
                    });

            return builder.create();
        }
    }

    public static class SavePreferencesErrorMessage extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Save error").
                    setMessage("An unexpected error occurred. Some preferences may not have been saved.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Close pop-up
                        }
                    });

            return builder.create();
        }
    }

    public static class SavePreferencesNotRequiredMessage extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Save not required").
                    setMessage("No update since last preferences update has been detected.").
                    setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Close pop-up
                        }
                    });

            return builder.create();
        }
    }

    public class UpdateRangedPreferencesTask extends AsyncTask<Void, Void, ResponseUpdateRangedPreferences> {
        ArrayList<RangedPreference> rangs;
        ResponseUpdateRangedPreferences response;

        UpdateRangedPreferencesTask(ArrayList<RangedPreference> rs){
            this.rangs = rs;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
            isRangUpdFinished = false;
        }

        @Override
        protected ResponseUpdateRangedPreferences doInBackground(Void... params) {
            if(!Data.isOfflineMode()) {
                ArrayList<Integer> vals = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();

                for(RangedPreference rp : rangSet){
                    vals.add(rp.getType().getValue());
                    ids.add(Integer.toString(rp.getValue()));
                }

                try {
                    response = NetworkLayer.updateRangedPreferencesRequest(myUser.getUsername(), myUser.getPassword(), vals, ids);
                } catch (IOException e) {
                    return null;
                }
            }
            else {
                response = new ResponseUpdateRangedPreferences(ResponseUpdateRangedPreferencesType.OK, rangSet);
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseUpdateRangedPreferences response) {
            mRangUpdTask = null;

            if(response.equals(null)){
                isTaskErrorDetected = true;
                return;
            }

            switch (response.getType()){
                case OK:
                    myUser.setRangedPreferences(response.getData());
                    break;
                case UPDATE_RANGED_PREFERENCES_INVALID_LOGIN:
                    isTaskErrorDetected = true;
                    break;
                case UPDATE_RANGED_PREFERENCES_WRONG_INPUT:
                    isTaskErrorDetected = true;
                    break;
                case UPDATE_RANGED_PREFERENCES_CONNECTION_ERROR:
                    isTaskErrorDetected = true;
                    break;
                default:
                    isTaskErrorDetected = true;
                    break;
            }

            isRangUpdFinished = true;
            updateView();
        }

        @Override
        protected void onCancelled() {
            mRangUpdTask = null;
            showProgress(false);
        }
    }

    public class UpdateBooleanPreferencesTask extends AsyncTask<Void, Void, ResponseUpdateBooleanPreferences>{
        BooleanPreferencesSet bools;
        ResponseUpdateBooleanPreferences response;

        UpdateBooleanPreferencesTask(BooleanPreferencesSet bs){
            this.bools = bs;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
            isBoolUpdFinished = false;
        }

        @Override
        protected ResponseUpdateBooleanPreferences doInBackground(Void... params) {
            if(!Data.isOfflineMode()) {
                try {
                    response = NetworkLayer.updateBooleanPreferencesRequest(myUser.getUsername(), myUser.getPassword(), bools.personalCar(), bools.carSharing(), bools.personalBike(), bools.bikeSharing(), bools.publicTrasport(), bools.uberTaxi(), bools.mode().getValue());
                } catch (IOException e) {
                    return null;
                }
            }
            else {
                response = new ResponseUpdateBooleanPreferences(ResponseUpdateBooleanPreferencesType.OK, bools);
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseUpdateBooleanPreferences response) {
            mBoolUpdTask = null;

            if(response.equals(null)){
                isTaskErrorDetected = true;
                return;
            }

            switch (response.getType()){
                case OK:
                    myUser.setBooleanPreferences(response.getData());
                    break;
                case UPDATE_BOOLEAN_PREFERENCES_INVALID_LOGIN:
                    isTaskErrorDetected = true;
                    break;
                case UPDATE_BOOLEAN_PREFERENCES_WRONG_INPUT:
                    isTaskErrorDetected = true;
                    break;
                case UPDATE_BOOLEAN_PREFERENCES_CONNECTION_ERROR:
                    isTaskErrorDetected = true;
                    break;
                default:
                    isTaskErrorDetected = true;
                    break;
            }

            isBoolUpdFinished = true;
            updateView();
        }

        @Override
        protected void onCancelled() {
            mBoolUpdTask = null;
            showProgress(false);
        }
    }

    public class DeleteRangedPreferencesTask extends AsyncTask<Void, Void, ResponseDeleteRangedPreferences>{
        ArrayList<RangedPreference> rangs;
        ResponseDeleteRangedPreferences response;

        DeleteRangedPreferencesTask(ArrayList<RangedPreference> rs){
            this.rangs = rs;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
            isRangDelFinished = false;
        }

        @Override
        protected ResponseDeleteRangedPreferences doInBackground(Void... params) {
            if(!Data.isOfflineMode()) {
                ArrayList<String> ids = new ArrayList<>();

                for(RangedPreference rp : rangs){
                    if(rp.getValue() == 0) {
                        ids.add(Integer.toString(rp.getType().getValue()));
                    }
                }

                try {
                    response = NetworkLayer.deleteRangedPreferencesRequest(myUser.getUsername(), myUser.getPassword(), ids);
                } catch (IOException e) {
                    return null;
                }
            }
            else {
                for(RangedPreference rp : rangs){
                    if(rp.getValue() == 0) {
                        rangs.remove(rp);
                    }
                }
                response = new ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType.OK, rangs);
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseDeleteRangedPreferences response) {
            mRangDelTask = null;

            if(response.equals(null)){
                isTaskErrorDetected = true;
                return;
            }

            switch (response.getType()){
                case OK:
                    myUser.deleteRangedPreferences(response.getData());
                    break;
                case DELETE_RANGED_PREFERENCES_INVALID_LOGIN:
                    isTaskErrorDetected = true;
                    break;
                case DELETE_RANGED_PREFERENCES_WRONG_INPUT:
                    isTaskErrorDetected = true;
                    break;
                case DELETE_RANGED_PREFERENCES_CONNECTION_ERROR:
                    isTaskErrorDetected = true;
                    break;
                default:
                    isTaskErrorDetected = true;
                    break;
            }

            isRangDelFinished = true;
            updateView();
        }

        @Override
        protected void onCancelled() {
            mRangDelTask = null;
            showProgress(false);
        }
    }
}
