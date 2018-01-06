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
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.data.Data;
import biasiellicapodifatta.travlendar.network.NetworkLayer;
import biasiellicapodifatta.travlendar.response.responseaddtag.ResponseAddTag;
import biasiellicapodifatta.travlendar.response.responsedeletetag.ResponseDeleteTag;

public class SettingsMenu extends AppCompatActivity {

    private AddTagTask addTagTask = null;
    private DeleteTagTask deleteTagTask = null;

    protected static ArrayList<String> allTags = new ArrayList<>();
    protected static String toDelete;
    protected static Boolean deletion = false;

    private TextView usernameFieldView;
    private EditText addressField;
    private EditText tagField;
    private static EditText selectTagField;
    private View progressView;
    private View settingsView;

    /**
     * Initialize UI components and its listeners by retrieving their id-s
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        for (int i=0; i < Data.getUser().getFavPositions().size(); i++) {
            allTags.add(Data.getUser().getFavPositions().get(i).getTag());
        }

        usernameFieldView = findViewById(R.id.username_field);
        usernameFieldView.setText(Data.getUser().getUsername());

        addressField = findViewById(R.id.adress);

        tagField = findViewById(R.id.tag);

        Button checkTags = findViewById(R.id.checkButton);
        checkTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment list = new TagList();
                list.show(getFragmentManager(), "tag-list");
            }
        });

        selectTagField = findViewById(R.id.selectedTag);
        selectTagField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment list = new DeletionList();
                list.show(getFragmentManager(), "deletion-list");
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deletion){
                    attemptDeletion();
                    deletion = false;
                    selectTagField.setText("");
                }
            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAddition();
            }
        });

        progressView = findViewById(R.id.progressBar);

        settingsView = findViewById(R.id.settingsView);
    }

    /**
     * Retrieves data from each field and attempts to add the new tag
     * connecting to the server
     */
    private void attemptAddition(){
        String tag = tagField.getText().toString();
        String address = addressField.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if(!isTagValid(tag)) {
            tagField.setError("The tag must be just one word.");
            focusView = tagField;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else{
            showProgress(true);
            addTagTask = new AddTagTask(Data.getUser().getUsername(), Data.getUser().getPassword(), address, tag);
            addTagTask.execute((Void)null);
        }

        tagField.setText(R.string.tag_hint);
        addressField.setText(R.string.postal_address);

    }

    /**
     * Attempts to delete the selected tag connecting to the server
     */
    private void attemptDeletion(){
        showProgress(true);
        deleteTagTask = new DeleteTagTask(Data.getUser().getUsername(), Data.getUser().getPassword(), toDelete);
        deleteTagTask.execute((Void) null);
    }

    /**
     * Check if the tag is one word with just uppercase an lowercase letters
     * @param tag
     * @return
     */
    private boolean isTagValid(String tag){
        return tag.matches("([a-z]|[A-Z])+");
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

            settingsView.setVisibility(show ? View.GONE : View.VISIBLE);
            settingsView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    settingsView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    /**
     * Task appointed to connect to the server and add the new tag
     */
    public class AddTagTask extends AsyncTask<Void, Void, ResponseAddTag>{

        private final String username;
        private final String password;
        private final String tag;
        private final String address;
        private ResponseAddTag response;

        AddTagTask(String username, String password, String address, String tag){
            this.address = address;
            this.password = password;
            this.tag = tag;
            this.username = username;
        }

        @Override
        protected ResponseAddTag doInBackground(Void... voids) {
            try{
                response = NetworkLayer.addTagRequest(username, password, tag, address);
            }catch (IOException e){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexpected-error");
            }
            return response;
        }

        @Override
        protected void onPostExecute(final ResponseAddTag response){
            addTagTask = null;
            showProgress(false);

            if(response == null){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexpected-error");
                return;
            }

            switch(response.getType()){
                case OK:
                    //update local tags
                    Data.getUser().setFavPositions(response.getPositions());

                    DialogFragment success = new AddedTag();
                    success.show(getFragmentManager(), "add-success");
                    //TODO: aggiornare allTags
                    SettingsMenu.allTags.add(tag);
                    break;
                case ADD_TAG_LOGIN_ERROR:
                    DialogFragment login = new LoginError();
                    login.show(getFragmentManager(), "login-error");
                    break;
                case ADD_TAG_ALREADY_EXISTING:
                    DialogFragment alreadyEx = new ExistingTag();
                    alreadyEx.show(getFragmentManager(), "already-existing-tag");
                    addressField.setError(response.getType().getMessage());
                    addressField.requestFocus();
                    tagField.setError(response.getType().getMessage());
                    tagField.requestFocus();
                    break;
                case ADD_TAG_WRONG_INPUT:
                    DialogFragment input = new WrongInput();
                    input.show(getFragmentManager(), "wrong-input");
                    break;
                case ADD_TAG_CONNECTION_ERROR:
                    DialogFragment conn = new ConnectionError();
                    conn.show(getFragmentManager(), "connection-error");
                    break;
                default:
                    DialogFragment unexp = new UnexpectedError();
                    unexp.show(getFragmentManager(), "unexpected-error");
                    break;
            }
        }

        @Override
        protected void onCancelled(){
            addTagTask = null;
            showProgress(false);
        }
    }

    /**
     * Task appointed to connect to the server and delete the selected tag
     */
    public class DeleteTagTask extends AsyncTask<Void, Void, ResponseDeleteTag>{

        private final String username;
        private final String password;
        private final String tag;
        private ResponseDeleteTag response;

        DeleteTagTask(String username, String password, String tag){
            this.username = username;
            this.password = password;
            this.tag = tag;
        }

        @Override
        protected ResponseDeleteTag doInBackground(Void... voids) {

            try {
                response = NetworkLayer.deleteTagRequest(username, password, tag);
            }catch (IOException e){
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexpected-error");
            }
            return response;
        }

        @Override
        protected void onPostExecute(ResponseDeleteTag responseDeleteTag) {
            deleteTagTask = null;
            showProgress(false);

            if(response == null) {
                DialogFragment unexp = new UnexpectedError();
                unexp.show(getFragmentManager(), "unexpected-error");
                return;
            }

            switch(response.getType()){
                case OK:
                    //update local tags
                    Data.getUser().setFavPositions(response.getPositions());

                    DialogFragment delete_success = new DeletedTag();
                    delete_success.show(getFragmentManager(), "deletion-success");
                    SettingsMenu.allTags.remove(tag);
                    toDelete = null;
                    break;
                case DELETE_TAG_LOGIN_ERROR:
                    DialogFragment login = new LoginError();
                    login.show(getFragmentManager(), "delete-login-err");
                    break;
                case DELETE_TAG_WRONG_INPUT:
                    DialogFragment input = new WrongInput();
                    input.show(getFragmentManager(), "delete-input-err");
                    break;
                case DELETE_TAG_CONNECTION_ERROR:
                    DialogFragment conn = new ConnectionError();
                    conn.show(getFragmentManager(), "delete-conn-err");
                    break;
                default:
                    DialogFragment unexp = new UnexpectedError();
                    unexp.show(getFragmentManager(), "unexpected-error");
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            addTagTask = null;
            showProgress(false);
        }
    }

    /**
     * Dialog showing user's tags
     */
    public static class TagList extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String[] allTags = new String[SettingsMenu.allTags.size()];

            for (int i=0; i < SettingsMenu.allTags.size(); i++) {
                allTags[i] = SettingsMenu.allTags.get(i);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Your tags")
                    .setItems(allTags, null)
                    .setNeutralButton("Hide", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dismiss this dialog
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog showing user's tags and handling his choice
     */
    public static class DeletionList extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String[] allTags = new String[SettingsMenu.allTags.size()];

            for (int i=0; i < SettingsMenu.allTags.size(); i++) {
                allTags[i] = SettingsMenu.allTags.get(i);
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Your tags")
                    .setItems(allTags, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            toDelete = allTags[which];
                            SettingsMenu.deletion = true;
                            selectTagField.setText(toDelete);
                        }
                    })
                    .setNeutralButton("Hide", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dismiss this dialog
                        }
                    });
            return builder.create();
        }
    }

    /**
     * Dialog making the user aware of an unexpected error occurred during the processing
     */
    public static class UnexpectedError extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Unexpected error").
                    setMessage("An unexpected error occurred. You'll be directed to the last valid screen.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            Intent intent = new Intent(getActivity(), MainTabContainer.class);
                            startActivity(intent);
                            //back to tab container
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware that the new tag has been added successfully
     */
    public static class AddedTag extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Tag added successfully").
                    setMessage("The new tag has been successfully added.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware that the selected tag has been deleted successfully
     */
    public static class DeletedTag extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Tag(s) deleted successfully").
                    setMessage("The tag(s) selected has/have been successfully added.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
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
            builder.setTitle("Login Error").
                    setMessage("An error with your credentials occurred while connecting to the server. You'll be disconnected.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            //back to login
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware of some problems during the addition of the new tag
     */
    public static class ExistingTag extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Existing tag").
                    setMessage("Seems like the tag you're trying to add already exists.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                        }
                    });

            return builder.create();
        }
    }

    /**
     * Dialog used to make the user aware of some problems in the data provided
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
                            //back to form
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
                            //back to form
                        }
                    });

            return builder.create();
        }
    }
}
