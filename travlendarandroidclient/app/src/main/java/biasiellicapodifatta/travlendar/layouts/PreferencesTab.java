package biasiellicapodifatta.travlendar.layouts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biasiellicapodifatta.travlendar.R;

/**
 * Created by Emilio on 03/12/2017.
 */

public class PreferencesTab extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.preferences_tab_layout, container, false);
    }
}
