package com.huji.cse.flatfinder;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.design.widget.FloatingActionButton;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huji.cse.flatfinder.db.entity.FacebookPost;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class FlatInfoActivity extends FragmentActivity {

    private FacebookPost mFacebookPost;
    private PostViewModel mPostViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_info);

        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE_KEY);
        mFacebookPost = bundle.getParcelable(Constants.FACEBOOK_KEY);

        fillContent();
    }

    /**
     * reduce the size of the bitmap image before displaying it
     * @param viewID image view address
     * @param imageID drawable image id
     */
    private void loadImage(int viewID, int imageID) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        options.inJustDecodeBounds = false;
        Bitmap smallBitmap = BitmapFactory.decodeResource(getResources(), imageID);
        ImageView lock = findViewById(viewID);
        lock.setImageBitmap(smallBitmap);
    }

    /**
     * set the content value of the apartment information fields
     */
    private void fillContent() {
        // After we finish Parser we will do
        //List<String> lst = apartment.getPic;
        List<String> lst = new ArrayList<>();
        // Add a horizontal RecyclerView for apartments
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Initialize recycler view
        RecyclerView mApartmentPicturesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_photos);
        imageAdapter mAdapter = new imageAdapter(this,mFacebookPost.getPicture());
        mApartmentPicturesRecyclerView.setAdapter(mAdapter);
        mApartmentPicturesRecyclerView.setLayoutManager(layoutManager);
        mApartmentPicturesRecyclerView.setClipToPadding(false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mApartmentPicturesRecyclerView);


        mApartmentPicturesRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration());

        //address
        TextView apartment_address = findViewById(R.id.apartment_address);
        apartment_address.setText(mFacebookPost.getAddress());

        //price
        TextView apartment_price = findViewById(R.id.apartment_price);
        apartment_price.setText(String.valueOf(mFacebookPost.getPrice()));

        //number of roommates
        TextView roommates=findViewById(R.id.apartment_roommates);
        roommates.setText(String.valueOf(mFacebookPost.getNumOfRoommates()));

        //post message
        TextView apartment_post = findViewById(R.id.apartment_post);
        apartment_post.setText(mFacebookPost.getMessage());


        viewFavoriteStatus();

    }

    /**
     * change the status of the object favorite boolean
     */
    public void changeFavoriteStatus(View view) {
        mFacebookPost.setFavorite(!mFacebookPost.isFavorite());
        viewFavoriteStatus();
        updatePostInDB();
        String message;
        if(mFacebookPost.isFavorite())
            message="added to favorites";
        else
            message="removed from favorites";
        showFavoriteMessage(message);

    }

    private void showFavoriteMessage(String message) {

        Toast toast = Toast.makeText(getApplicationContext(),
                message,
                (8 * (Toast.LENGTH_LONG)));
        toast.setGravity(Gravity.TOP, 0,  3);

        toast.show();
    }

    /**
     * a helper function to change the favorite icon on screen
     */
    private void viewFavoriteStatus() {
        ImageButton imageButton = findViewById(R.id.favorite_button);
        imageButton.bringToFront();
        if (mFacebookPost.isFavorite()) {
            imageButton.setBackgroundResource(R.drawable.ic_favorite);
            android.view.ViewGroup.LayoutParams params = imageButton.getLayoutParams();
            params.height = 130;
            params.width = 100;
            imageButton.setLayoutParams(params);
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_notfavorite);
            android.view.ViewGroup.LayoutParams params = imageButton.getLayoutParams();
            params.height = 130;
            params.width = 100;
            imageButton.setLayoutParams(params);
        }
    }

    /**
     * go to the post in the facebook site
     */
    public void goToPostInFacebook(View view){
        String s = mFacebookPost.getId();
        int underscore=s.indexOf('_');
        String groupID=s.substring(0,underscore);
        String messageID=s.substring(underscore+1);
        String address = "https://www.facebook.com/groups/" + groupID + "/permalink/" + messageID;
        Intent facebookBrowser = new Intent(Intent.ACTION_VIEW,Uri.parse(address));
        startActivity(facebookBrowser);
    }

    private void updatePostInDB() {
        mPostViewModel.insert(mFacebookPost);
    }

    public void makePlantGreen(View view) {
        ImageView greyPlant=(ImageView)findViewById(R.id.plant);
        ImageView greenPlant=(ImageView)findViewById(R.id.plant_green);
        greenPlant.setVisibility(View.VISIBLE);
        greyPlant.setVisibility(View.INVISIBLE);
    }

    public void makePlantGrey(View view) {
        ImageView greyPlant=(ImageView)findViewById(R.id.plant);
        ImageView greenPlant=(ImageView)findViewById(R.id.plant_green);
        greenPlant.setVisibility(View.INVISIBLE);
        greyPlant.setVisibility(View.VISIBLE);
    }
}
