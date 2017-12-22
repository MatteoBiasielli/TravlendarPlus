package biasiellicapodifatta.travlendar.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.user.User;
import biasiellicapodifatta.travlendar.data.user.preferences.BooleanPreferencesSet;
import biasiellicapodifatta.travlendar.data.user.preferences.Modality;
import biasiellicapodifatta.travlendar.data.user.preferences.RangedPreference;
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
    private User myUser = Data.getUser();
    private boolean isBoolUpdFinished;
    private boolean isRangUpdFinished;
    private boolean isRangDelFinished;
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
        isBoolUpdFinished = true;
        isRangUpdFinished = true;
        isRangDelFinished = true;
        boolSet = new BooleanPreferencesSet();
        rangSet = new ArrayList<>();
        mModesList = new ArrayList<>();
        mEditTextsList = new ArrayList<>();
        mMeansList = new ArrayList<>();
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

        for(final ImageButton b : mModesList){
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

        getCurrentData();

        return preferencesView;
    }

    private void savePreferences(){
        if(mRangUpdTask != null && mBoolUpdTask != null && mRangDelTask != null){
            return;
        }

        mRangUpdTask = new UpdateRangedPreferencesTask(rangSet);
        mRangUpdTask.execute((Void) null);

        mBoolUpdTask = new UpdateBooleanPreferencesTask(boolSet);
        mBoolUpdTask.execute((Void) null);

        mRangDelTask = new DeleteRangedPreferencesTask(rangSet);
        mRangDelTask.execute((Void) null);
    }

    private void setModes(View b){
        int mod;

        //TODO add image button change
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

        boolSet.setMode(Modality.getModalityfor(mod));
    }

    private void setMeans(View b){
        //TODO add image button change
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

    }

    private void getCurrentData(){
        //TODO implement
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

    public class UpdateRangedPreferencesTask extends AsyncTask<Void, Void, ResponseUpdateRangedPreferences> {
        BooleanPreferencesSet bools;
        ArrayList<RangedPreference> rangs;
        ResponseUpdateRangedPreferences response;

        UpdateRangedPreferencesTask(ArrayList<RangedPreference> rs){
            this.rangs = rs;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
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
            showProgress(false);

            if(response.equals(null)){
                //TODO add pop-up
                return;
            }

            switch (response.getType()){
                case OK:
                    myUser.setRangedPreferences(response.getData());
                    //TODO update view
                    //TODO add pop-up
                    break;
                case UPDATE_RANGED_PREFERENCES_INVALID_LOGIN:
                    //TODO add pop-up
                    break;
                case UPDATE_RANGED_PREFERENCES_WRONG_INPUT:
                    //TODO add pop-up
                    break;
                case UPDATE_RANGED_PREFERENCES_CONNECTION_ERROR:
                    //TODO add pop-up
                    break;
                default:
                    //TODO add pop-up
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mRangUpdTask = null;
            showProgress(false);
        }
    }

    public class UpdateBooleanPreferencesTask extends AsyncTask<Void, Void, ResponseUpdateBooleanPreferences>{
        BooleanPreferencesSet bools;
        ArrayList<RangedPreference> rangs;
        ResponseUpdateBooleanPreferences response;

        UpdateBooleanPreferencesTask(BooleanPreferencesSet bs){
            this.bools = bs;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }

        @Override
        protected ResponseUpdateBooleanPreferences doInBackground(Void... params) {
            if(!Data.isOfflineMode()) {
                ArrayList<Integer> vals = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();

                for(RangedPreference rp : rangSet){
                    vals.add(rp.getType().getValue());
                    ids.add(Integer.toString(rp.getValue()));
                }

                try {
                    response = NetworkLayer.updateBooleanPreferencesRequest(myUser.getUsername(), myUser.getPassword(), boolSet.personalCar(), boolSet.carSharing(), boolSet.personalBike(), boolSet.bikeSharing(), boolSet.publicTrasport(), boolSet.uberTaxi(), boolSet.mode().getValue());
                } catch (IOException e) {
                    return null;
                }
            }
            else {
                response = new ResponseUpdateBooleanPreferences(ResponseUpdateBooleanPreferencesType.OK, boolSet);
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseUpdateBooleanPreferences response) {
            mBoolUpdTask = null;
            showProgress(false);

            if(response.equals(null)){
                //TODO add pop-up
                return;
            }

            switch (response.getType()){
                case OK:
                    myUser.setBooleanPreferences(response.getData());
                    //TODO update view
                    //TODO add pop-up
                    break;
                case UPDATE_BOOLEAN_PREFERENCES_INVALID_LOGIN:
                    //TODO add pop-up
                    break;
                case UPDATE_BOOLEAN_PREFERENCES_WRONG_INPUT:
                    //TODO add pop-up
                    break;
                case UPDATE_BOOLEAN_PREFERENCES_CONNECTION_ERROR:
                    //TODO add pop-up
                    break;
                default:
                    //TODO add pop-up
                    break;
            }

            isBoolUpdFinished = true;

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
        }

        @Override
        protected ResponseDeleteRangedPreferences doInBackground(Void... params) {
            if(!Data.isOfflineMode()) {
                ArrayList<String> ids = new ArrayList<>();

                for(RangedPreference rp : rangSet){
                    ids.add(Integer.toString(rp.getValue()));
                }

                try {
                    response = NetworkLayer.deleteRangedPreferencesRequest(myUser.getUsername(), myUser.getPassword(), ids);
                } catch (IOException e) {
                    return null;
                }
            }
            else {
                response = new ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType.OK, rangSet);
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseDeleteRangedPreferences response) {
            mRangDelTask = null;
            showProgress(false);

            if(response.equals(null)){
                //TODO add pop-up
                return;
            }

            switch (response.getType()){
                case OK:
                    myUser.deleteRangedPreferences(response.getData());
                    //TODO update view
                    //TODO add pop-up
                    break;
                case DELETE_RANGED_PREFERENCES_INVALID_LOGIN:
                    //TODO add pop-up
                    break;
                case DELETE_RANGED_PREFERENCES_WRONG_INPUT:
                    //TODO add pop-up
                    break;
                case DELETE_RANGED_PREFERENCES_CONNECTION_ERROR:
                    //TODO add pop-up
                    break;
                default:
                    //TODO add pop-up
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mRangDelTask = null;
            showProgress(false);
        }
    }
}
