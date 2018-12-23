package com.huji.cse.flatfinder;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.huji.cse.flatfinder.db.entity.FacebookPost;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RecyclerView mApartmentsRecyclerView;
    private InMapApartmentsAdapter mAdapter;
    private PostViewModel mPostViewModel;
    private List<FacebookPost> mfacebookPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        mPostViewModel.getAllPosts().observe(this, new Observer<List<FacebookPost>>() {
            @Override
            public void onChanged(@Nullable List<FacebookPost> facebookPosts) {
                if (facebookPosts!= null && facebookPosts.size() > 0) {
                    mAdapter.setmApartments(facebookPosts);

                    mfacebookPosts = facebookPosts;
                    updateMap(mfacebookPosts.get(0).getAddress());
                }

            }
        });

        // Add a horizontal RecyclerView for apartments
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        // Initialize recycler view
        mApartmentsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_maps);
        mAdapter = new InMapApartmentsAdapter(this);
        mApartmentsRecyclerView.setAdapter(mAdapter);
        mApartmentsRecyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mApartmentsRecyclerView);
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

        // Set map to update each time the user scrolls to the next item
        mApartmentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mfacebookPosts != null) {
                        FacebookPost currentViewedApartment = mfacebookPosts.get(getCurrentItemPosition());
                        updateMap(currentViewedApartment.getAddress());
                    }
                }
            }
        });
    }

    /**
     * Indicates the currently viewed's item position
     * @return an integer that indicates the currently viewed's item position
     */
    private int getCurrentItemPosition() {
        return ((LinearLayoutManager)mApartmentsRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    /**
     * Updates the map - clears current markers, adds a new marker at the new location, and zooms in
     * to the new location
     * @param newAddress A string the indicates the new location by address
     */
    public void updateMap(String newAddress) {
        mMap.clear();
        Geocoder geoCoder = new Geocoder(this);
        try {
            List<Address> addressList = geoCoder.getFromLocationName(newAddress, 5);
            if(addressList != null) {
                Address location = addressList.get(0);
                location.getLatitude();
                location.getLongitude();

                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                float zoomLevel = 17.0f;
                mMap.addMarker(new MarkerOptions().position(coordinate).title(newAddress));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoomLevel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
