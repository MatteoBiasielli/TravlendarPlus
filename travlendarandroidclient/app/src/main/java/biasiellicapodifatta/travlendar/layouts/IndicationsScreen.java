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
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
            }
            return response;
        }

        @Override
        protected void onPostExecute(ResponseTravel responseTravel) {
            mDownload = null;

            if(response == null){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexp-err");
            }

            switch(response.getType()){
                case OK:
                    indicationsText.setText("");
                    for(Route r:response.getRoutes()){
                        indicationsText.append(r.toString());
                        indicationsText.append("\n\n-------------------------------------------------------------\n");
                    }
                    break;
                case TRAVEL_NO_EXISTING_ROUTE:
                    DialogFragment route = new NoRoute();
                    route.show(getFragmentManager(), "no-route");
                    break;
                case TRAVEL_WRONG_INPUT:
                    DialogFragment wrong_input = new WrongInput();
                    wrong_input.show(getFragmentManager(), "wrong-input");
                    break;
                case TRAVEL_NO_ACTIVITY:
                    DialogFragment noAct = new NoActivity();
                    noAct.show(getFragmentManager(), "no-activity");
                    break;
                case TRAVEL_LOGIN_ERROR:
                    DialogFragment login_error = new LoginError();
                    login_error.show(getFragmentManager(), "login-error");
                    break;
                case TRAVEL_CONN_ERROR:
                    DialogFragment conn_error = new ConnectionError();
                    conn_error.show(getFragmentManager(), "conn-error");
                    break;
                default:
                    DialogFragment unexp = new UnexpectedError();
                    unexp.show(getFragmentManager(), "unexp-err");
                    break;
            }

            showProgress(false);
        }
    }

    /**
     * Dialog making the user aware of an unexpected error occurred during the processing
     */
    public static class UnexpectedError extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Unexpected error").
                    setMessage("An unexpected error occurred. You'll be directed to the last valid screen.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), MainTabContainer.class);
                            startActivity(intent);
                            //back to tab container
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
                            Intent intent = new Intent(getActivity(), MainTabContainer.class);
                            startActivity(intent);
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
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            //back to login screen
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware of some problems in the data provided by the client
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
                            Intent intent = new Intent(getActivity(), MainTabContainer.class);
                            startActivity(intent);
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware of some problems in his calendar
     */
    public static class NoRoute extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No Route").
                    setMessage("No paths have been found for your most impending activity due to your preferences")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), MainTabContainer.class);
                            startActivity(intent);
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware of some problems in his calendar
     */
    public static class NoActivity extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No Activity").
                    setMessage("No such activity in your calendar to show a path. First add a new activity.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), MainTabContainer.class);
                            startActivity(intent);
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }
}
