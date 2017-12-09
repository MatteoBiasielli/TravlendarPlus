package biasiellicapodifatta.travlendar.layouts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.user.User;
import biasiellicapodifatta.travlendar.response.responselogin.ResponseLogin;
import biasiellicapodifatta.travlendar.response.responselogin.ResponseLoginType;
import biasiellicapodifatta.travlendar.response.responsetravel.ResponseTravel;

/**
 * Created by Emilio on 29/11/2017.
 */

public class MapTab extends Fragment implements OnMapReadyCallback {
    private static final int MIN_ZOOM_LEVEL = 10;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_tab_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map == null) {
            FragmentManager fm = getFragmentManager();
            SupportMapFragment supMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            supMap.getMapAsync(this);

            if (map != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        LatLng milan = new LatLng(45.465454, 9.186515999999983);
        map.addMarker(new MarkerOptions().position(milan).title("Marker in Milan"));
        map.moveCamera(CameraUpdateFactory.newLatLng(milan));
        map.setMinZoomPreference(MIN_ZOOM_LEVEL);
    }
}

    /*public class ItineraryComputationTask extends AsyncTask<Void, Void, ResponseTravel> {
        private final String mUsername;
        private final String mPassword;
        ResponseLogin response;

        ItineraryComputationTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }

        @Override
        protected ResponseTravel doInBackground(Void... params) {

//            try {
//                response = NetworkLayer.loginRequest(mUsername, mPassword);
//            } catch (IOException e) {
//                return null;
//            }

            response = new ResponseLogin(ResponseLoginType.OK, new User(mUsername, mPassword));

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
}*/
