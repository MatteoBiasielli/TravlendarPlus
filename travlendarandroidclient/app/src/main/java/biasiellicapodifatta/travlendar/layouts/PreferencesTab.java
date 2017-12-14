package biasiellicapodifatta.travlendar.layouts;

import android.os.AsyncTask;
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

import java.util.ArrayList;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.user.preferences.BooleanPreferencesSet;
import biasiellicapodifatta.travlendar.data.user.preferences.Modality;
import biasiellicapodifatta.travlendar.data.user.preferences.RangedPreference;
import biasiellicapodifatta.travlendar.response.responseupdatebooleanpreferences.ResponseUpdateBooleanPreferences;

/**
 * Created by Emilio on 03/12/2017.
 */

public class PreferencesTab extends Fragment {
    private SavePreferencesTask mSaveTask = null;

    private BooleanPreferencesSet boolSet;
    private ArrayList<RangedPreference> rangSet;

    //UI references
    private ArrayList<ImageButton> mModesList;
    private ArrayList<EditText> mEditTextsList;
    private ArrayList<ImageButton> mMeansList;
    private Button mSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(mSaveTask != null){
            return;
        }

        mSaveTask = new SavePreferencesTask(boolSet, rangSet);
        mSaveTask.execute((Void) null);
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

    private getCurrentData(){
        //TODO implement
    }

    public class UpdateRangedPreferencesTask implements AsyncTask<Void, Void, ResponsePreferences{
        BooleanPreferencesSet bools;
        ArrayList<RangedPreference> rangs;

        UpdateRangedPreferencesTask(BooleanPreferencesSet bs, ArrayList<RangedPreference> rs){
            this.bools = bs;
            this.rangs = rs;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }

        @Override
        protected ResponseLogin doInBackground(Void... params) {
            if(!isOfflineMode()) {
                try {
                    response = NetworkLayer.loginRequest(mUsername, mPassword);
                } catch (IOException e) {
                    return null;
                }
            }
            else {
                response = new ResponseLogin(ResponseLoginType.OK, new User(mUsername, mPassword));
            }

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseLogin response) {
            mAuthTask = null;
            showProgress(false);

            if(response.equals(null)){
                //TODO add pop-up
                return;
            }

            switch (response.getType()){
                case OK:
                    Intent intent = new Intent(LoginActivity.this, MainTabContainer.class);
                    startActivity(intent);
                    break;
                case LOGIN_USERNAME_ERROR:
                    mUsernameView.setError(response.getType().getMessage());
                    mUsernameView.requestFocus();
                    break;
                case LOGIN_PASSWORD_ERROR:
                    mPasswordView.setError(response.getType().getMessage());
                    mPasswordView.requestFocus();
                    break;
                case LOGIN_WRONG_INPUT:
                    mUsernameView.setError(response.getType().getMessage());
                    mUsernameView.requestFocus();
                    break;
                case LOGIN_CONNECTION_ERROR:
                    //TODO add behaviour
                    break;
                default:
                    //TODO add behaviour
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class UpdateBooleanPreferencesTask implements AsyncTask<Void, Void, ResponseUpdateBooleanPreferences>{

    }

}
