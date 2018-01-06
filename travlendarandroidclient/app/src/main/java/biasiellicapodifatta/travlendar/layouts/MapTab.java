package biasiellicapodifatta.travlendar.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import biasiellicapodifatta.travlendar.R;

/**
 * Created by Emilio on 29/11/2017.
 */

/**
 * This class represents a tab in which the user can interact with a map.
 */
public class MapTab extends Fragment implements OnMapReadyCallback {
    private static final int MIN_ZOOM_LEVEL = 10;
    private GoogleMap map;

    private ImageButton mComputeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.map_tab_layout, container, false);

        mComputeButton = mapView.findViewById(R.id.compute_button);

        mComputeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), IndicationsScreen.class);
                startActivity(intent);
            }
        });

        return mapView;
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

        //Constraining view to the area of Milan.
        map.setMinZoomPreference(MIN_ZOOM_LEVEL);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(45.4088700, 9.1256500));
        builder.include(new LatLng(45.5332900, 9.2258500));
        LatLngBounds bounds = builder.build();

        map.setLatLngBoundsForCameraTarget(bounds);
        map.moveCamera(CameraUpdateFactory.newLatLng(milan));
    }
}