package biasiellicapodifatta.travlendar.layouts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biasiellicapodifatta.travlendar.R;

/**
 * Created by Emilio on 29/11/2017.
 */

public class CalendarTab extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.calendar_tab_layout, container, false);
    }
}
