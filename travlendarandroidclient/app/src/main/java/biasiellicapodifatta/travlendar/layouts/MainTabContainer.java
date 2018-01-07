package biasiellicapodifatta.travlendar.layouts;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import biasiellicapodifatta.travlendar.R;
import biasiellicapodifatta.travlendar.adapters.TabAdapter;

/**
 * This class represents the activity that will host the tabs of the application.
 */
public class MainTabContainer extends AppCompatActivity {
    ViewPager simpleViewPager;
    TabLayout tabLayout;
    FloatingActionButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_container_layout);

        // Get UI references.
        simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        menuButton = (FloatingActionButton) findViewById(R.id.menu_button);

        // Map tab declaration.
        TabLayout.Tab mapTab = tabLayout.newTab();
        mapTab.setIcon(android.R.drawable.ic_menu_mapmode);
        tabLayout.addTab(mapTab);

        // Calendar tab declaration.
        TabLayout.Tab calendarTab = tabLayout.newTab();
        calendarTab.setIcon(android.R.drawable.ic_menu_my_calendar);
        tabLayout.addTab(calendarTab);

        // Preferences tab declaration.
        TabLayout.Tab preferencesTab = tabLayout.newTab();
        preferencesTab.setIcon(android.R.drawable.ic_menu_edit);
        tabLayout.addTab(preferencesTab);

        // Set menu button listener.
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTabContainer.this, SettingsMenu.class);
                startActivity(intent);
            }
        });

        // Adapter declaration.
        TabAdapter adapter = new TabAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);

        // Set listener to change tabs.
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set initial tab.
        simpleViewPager.setCurrentItem(calendarTab.getPosition());
    }

}