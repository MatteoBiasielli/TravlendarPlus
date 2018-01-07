package biasiellicapodifatta.travlendar.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import biasiellicapodifatta.travlendar.layouts.CalendarTab;
import biasiellicapodifatta.travlendar.layouts.PreferencesTab;
import biasiellicapodifatta.travlendar.layouts.MapTab;

/**
 * Created by Emilio on 29/11/2017.
 */

/**
 * The objective of this class is to inject the tabs contained in the TabAdapter of the MainTabContainer class.
 */
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
                MapTab tab1 = new MapTab();
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
