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

    private ArrayList<RangedPreference> rangUpdResult = null;
    private ArrayList<RangedPreference> rangDelResult = null;

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
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_6));
        //Getting save button and setting listener
        mSaveButton = (Button) preferencesView.findViewById(R.id.save_prefs);

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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
            }
        });

        //Update view with current data
        updateModesView();
        updateEditTextsView();
        updateMeansView();

        return preferencesView;
    }

    private void savePreferences(){
        boolean isBoolUpdDetected = !myUser.getBoolPreferences().equalTo(boolSet);
        boolean isRangUpdDetected = false;
        boolean isRangDelDetected = false;
        DialogFragment f;

        if(mRangUpdTask != null || mBoolUpdTask != null || mRangDelTask != null){
            return;
        }
        
        ArrayList<RangedPreference> newRangSet = new ArrayList<>();
        RangedPreferenceType currType;
        RangedPreference newRang, oldRang;

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

            if(t.getText().toString().equals("")) {
                t.setText(R.string.et_def_value);
            }

            newRang = new RangedPreference(currType, Integer.parseInt(t.getText().toString()));
            oldRang = newRang.getSameTypeIn(rangSet);

            //newRang is in the old ranged preferences set with a different value, thus add to the set of preferences to save.
            if(oldRang != null && newRang.getValue() != oldRang.getValue()){
                    newRangSet.add(newRang);
                    if(newRang.getValue() == 0){
                        isRangDelDetected = true;
                    }
                    else{
                        isRangUpdDetected = true;
                    }
            }

            //newRang has a value different from 0 (update ranged request) and is not in the old ranged preferences set,
            //thus add to the set of preferences to save.
            else if(oldRang == null && newRang.getValue() != 0){
                newRangSet.add(newRang);
                isRangUpdDetected = true;
            }
        }

        if(!isRangUpdDetected && !isBoolUpdDetected && !isRangDelDetected){
            f = new SavePreferencesNotRequiredMessage();
            f.show(getActivity().getFragmentManager(), "savepreferences_notrequired");
        }
        else {
            //Set tasks to execute. Needed due to synchronization issues.
            if(isRangUpdDetected){
                isRangUpdFinished = false;
            }
            else {
                isRangUpdFinished = true;
            }
            if(isBoolUpdDetected){
                isBoolUpdFinished = false;
            }
            else {
                isBoolUpdDetected = true;
            }
            if(isRangDelDetected){
                isRangDelFinished = false;
            }
            else {
                isRangDelFinished = true;
            }

            showProgress(true);
            //Execute tasks
            if(isRangUpdDetected) {
                mRangUpdTask = new UpdateRangedPreferencesTask(newRangSet);
                mRangUpdTask.execute((Void) null);
            }
            if(isBoolUpdDetected) {
                mBoolUpdTask = new UpdateBooleanPreferencesTask(boolSet);
                mBoolUpdTask.execute((Void) null);
            }
            if(isRangDelDetected) {
                mRangDelTask = new DeleteRangedPreferencesTask(newRangSet);
                mRangDelTask.execute((Void) null);
            }
        }
    }

    private void setModes(View b){
        int mod;

        switch(b.getId()){
            case R.id.mod_1:
                mod = 4;
                break;
            case R.id.mod_2:
                mod = 3;
                break;
            case R.id.mod_3:
                mod = 2;
                break;
            case R.id.mod_4:
                mod = 1;
                break;
            default:
                mod = 4;
        }
        boolSet.setMode(Modality.getModalityfor(mod));

        updateModesView();
    }

    private void setMeans(View b){
        int ifSelectedResId;
        int ifNotSelectedResId;

        switch(b.getId()){
            case R.id.mean_1:
                boolSet.setPersonalBike(!boolSet.personalBike());
                ifSelectedResId = R.drawable.common_full_open_on_phone;
                ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                break;
            case R.id.mean_2:
                boolSet.setPersonalCar(!boolSet.personalCar());
                ifSelectedResId = R.drawable.common_full_open_on_phone;
                ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                break;
            case R.id.mean_3:
                boolSet.setPublicTransport(!boolSet.publicTrasport());
                ifSelectedResId = R.drawable.common_full_open_on_phone;
                ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                break;
            case R.id.mean_4:
                boolSet.setBikeSharing(!boolSet.bikeSharing());
                ifSelectedResId = R.drawable.common_full_open_on_phone;
                ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                break;
            case R.id.mean_5:
                boolSet.setCarSharing(!boolSet.carSharing());
                ifSelectedResId = R.drawable.common_full_open_on_phone;
                ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                break;
            case R.id.mean_6:
                boolSet.setUberTaxi(!boolSet.uberTaxi());
                ifSelectedResId = R.drawable.common_full_open_on_phone;
                ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                break;
            default:
                ifSelectedResId = R.drawable.common_full_open_on_phone;
                ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                break;
        }
        b.setSelected(!b.isSelected());

        updateButtonImage((ImageButton) b, ifSelectedResId, ifNotSelectedResId);
    }

    private void getCurrentData(){
        ArrayList<RangedPreference> myRang = myUser.getRangedPreferences();
        BooleanPreferencesSet myBool = myUser.getBoolPreferences();

        this.rangSet = new ArrayList<>(myRang);
        this.boolSet = new BooleanPreferencesSet(myBool);
    }

    private synchronized void updateView(){
        if(isRangUpdFinished && isBoolUpdFinished && isRangDelFinished){
            DialogFragment f;

            mergeRangResults();

            //Show appropriate pop-up
            if(!isTaskErrorDetected){
                //Update data
                getCurrentData();

                //Update view (useless since view's context is automatically saved, but formally correct)
                updateModesView();
                updateEditTextsView();
                updateMeansView();

                f = new SavePreferencesOKMessage();
                f.show(getActivity().getFragmentManager(), "savepreferences_ok");
            }
            else{
                f = new SavePreferencesErrorMessage();
                f.show(getActivity().getFragmentManager(), "savepreferences_error");
            }

            showProgress(false);

            //Reset task status values
            isRangUpdFinished = false;
            isBoolUpdFinished = false;
            isRangDelFinished = false;
            isTaskErrorDetected = false;
        }
    }

    private void updateModesView(){
        int ifSelectedResId;
        int ifNotSelectedResId;

        for(ImageButton b : mModesList) {
            switch (b.getId()) {
                case R.id.mod_1:
                    b.setSelected(boolSet.mode().getValue() == 4);
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mod_2:
                    b.setSelected(boolSet.mode().getValue() == 3);
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mod_3:
                    b.setSelected(boolSet.mode().getValue() == 2);
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mod_4:
                    b.setSelected(boolSet.mode().getValue() == 1);
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                default:
                    b.setSelected(boolSet.mode().getValue() == 4);
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
            }

            updateButtonImage(b, ifSelectedResId, ifNotSelectedResId);
        }
    }

    private void updateEditTextsView() {
        RangedPreferenceType currType;
        RangedPreference upd;
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
            upd = (new RangedPreference(currType, 0)).getSameTypeIn(rangSet);

            if(upd != null) {
                t.setText(Integer.toString(upd.getValue()));
            }
        }
    }

    private void updateMeansView(){
        int ifSelectedResId;
        int ifNotSelectedResId;

        for(ImageButton b : mMeansList){
            switch(b.getId()){
                case R.id.mean_1:
                    b.setSelected(boolSet.personalBike());
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mean_2:
                    b.setSelected(boolSet.personalCar());
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mean_3:
                    b.setSelected(boolSet.publicTrasport());
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mean_4:
                    b.setSelected(boolSet.bikeSharing());
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mean_5:
                    b.setSelected(boolSet.carSharing());
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                case R.id.mean_6:
                    b.setSelected(boolSet.uberTaxi());
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
                default:
                    ifSelectedResId = R.drawable.common_full_open_on_phone;
                    ifNotSelectedResId = R.drawable.common_google_signin_btn_icon_light_normal_background;
                    break;
            }

            updateButtonImage(b, ifSelectedResId, ifNotSelectedResId);
        }
    }

    private void updateButtonImage(ImageButton b, int ifSelectedResId, int ifNotSelectedResId){
        if (!b.isSelected()) {
            ((ImageButton) b).setImageResource(ifNotSelectedResId);
        }
        else {
            ((ImageButton) b).setImageResource(ifSelectedResId);
        }
    }

    private void mergeRangResults() {
        if(rangUpdResult != null && rangDelResult != null) {
            myUser.setRangedPreferences(rangUpdResult.size() == rangDelResult.size() ? rangUpdResult : rangDelResult);
        } else if(rangUpdResult != null || rangDelResult != null){
            myUser.setRangedPreferences(rangDelResult == null ? rangUpdResult : rangDelResult);
        }

        rangUpdResult = null;
        rangDelResult = null;
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
        protected ResponseUpdateRangedPreferences doInBackground(Void... params) {
            if(!Data.isOfflineMode()) {
                ArrayList<Integer> vals = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();

                for(RangedPreference rp : rangs){
                    if(rp.getValue() != 0) {
                        vals.add(rp.getType().getValue());
                        ids.add(Integer.toString(rp.getValue()));
                    }
                }

                try {
                    response = NetworkLayer.updateRangedPreferencesRequest(myUser.getUsername(), myUser.getPassword(), vals, ids);
                } catch (IOException e) {
                    return null;
                }
            }
            else {
                //Simulate database access and operations.
                synchronized (rangSet) {
                    RangedPreference upd;

                    for (RangedPreference rp : rangs) {
                        if (rp.getValue() != 0) {
                            upd = rp.getSameTypeIn(rangSet);
                            rangSet.remove(upd);
                            rangSet.add(rp);
                        }
                    }

                    response = new ResponseUpdateRangedPreferences(ResponseUpdateRangedPreferencesType.OK, new ArrayList<RangedPreference>(rangSet));
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseUpdateRangedPreferences response) {
            mRangUpdTask = null;

            if(response == null){
                isTaskErrorDetected = true;
                return;
            }

            switch (response.getType()){
                case OK:
                    break;
                default:
                    isTaskErrorDetected = true;
                    break;
            }

            rangUpdResult = response.getData();
            isRangUpdFinished = true;
            updateView();
        }

        @Override
        protected void onCancelled() {
            mRangUpdTask = null;
            isRangUpdFinished = true;
            isTaskErrorDetected = false;
        }
    }

    public class UpdateBooleanPreferencesTask extends AsyncTask<Void, Void, ResponseUpdateBooleanPreferences>{
        BooleanPreferencesSet bools;
        ResponseUpdateBooleanPreferences response;

        UpdateBooleanPreferencesTask(BooleanPreferencesSet bs){
            this.bools = bs;
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

            if(response == null){
                isTaskErrorDetected = true;
                return;
            }

            switch (response.getType()){
                case OK:;
                    break;
                default:
                    isTaskErrorDetected = true;
                    break;
            }

            myUser.setBooleanPreferences(response.getData());
            isBoolUpdFinished = true;
            updateView();
        }

        @Override
        protected void onCancelled() {
            mBoolUpdTask = null;
            isBoolUpdFinished = true;
            isTaskErrorDetected = false;
        }
    }

    public class DeleteRangedPreferencesTask extends AsyncTask<Void, Void, ResponseDeleteRangedPreferences>{
        ArrayList<RangedPreference> rangs;
        ResponseDeleteRangedPreferences response;

        DeleteRangedPreferencesTask(ArrayList<RangedPreference> rs){
            this.rangs = rs;
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
                //Simulate database access and operations.
                synchronized (rangSet){
                    RangedPreference upd;

                    for(RangedPreference rp : rangs){
                        if(rp.getValue() == 0) {
                            upd = rp.getSameTypeIn(rangSet);
                            rangSet.remove(upd);
                        }
                    }

                    response = new ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType.OK, new ArrayList<RangedPreference>(rangSet));
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseDeleteRangedPreferences response) {
            mRangDelTask = null;

            if(response == null){
                isTaskErrorDetected = true;
                return;
            }

            switch (response.getType()){
                case OK:
                    break;
                default:
                    isTaskErrorDetected = true;
                    break;
            }

            rangDelResult = response.getData();
            isRangDelFinished = true;

            updateView();
        }

        @Override
        protected void onCancelled() {
            mRangDelTask = null;
            isRangDelFinished = true;
            isTaskErrorDetected = false;
        }
    }
}
