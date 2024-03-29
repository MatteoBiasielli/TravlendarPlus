package biasiellicapodifatta.travlendar.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

/**
 * This class represents a tab that let the user change his travel preferences.
 */
public class PreferencesTab extends Fragment {
    private User myUser;

    // Running task references.
    private UpdateBooleanPreferencesTask mBoolUpdTask = null;
    private UpdateRangedPreferencesTask mRangUpdTask = null;
    private DeleteRangedPreferencesTask mRangDelTask = null;

    // Results of ranged preferences related tasks.
    private ArrayList<RangedPreference> rangUpdResult = null;
    private ArrayList<RangedPreference> rangDelResult = null;

    // Task status parameters.
    private boolean isBoolUpdFinished;
    private boolean isRangUpdFinished;
    private boolean isRangDelFinished;
    private boolean isTaskErrorDetected;

    // Preferences sets.
    private BooleanPreferencesSet boolSet;
    private ArrayList<RangedPreference> rangSet;

    // UI references.
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

        // Get references for modes.
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_1)); // Minimize time.
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_2)); // Minimize cost.
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_3)); // Minimize footprint.
        mModesList.add((ImageButton) preferencesView.findViewById(R.id.mod_4)); // Standard.

        // Get references for ranged parameters.
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.walking_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.biking_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.publicTransport_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.car_editText));
        mEditTextsList.add((EditText) preferencesView.findViewById(R.id.cost_editText));

        // Get references for boolean parameters.
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_1));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_2));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_3));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_4));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_5));
        mMeansList.add((ImageButton) preferencesView.findViewById(R.id.mean_6));

        // Get save button and set buttons' listeners.
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

        // Set selectors for background tint.
        setModesSelector();
        setMeansSelector();

        // Update view with current data.
        updateModesView();
        updateEditTextsView();
        updateMeansView();

        return preferencesView;
    }

    /**
     * Allows to save the user preferences, if needed.
     */
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

        // Check for changes since last update.
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

            // newRang is in the old ranged preferences set with a different value, thus add to the set of preferences to save.
            if(oldRang != null && newRang.getValue() != oldRang.getValue()){
                    newRangSet.add(newRang);
                    if(newRang.getValue() == 0){
                        isRangDelDetected = true;
                    }
                    else{
                        isRangUpdDetected = true;
                    }
            }

            // newRang has a value different from 0 (update ranged request) and is not in the old ranged preferences set,
            // thus add to the set of preferences to save.
            else if(oldRang == null && newRang.getValue() != 0){
                newRangSet.add(newRang);
                isRangUpdDetected = true;
            }
        }

        // Check if update is needed.
        if(!isRangUpdDetected && !isBoolUpdDetected && !isRangDelDetected){
            f = new SavePreferencesNotRequiredMessage();
            f.show(getActivity().getFragmentManager(), "savepreferences_notrequired");
        }
        else {
            // Set tasks to execute. Needed due to synchronization issues.
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

            // Execute tasks.
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

    /**
     * Sets the modality of the boolean preferences set depending on which button was pressed.
     * @param b A view that represents the button that was pressed.
     */
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

    /**
     * Sets the means of the boolean preferences set depending on which button was pressed.
     * @param b A view that represents the button that was pressed.
     */
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
        b.setSelected(!b.isSelected());
    }

    /**
     * Updates the local boolean and ranged preferences sets with the ones contained into the Data class.
     */
    private void getCurrentData(){
        ArrayList<RangedPreference> myRang = myUser.getRangedPreferences();
        BooleanPreferencesSet myBool = myUser.getBoolPreferences();

        this.rangSet = new ArrayList<>(myRang);
        this.boolSet = new BooleanPreferencesSet(myBool);
    }

    /**
     * Updates the whole preferences view with the updated data received from the tasks.
     * It's run by every task after its completion.
     * It only kicks off when the last one is finished.
     */
    private synchronized void updateView(){
        if(isRangUpdFinished && isBoolUpdFinished && isRangDelFinished){
            DialogFragment f;

            mergeRangResults();

            // Show appropriate dialog.
            if(!isTaskErrorDetected){
                // Update data.
                getCurrentData();

                // Update view (useless since view's context is automatically saved, but formally correct).
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

            // Reset task status parameters.
            isRangUpdFinished = false;
            isBoolUpdFinished = false;
            isRangDelFinished = false;
            isTaskErrorDetected = false;
        }
    }

    /**
     * Updates the modes button accordingly to the values of the corresponding preferences.
     */
    private void updateModesView(){
        for(ImageButton b : mModesList) {
            switch (b.getId()) {
                case R.id.mod_1:
                    b.setSelected(boolSet.mode().getValue() == 4);
                    break;
                case R.id.mod_2:
                    b.setSelected(boolSet.mode().getValue() == 3);
                    break;
                case R.id.mod_3:
                    b.setSelected(boolSet.mode().getValue() == 2);
                    break;
                case R.id.mod_4:
                    b.setSelected(boolSet.mode().getValue() == 1);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Updates the ranged preferences edit texts accordingly to the values of the corresponding preferences.
     */
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

    /**
     * Updates the means button accordingly to the values of the corresponding preferences.
     */
    private void updateMeansView(){
        for(ImageButton b : mMeansList){
            switch(b.getId()){
                case R.id.mean_1:
                    b.setSelected(boolSet.personalBike());
                    break;
                case R.id.mean_2:
                    b.setSelected(boolSet.personalCar());
                    break;
                case R.id.mean_3:
                    b.setSelected(boolSet.publicTrasport());
                    break;
                case R.id.mean_4:
                    b.setSelected(boolSet.bikeSharing());
                    break;
                case R.id.mean_5:
                    b.setSelected(boolSet.carSharing());
                    break;
                case R.id.mean_6:
                    b.setSelected(boolSet.uberTaxi());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Set the modes' selectors so to update their background tint accordingly to their selection state.
     */
    private void setModesSelector() {
        int selectorId;

        for (ImageButton b : mModesList) {
            switch(b.getId()){
                case R.id.mod_1:
                    selectorId = R.color.minimize_time_state;
                    break;
                case R.id.mod_2:
                    selectorId = R.color.minimize_cost_state;
                    break;
                case R.id.mod_3:
                    selectorId = R.color.minimize_footprint_state;
                    break;
                case R.id.mod_4:
                    selectorId = R.color.standard_state;
                    break;
                default:
                    selectorId = R.color.minimize_time_state;
            }
            b.setBackgroundTintList(getContext().getResources().getColorStateList(selectorId, null));
        }
    }

    /**
     * Set the means' selector so to update their background tint accordingly to their selection state.
     */
    private void setMeansSelector() {
        for (ImageButton b : mMeansList) {
            b.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.mean_state, null));
        }
    }

    /**
     * Puts together the ranged update and ranged deletion results, maintaining the latest data.
     */
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

    /**
     * A dialog that represents a successful preferences update.
     */
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

    /**
     * A dialog that represents a failure in preferences update.
     */
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

    /**
     * A dialog that represents the superfluity of an update request.
     */
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

    /**
     * Represents an asynchronous preferences update task used to update some ranged preferences.
     */
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
                        vals.add(rp.getValue());
                        ids.add(Integer.toString(rp.getType().getValue()));
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

    /**
     * Represents an asynchronous preferences update task used to update some boolean preferences.
     */
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

    /**
     * Represents an asynchronous preferences update task used to delete some ranged preferences.
     */
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