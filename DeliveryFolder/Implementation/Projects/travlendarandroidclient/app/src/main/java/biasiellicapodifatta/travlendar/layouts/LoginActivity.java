package biasiellicapodifatta.travlendar.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.data.user.User;
import biasiellicapodifatta.travlendar.network.NetworkLayer;
import biasiellicapodifatta.travlendar.response.responselogin.ResponseLogin;
import biasiellicapodifatta.travlendar.response.responselogin.ResponseLoginType;

/**
 * This class offers a login screen that manages an authentication procedure via username/password.
 */
public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mRegistrationFormView;
    private EditText mIPView;
    private static CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // Get button references and set listeners.
        Button mUsernameSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegistrationSignInButton = (Button) findViewById(R.id.username_register_button);
        mRegistrationSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistrationForm();
            }
        });

        // Get missing UI references.
        mLoginFormView = findViewById(R.id.login_form);
        mRegistrationFormView = findViewById(R.id.username_register_button);
        mProgressView = findViewById(R.id.login_progress);
        mIPView = findViewById(R.id.ip_textbox);
        mCheckBox = findViewById(R.id.offline_checkBox);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mIPView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String ip = mIPView.getText().toString();
        Data.setOfflineMode(mCheckBox.isChecked());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (!isUsernameValid(username)) {
            mUsernameView.setError("This is not a valid username.");
            focusView = mUsernameView;
            cancel = true;
        }

        if(!Data.isOfflineMode()) {
            // Check for a valid ip.
            if (!isIPValid(ip)) {
                mIPView.setError("This is not a valid ip.");
                focusView = mIPView;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            NetworkLayer.setIP(ip);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Launches the registration activity.
     */
    private void showRegistrationForm() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isUsernameValid(String username) {
        return username.matches("([a-z]|[A-Z])+");
    }

    private boolean isPasswordValid(String password) {
        return password.matches("([a-z]|[A-Z])+");
    }

    private boolean isIPValid(String ip){
        return ip.matches("^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}$");
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Shows a login error dialog.
     */
    private void showLoginError(){
        DialogFragment df = new LoginError();
        df.show(getFragmentManager(), "login_error");
    }

    /**
     * A dialog shown in case of login errors.
     */
    public static class LoginError extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Login error").
                    setMessage("An unexpected error occurred. Please try again in a while.")
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
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, ResponseLogin> {

        private final String mUsername;
        private final String mPassword;
        ResponseLogin response;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }

        @Override
        protected ResponseLogin doInBackground(Void... params) {
            if(!Data.isOfflineMode()) {
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

            if(response == null){
                showLoginError();
                return;
            }

            switch (response.getType()){
                case OK:
                    Data.setUser(response.getUser());
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
                    showLoginError();
                    break;
                default:
                    showLoginError();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}