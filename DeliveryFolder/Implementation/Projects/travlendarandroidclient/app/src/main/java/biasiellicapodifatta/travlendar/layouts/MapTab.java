package biasiellicapodifatta.travlendar.layouts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import biasiellicapodifatta.travlendar.R;

/**
 * Created by Emilio on 29/11/2017.
 */

/**
 * This class represents a tab in which the user can interact with a map.
 */
public class MapTab extends Fragment implements OnMapReadyCallback {
    private static final int MIN_ZOOM_LEVEL = 10;
    private static final double LAT_MIN_BOUND = 45.3578100;
    private static final double LAT_MAX_BOUND = 45.5717600;
    private static final double LNG_MIN_BOUND = 9.0268100;
    private static final double LNG_MAX_BOUND = 9.3236000;

    private GoogleMap map;

    // UI references.
    private ImageButton mSearchButton;
    private ImageButton mComputeButton;
    private EditText mSearchBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.map_tab_layout, container, false);

        // Get UI references.
        mSearchBar = mapView.findViewById(R.id.search_bar);
        mComputeButton = mapView.findViewById(R.id.compute_button);
        mSearchButton = mapView.findViewById(R.id.search_button);

        // Set listeners.
        mComputeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), IndicationsScreen.class);
                startActivity(intent);
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String g = mSearchBar.getText().toString();

                Geocoder geocoder = new Geocoder(getActivity().getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input text
                    addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null && !addresses.equals(""))
                        showSearchResult(addresses);

                } catch (Exception e) {
                    DialogFragment fm = new NoResultDialog();
                    fm.show(getActivity().getFragmentManager(), "no_result");
                }

            }
        });

        return mapView;
    }

    /**
     * Shows the result of the user search query.
     * @param addresses A list of addresses that match the previous search.
     */
    protected void showSearchResult(List<Address> addresses) {
        ArrayList<MarkerOptions> markers = new ArrayList<>();

        // Analyze results.
        if(addresses.size() >= 1) {
            LatLng firstLatLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            MarkerOptions firstMarkerOptions = new MarkerOptions();
            firstMarkerOptions.position(firstLatLng);
            if(isWithinBounds(firstLatLng)) {
                markers.add(firstMarkerOptions);
            }
        }
        if(addresses.size() >= 2) {
            LatLng secondLatLng = new LatLng(addresses.get(1).getLatitude(), addresses.get(1).getLongitude());
            MarkerOptions secondMarkerOptions = new MarkerOptions();
            secondMarkerOptions.position(secondLatLng);
            if(isWithinBounds(secondLatLng)) {
                markers.add(secondMarkerOptions);
            }
        }
        if(addresses.size() == 3) {
            LatLng thirdLatLng = new LatLng(addresses.get(2).getLatitude(), addresses.get(2).getLongitude());
            MarkerOptions thirdMarkerOptions = new MarkerOptions();
            thirdMarkerOptions.position(thirdLatLng);
            if(isWithinBounds(thirdLatLng)) {
                markers.add(thirdMarkerOptions);
            }
        }

        if(!markers.isEmpty()) {
            // Add markers to the map and zoom to the first result.
            map.clear();
            for(MarkerOptions m : markers) {
                map.addMarker(m);
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(markers.get(0).getPosition()));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        else{
            // Show a warning dialog.
            DialogFragment fm = new NoResultDialog();
            fm.show(getActivity().getFragmentManager(), "no_result");
        }
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
        builder.include(new LatLng(LAT_MIN_BOUND, LNG_MAX_BOUND));
        builder.include(new LatLng(LAT_MAX_BOUND, LNG_MIN_BOUND));
        LatLngBounds bounds = builder.build();
        map.setLatLngBoundsForCameraTarget(bounds);

        map.moveCamera(CameraUpdateFactory.newLatLng(milan));
    }

    /**
     * Checks that the given place is within the bounds of the interesting portion of the map.
     * @param place The place to check the position of.
     * @return true, if the place is within bounds; false, otherwise.
     */
    private static boolean isWithinBounds(LatLng place){
        if(place.latitude < LAT_MIN_BOUND || place.latitude > LAT_MAX_BOUND || place.longitude < LNG_MIN_BOUND || place.longitude > LNG_MAX_BOUND){
            return false;
        }
        return true;
    }

    /**
     * A dialog shown in case of login errors.
     */
    public static class NoResultDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No result").
                    setMessage("No result in the area of Milan or surrounding Milan.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Close pop-up
                        }
                    });

            return builder.create();
        }
    }
}