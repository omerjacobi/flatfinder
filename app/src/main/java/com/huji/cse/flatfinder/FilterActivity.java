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

    private static final int MAX_ROOMMATE = 5;
    public static final int MAX_PRICE = 10000;
    public static final int MAX_DISTANCE = 10000;
    public static final int PRICE_STEP_SIZE = 100;
    private SeekBar priceSeekBar;
    private SeekBar roommateSeekBar;
    private SeekBar distanceSeekBar;

    private static Place selectedPlace = null;
    private static CharSequence selectedPlaceName;
    private static int selectedRoommateValue = MAX_ROOMMATE;
    private static int selectedPriceValue = MAX_PRICE;
    private static int selectedDistanceValue = MAX_DISTANCE;
    private static boolean selectedOnlyFavorites = false;


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
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedPlace = place;
                selectedPlaceName = place.getName();
                showOrHideDistanceBar(View.VISIBLE);
            }

            @Override
            public void onError(Status status) {
                Log.i("Place", "An error occurred: " + status);
            }
        });
        /* Clear the search for the autocomplete place fragment */
        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPlace = null;
                        autocompleteFragment.setText("");
                    }
                });
        /* restore the last place the user search*/
        if (selectedPlace != null) {
            autocompleteFragment.setText(selectedPlaceName);
            showOrHideDistanceBar(View.VISIBLE);
        }
        else {
            showOrHideDistanceBar(View.INVISIBLE);
        }

        final Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRoommateValue = MAX_ROOMMATE;
                selectedPriceValue = MAX_PRICE;
                selectedDistanceValue = MAX_DISTANCE;
                selectedOnlyFavorites = false;
                initialSeekbarValues(distanceSeekBarValue, priceSeekBarValue, roommatesSeekBarValue);
                selectedPlace = null;
                autocompleteFragment.setText("");
                showOrHideDistanceBar(View.INVISIBLE);

            }
        });


    }

    private void showOrHideDistanceBar(int setTo) {
        TextView distanceSeekBarValue = (TextView)findViewById(R.id.DistanceTextViewValue);
        TextView km=(TextView)findViewById(R.id.distance_km);
        TextView distanceTag=(TextView)findViewById(R.id.DistnaceTextView);
        SeekBar bar = (SeekBar)findViewById(R.id.seekBarDistance);
        distanceTag.setText("Distance from "+selectedPlaceName);
        distanceSeekBarValue.setVisibility(setTo);
        km.setVisibility(setTo);
        bar.setVisibility(setTo);
        distanceTag.setVisibility(setTo);
    }

    /**
     * seek bar initiation values
     */
    private void initialSeekbarValues(TextView distanceSeekBarValue, TextView priceSeekBarValue,
                                      TextView roommatesSeekBarValue) {
        Switch favoriteSwitch = findViewById(R.id.OnlyFavorites);
        favoriteSwitch.setChecked(selectedOnlyFavorites);
        priceSeekBar = findViewById(R.id.seekBarPrice);
        priceSeekBar.setMax(MAX_PRICE);
        priceSeekBar.setProgress(selectedPriceValue);
        roommateSeekBar = findViewById(R.id.seekBarRoommates);
        roommateSeekBar.setMax(MAX_ROOMMATE);
        roommateSeekBar.setProgress(selectedRoommateValue);
        distanceSeekBar = findViewById(R.id.seekBarDistance);
        distanceSeekBar.setMax(MAX_DISTANCE);
        distanceSeekBar.setProgress(selectedDistanceValue);
        priceSeekBarValue.setText(String.valueOf(selectedPriceValue));
        roommatesSeekBarValue.setText(String.valueOf(selectedRoommateValue));
        distanceSeekBarValue.setText(String.valueOf((double)selectedDistanceValue/1000));
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
                selectedDistanceValue = progress;

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
                selectedPriceValue = progress;
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
                selectedRoommateValue = progress;
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
        int roommateValue = roommateSeekBar.getProgress();
        Switch favoriteSwitch = findViewById(R.id.OnlyFavorites);
        selectedOnlyFavorites = favoriteSwitch.isChecked();
        b.putBoolean(Constants.FAVORITES_ONLY_KEY,favoriteSwitch.isChecked());
        b.putInt(Constants.PRICE_VALUE_KEY, priceValue);
        b.putInt(Constants.ROOMMATES_VALUE_KEY, roommateValue);
        i.putExtra(Constants.FILTER_VALUES_KEY,b);
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
            b.putBoolean(Constants.FILTER_DISTANCE_KEY, true);
            b.putDouble(Constants.MIN_LONG_KEY, SphericalUtil.computeOffset(centerCoordination, distanceValue, 270).longitude);
            b.putDouble(Constants.MAX_LONG_KEY, SphericalUtil.computeOffset(centerCoordination, distanceValue, 90).longitude);
            b.putDouble(Constants.MIN_LAT_KEY, SphericalUtil.computeOffset(centerCoordination, distanceValue, 180).latitude);
            b.putDouble(Constants.MAX_LAT_KEY, SphericalUtil.computeOffset(centerCoordination, distanceValue, 0).latitude);
        }
        else
            b.putBoolean(Constants.FILTER_DISTANCE_KEY, false);
    }
}
