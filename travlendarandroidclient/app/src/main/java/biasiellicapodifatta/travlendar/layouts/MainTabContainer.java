package biasiellicapodifatta.travlendar.layouts;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.adapters.TabAdapter;

public class MainTabContainer extends AppCompatActivity {
    ViewPager simpleViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_container_layout);

        simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        //Map tab declaration
        TabLayout.Tab mapTab = tabLayout.newTab();
        mapTab.setIcon(android.R.drawable.ic_menu_mapmode);
        tabLayout.addTab(mapTab);

        //Calendar tab declaration
        TabLayout.Tab calendarTab = tabLayout.newTab();
        calendarTab.setIcon(android.R.drawable.ic_menu_my_calendar);
        tabLayout.addTab(calendarTab);

        //Preferences tab declaration
        TabLayout.Tab preferencesTab = tabLayout.newTab();
        preferencesTab.setIcon(android.R.drawable.ic_menu_edit);
        tabLayout.addTab(preferencesTab);

        TabAdapter adapter = new TabAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);
        // addOnPageChangeListener event change the tab on slide
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

}
