package biasiellicapodifatta.travlendar.layouts;

import android.content.Intent;
import android.os.AsyncTask;
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

    private TextView indicationsText;
    private View progressView;
    private View indicationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indications_screen);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        indicationsText = findViewById(R.id.indications);

        progressView = findViewById(R.id.progressBar);

        indicationsView = findViewById(R.id.indicationsView);

        downloadDirections();
    }

    private void downloadDirections(){
        ArrayList<String> directions;
        ResponseTravel response = null;

        try {
            response = NetworkLayer.travelRequest(Data.getUser().getUsername(), Data.getUser().getPassword());
        }catch (IOException e){
            //TODO
        }

        if(response == null){
            //TODO
        }

        switch(response.getType()){
            case OK:
                indicationsText.setText("");
                for(Route r:response.getRoutes()){
                    indicationsText.append(r.toString());
                    indicationsText.append("-------------------------");
                }
                //TODO
                break;
            case TRAVEL_CONN_ERROR:
                //TODO
                break;
            case TRAVEL_LOGIN_ERROR:
                //TODO
                break;
            case TRAVEL_NO_ACTIVITY:
                //TODO
                break;
            case TRAVEL_WRONG_INPUT:
                //TODO
                break;
            case TRAVEL_NO_EXISTING_ROUTE:
                //TODO
                break;
            default:
                //TODO
                break;
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainTabContainer.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
