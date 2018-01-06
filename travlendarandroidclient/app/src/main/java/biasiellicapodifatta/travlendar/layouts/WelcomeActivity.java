package biasiellicapodifatta.travlendar.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import biasiellicapodifatta.travlendar.R;

/**
 * Created by Emilio on 05/01/2018.
 */

/**
 * The objective of this class is to show a loading screen when Travlendar+ is run.
 */
public class WelcomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        ImageView mTempImage = findViewById(R.id.image_logo);
        ProgressBar mTempBar = findViewById(R.id.welcome_bar);

        mTempBar.setIndeterminate(true);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);
                } catch (Exception e) {

                } finally {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
