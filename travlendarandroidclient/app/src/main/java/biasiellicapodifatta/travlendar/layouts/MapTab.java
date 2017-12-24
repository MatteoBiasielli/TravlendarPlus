package biasiellicapodifatta.travlendar.layouts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import biasiellicapodifatta.travlendar.R;

/**
 * Created by Emilio on 29/11/2017.
 */

public class MapTab extends Fragment implements OnMapReadyCallback {
    private static final int MIN_ZOOM_LEVEL = 10;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_tab_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map == null) {
            FragmentManager fm = getFragmentManager();
            SupportMapFragment supMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            supMap.getMapAsync(this);

            if (map != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        LatLng milan = new LatLng(45.465454, 9.186515999999983);
        map.addMarker(new MarkerOptions().position(milan).title("Marker in Milan"));
        map.moveCamera(CameraUpdateFactory.newLatLng(milan));
        map.setMinZoomPreference(MIN_ZOOM_LEVEL);
    }
}