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
import android.widget.Button;
import android.widget.EditText;
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
    protected static ArrayList<Integer> selectedTags = new ArrayList<>();
    protected static Boolean deletion = false;

    private TextView usernameFieldView;
    private EditText addressField;
    private EditText tagField;
    private View progressView;
    private View settingsView;


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
                if(deletion){
                    attemptDeletion();
                    deletion = false;
                    selectedTags = null;
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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainTabContainer.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

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

    private void attemptDeletion(){
        for(Integer n : selectedTags) {
            deleteTagTask = new DeleteTagTask(Data.getUser().getUsername(), Data.getUser().getPassword(), n);
            deleteTagTask.execute((Void) null);
        }
    }

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
            }

            switch(response.getType()){
                case OK:
                    DialogFragment success = new AddedTag();
                    success.show(getFragmentManager(), "add-success");
                    //TODO: aggiornare allTags
                    SettingsMenu.allTags.add(tag);
                    break;
                case ADD_TAG_LOGIN_ERROR:
                    DialogFragment login = new LoginError();
                    login.show(getFragmentManager(), "login-error");
                    Intent intent1 = new Intent(SettingsMenu.this, LoginActivity.class);
                    startActivity(intent1);
                    break;
                case ADD_TAG_ALREADY_EXISTING:
                    DialogFragment alreadyEx = new ExistingTag();
                    alreadyEx.show(getFragmentManager(), "already-existing-tag");
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

    public class DeleteTagTask extends AsyncTask<Void, Void, ResponseDeleteTag>{

        private final String username;
        private final String password;
        private final Integer tag;
        private ResponseDeleteTag response;

        DeleteTagTask(String username, String password, Integer index){
            this.username = username;
            this.password = password;
            this.tag = index;
        }

        @Override
        protected ResponseDeleteTag doInBackground(Void... voids) {

            try {
                response = NetworkLayer.deleteTagRequest(username, password, SettingsMenu.allTags.get(tag));
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
            }

            switch(response.getType()){
                case OK:
                    DialogFragment delete_success = new DeletedTag();
                    delete_success.show(getFragmentManager(), "deletion-success");
                    //TODO: aggiornare allTags
                    SettingsMenu.allTags.remove(tag);
                    break;
                case DELETE_TAG_LOGIN_ERROR:
                    DialogFragment login = new LoginError();
                    login.show(getFragmentManager(), "delete-login-err");
                    Intent intent1 = new Intent(SettingsMenu.this, LoginActivity.class);
                    startActivity(intent1);
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

    public static class TagList extends DialogFragment{
        private ArrayList<Integer> selectedTags;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            selectedTags = new ArrayList<>();
            final String[] allTags = new String[SettingsMenu.allTags.size()];

            for (int i=0; i < SettingsMenu.allTags.size(); i++) {
                allTags[i] = SettingsMenu.allTags.get(i);
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Your tags")
                    .setMultiChoiceItems(allTags, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                            //TODO: problema nel visualizzare la lista
                            if (isChecked) {
                                selectedTags.add(which);
                            } else if (selectedTags.contains(which)) {
                                selectedTags.remove(which);
                            }
                        }
                    })
                    .setPositiveButton("Delete tag[s]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SettingsMenu.deletion = true;
                            SettingsMenu.selectedTags.addAll(selectedTags);
                            selectedTags = null;
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

    public static class UnexpectedError extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Unexpected error").
                    setMessage("An unexpected error occurred. You'll be directed to the last valid screen.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                        }
                    });

            return builder.create();
        }
    }

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

    public static class LoginError extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Login Error").
                    setMessage("An error with your credentials occurred while connecting to the server. You'll be disconnected.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                        }
                    });

            return builder.create();
        }
    }

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
