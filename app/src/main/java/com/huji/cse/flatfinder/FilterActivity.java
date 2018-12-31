package com.huji.cse.flatfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class FilterActivity extends AppCompatActivity {

    public static final int MAX_ROOMMATE = 5;
    public static final int MAX_PRICE = 10000;
    public static final int MAX_DISTANCE = 10000;
    public static final int PRICE_STEP_SIZE = 100;
    private SeekBar priceSeekBar;
    private SeekBar roommateSeekBar;
    private SeekBar distanceSeekBar;
    private Place selectedPlace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        final TextView distanceSeekBarValue = (TextView)findViewById(R.id.DistanceTextViewValue);
        final TextView priceSeekBarValue = (TextView)findViewById(R.id.priceValueTextView);
        final TextView roommatesSeekBarValue = (TextView)findViewById(R.id.roommateValueTextView);

        initialSeekbarValues(distanceSeekBarValue, priceSeekBarValue, roommatesSeekBarValue);
        seekbarToTextviewBounding(distanceSeekBarValue, priceSeekBarValue, roommatesSeekBarValue);
        /* a listener for the done button*/
        final Button doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoneButtonClicked();
            }
        });
        /* autocomplete fragment to get the area the user want to search from*/
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedPlace = place;
            }

            @Override
            public void onError(Status status) {
                Log.i("Place", "An error occurred: " + status);
            }
        });


    }

    /**
     * seek bar initiation values
     */
    private void initialSeekbarValues(TextView distanceSeekBarValue, TextView priceSeekBarValue,
                                      TextView roommatesSeekBarValue) {
        priceSeekBar = findViewById(R.id.seekBarPrice);
        priceSeekBar.setMax(MAX_PRICE);
        priceSeekBar.setProgress(MAX_PRICE);
        roommateSeekBar = findViewById(R.id.seekBarRoommates);
        roommateSeekBar.setMax(MAX_ROOMMATE);
        roommateSeekBar.setProgress(MAX_ROOMMATE);
        distanceSeekBar = findViewById(R.id.seekBarDistance);
        distanceSeekBar.setMax(MAX_DISTANCE);
        distanceSeekBar.setProgress(MAX_DISTANCE);

        priceSeekBarValue.setText(String.valueOf(MAX_PRICE));
        roommatesSeekBarValue.setText(String.valueOf(MAX_ROOMMATE));
        distanceSeekBarValue.setText(String.valueOf((double)MAX_DISTANCE/1000));
    }

    /**
     * bounding the seekbar to the textview so the user can see the values of the bar
     * @param distanceSeekBarValue distance from center seekbar
     * @param priceSeekBarValue max price seekbar
     * @param roommatesSeekBarValue max roommates seekbar
     */
    private void seekbarToTextviewBounding(final TextView distanceSeekBarValue,
                                           final TextView priceSeekBarValue,
                                           final TextView roommatesSeekBarValue) {
        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress / PRICE_STEP_SIZE;
                progress = progress * PRICE_STEP_SIZE;
                distanceSeekBarValue.setText(String.valueOf((double)progress/1000));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //To show only price jump in size of PRICE_STEP_SIZE
                progress = progress / PRICE_STEP_SIZE;
                progress = progress * PRICE_STEP_SIZE;
                priceSeekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        roommateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                roommatesSeekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
    }

    /**
     * after pressing done in the filter windows, we calculate the parametres the user inserted and
     * putting them to a bundle that we send back
     */
    private void DoneButtonClicked()
    {

        Intent i = new Intent(FilterActivity.this, MapsActivity.class);
        Bundle b = new Bundle();

        CalculateFilterSearchAreaBoundary(b);

        int priceValue = priceSeekBar.getProgress();
        int rooommateValue = roommateSeekBar.getProgress();
        Switch favoriteSwitch = findViewById(R.id.OnlyFavorites);
        b.putBoolean("onlyFavorites",favoriteSwitch.isChecked());
        b.putInt("priceValue", priceValue);
        b.putInt("roommateValue", rooommateValue);
        i.putExtra("filterValues",b);
        startActivity(i);
        /* and animation for the transition between map and filter activity*/
        overridePendingTransition(R.anim.slide_in,R.anim.slide_static);

    }

    /**
     * calculate the max long and lat, and min long and lat using the selected place as the center
     * and adding the seekbar distance to the north, south, west and east of that point
     * adding that to a bundle that send back to the map activity
     * @param b the bundle object
     */
    private void CalculateFilterSearchAreaBoundary(Bundle b) {
        int distanceValue = distanceSeekBar.getProgress();
        if (selectedPlace!= null) {
            LatLng centerCoordination = selectedPlace.getLatLng();
            b.putBoolean("filterDistance", true);
            b.putDouble("minLong", SphericalUtil.computeOffset(centerCoordination, distanceValue, 270).longitude);
            b.putDouble("maxLong", SphericalUtil.computeOffset(centerCoordination, distanceValue, 90).longitude);
            b.putDouble("minLat", SphericalUtil.computeOffset(centerCoordination, distanceValue, 180).latitude);
            b.putDouble("maxLat", SphericalUtil.computeOffset(centerCoordination, distanceValue, 0).latitude);
        }
        else
            b.putBoolean("filterDistance", false);
    }
}
