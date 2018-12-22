package com.huji.cse.flatfinder;

import android.arch.lifecycle.Observer;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.huji.cse.flatfinder.db.entity.FacebookPost;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity
        extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private RecyclerView mApartmentsRecyclerView;
    private InMapApartmentsAdapter mAdapter;
    private ArrayList<Marker> mMarkers;
    private Marker mCurrentlyViewedMarker;

    private static final float BACKGROUND_MARKER_OPACITY = (float) 0.7;
    private static final float MAIN_MARKER_OPACITY = 1;
    private static final float ZOOM_LEVEL = 14.5f;
    private PostViewModel mPostViewModel;
    private List<FacebookPost> mFacebookPosts;

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
                    mAdapter.notifyDataSetChanged();
                    mFacebookPosts = facebookPosts;
                    refreshMap();
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
        refreshMap();

        // Set map to update each time the user scrolls to the next item
        mApartmentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int itemPosition = getCurrentItemPosition();
                    Marker focusMarker = (mMarkers != null && mMarkers.size() > 0 && itemPosition >= 0) ? mMarkers.get(itemPosition) : null;
                    if (focusMarker != null) {
                        focusOnMarker(focusMarker);
                    }
                }
            }
        });
        mMap.setOnMarkerClickListener(this);
    }

    /**
     * Clears the map and sets new markers on it, according to the currently held Facebook posts
     */
    private void refreshMap() {
        if (mMap != null) {
            mMap.clear();
            mMarkers = generateMarkersFromPosts();
            // Make sure that we have at least one marker, and if so, focus on the first item in the list
            Marker focusMarker = (mMarkers != null && mMarkers.size() > 0) ? mMarkers.get(0) : null;
            if (focusMarker != null) {
                focusOnMarker(focusMarker);
            }
        }
    }

    /**
     * Generates a list of markers from the currently held Facebook posts, and sets each marker on the map
     * @return A list of markers with respective indexes to mFacebookPosts
     */
    private ArrayList<Marker> generateMarkersFromPosts() {
        ArrayList<Marker> markers = new ArrayList<>();
        if (mFacebookPosts != null) {
            Address currentAddress;
            LatLng currentCoordinate;
            Marker currentMarker;

            for (FacebookPost apartment : mFacebookPosts) {
                currentAddress = getAddressFromString(apartment.getAddress());
                if (currentAddress == null) {
                    // TODO: Think about how we deal with this case (no lat-lng for the address)
                }
                currentCoordinate = new LatLng(currentAddress.getLatitude(), currentAddress.getLongitude());
                currentMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(currentCoordinate)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .alpha(BACKGROUND_MARKER_OPACITY)
                );
                markers.add(currentMarker);
            }
        }
        return markers;
    }

    /**
     * Changes focus on the map to a given marker
     * @param marker A marker on the map
     */
    private void focusOnMarker(Marker marker) {
        if (mCurrentlyViewedMarker != null) {
            mCurrentlyViewedMarker.setAlpha(BACKGROUND_MARKER_OPACITY);
            mCurrentlyViewedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }

        // In case the user zoomed in more than our default zoom value, don't re-scale the map
        CameraPosition cameraPosition = (mMap != null) ? mMap.getCameraPosition() : null;
        float zoomLevel = (cameraPosition != null) ? cameraPosition.zoom : -1;
        zoomLevel = (zoomLevel > ZOOM_LEVEL) ? zoomLevel : ZOOM_LEVEL;

        marker.setAlpha(MAIN_MARKER_OPACITY);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoomLevel));
        mCurrentlyViewedMarker = marker;
    }

    /**
     * Generates an Address object from a string that represents an address
     * @param addressStr string representation of an address
     * @return Address object that represents addressStr
     */
    private Address getAddressFromString(String addressStr) {
        Geocoder geocoder = new Geocoder(this);
        Address address;
        try {
            List<Address> addressList = geocoder.getFromLocationName(addressStr, 5);
            if (addressList != null) {
                address = addressList.get(0);
                address.getLatitude();
                address.getLongitude();
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Indicates the currently viewed's item position
     * @return an integer that indicates the currently viewed's item position
     */
    private int getCurrentItemPosition() {
        return ((LinearLayoutManager)mApartmentsRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        int markerIndex = mMarkers.indexOf(marker);
        mAdapter.notifyItemChanged(markerIndex);
        mApartmentsRecyclerView.getLayoutManager().scrollToPosition(markerIndex);
        focusOnMarker(marker);
        return true;
    }

}