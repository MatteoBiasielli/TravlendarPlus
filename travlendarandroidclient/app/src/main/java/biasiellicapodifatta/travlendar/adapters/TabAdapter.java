package biasiellicapodifatta.travlendar.adapters;

/**
 * Created by Emilio on 29/11/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.layouts.CalendarTab;
import biasiellicapodifatta.travlendar.layouts.MapTrial;
import biasiellicapodifatta.travlendar.layouts.PreferencesTab;

public class TabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MapTrial tab1 = new MapTrial();
                return tab1;
            case 1:
                CalendarTab tab2 = new CalendarTab();
                return tab2;
            case 2:
                PreferencesTab tab3 = new PreferencesTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
