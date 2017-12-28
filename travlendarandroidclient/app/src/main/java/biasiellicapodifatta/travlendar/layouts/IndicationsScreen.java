package biasiellicapodifatta.travlendar.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.travel.Route;
import biasiellicapodifatta.travlendar.network.NetworkLayer;
import biasiellicapodifatta.travlendar.response.responsedeletetag.ResponseDeleteTag;
import biasiellicapodifatta.travlendar.response.responsetravel.ResponseTravel;

public class IndicationsScreen extends AppCompatActivity {

    private DownloadIndicationsTask mDownload = null;

    private TextView indicationsText;
    private View progressView;
    private View indicationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indications_screen);

        indicationsText = findViewById(R.id.indications);

        indicationsView = findViewById(R.id.indicationsView);

        progressView = findViewById(R.id.progressBar);
        showProgress(true);

        downloadDirections();
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

            indicationsView.setVisibility(show ? View.GONE : View.VISIBLE);
            indicationsView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    indicationsView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void downloadDirections(){
        mDownload = new DownloadIndicationsTask(Data.getUser().getUsername(), Data.getUser().getPassword());
        mDownload.execute((Void)null);
    }

    public class DownloadIndicationsTask extends AsyncTask<Void, Void, ResponseTravel>{
        private String username;
        private String password;
        private ResponseTravel response = null;

        DownloadIndicationsTask(String username, String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected ResponseTravel doInBackground(Void... voids) {
            try{
                response = NetworkLayer.travelRequest(username, password);
            }catch (IOException e){
                //TODO
            }
            return response;
        }

        @Override
        protected void onPostExecute(ResponseTravel responseTravel) {
            mDownload = null;

            if(response == null){
                //TODO
            }

            switch(response.getType()){
                case OK:
                    indicationsText.setText("");
                    for(Route r:response.getRoutes()){
                        indicationsText.append(r.toString());
                        indicationsText.append("\n\n-------------------------------------------------------------\n");
                    }
                    //TODO
                    break;
                case TRAVEL_NO_EXISTING_ROUTE:
                    //TODO
                    break;
                case TRAVEL_WRONG_INPUT:
                    //TODO
                    break;
                case TRAVEL_NO_ACTIVITY:
                    //TODO
                    break;
                case TRAVEL_LOGIN_ERROR:
                    //TODO
                    break;
                case TRAVEL_CONN_ERROR:
                    //TODO
                    break;
                default:
                    //TODO
                    break;
            }

            showProgress(false);
        }
    }
}
