package com.huji.cse.flatfinder;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Add a horizontal RecyclerView for apartments
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // TODO: Each item in RecyclerView should contain: Price, Partners amount, address, picture
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_maps);
        mRecyclerView.setLayoutManager(layoutManager);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geoCoder = new Geocoder(this);
        try {
            List<Address> addressList = geoCoder.getFromLocationName("rehavia", 5);
            if(addressList != null) {
                Address location = addressList.get(0);
                location.getLatitude();
                location.getLongitude();

                LatLng p1 = new LatLng(location.getLatitude(), location.getLongitude());
                float zoomLevel = 17.0f;
                mMap.addMarker(new MarkerOptions().position(p1).title("Marker in Guy's apartment"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p1, zoomLevel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
