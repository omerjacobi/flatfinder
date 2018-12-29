package com.huji.cse.flatfinder;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huji.cse.flatfinder.db.entity.FacebookPost;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;

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

//        FloatingActionButton  backToMainMenu = findViewById(R.id.backToMainMenu);
//
//        //make pop up
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width*.8),(int)(height*.5));
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.gravity = Gravity.TOP;
//        params.x =0;
//        params.y =-20;
//        getWindow().setAttributes(params);

    }
    private void loadImage(int viewID, int imageID) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        options.inJustDecodeBounds = false;
        Bitmap smallBitmap = BitmapFactory.decodeResource(getResources(), imageID);
        ImageView lock = findViewById(viewID);
        lock.setImageBitmap(smallBitmap);
    }

    private void fillContent() {
        loadImage(R.id.pic1,R.drawable.room);

        //image
        ImageView apartmentPic = findViewById(R.id.pic1);
//        Drawable pic = getDrawable(R.drawable.room);
//        apartmentPic.setImageDrawable(pic);

        //address
        TextView apartment_address = findViewById(R.id.apartment_address);
        apartment_address.setText(mFacebookPost.getAddress());

        //price
        TextView apartment_price = findViewById(R.id.apartment_price);
        apartment_price.setText(String.valueOf(mFacebookPost.getPrice()));

        //number of roomates
        TextView rommates=findViewById(R.id.apartment_roommates);
        rommates.setText(String.valueOf(mFacebookPost.getNumOfRoommates()));

        //post message
        TextView apartment_post = findViewById(R.id.apartment_post);
        apartment_post.setText(mFacebookPost.getMessage());

        //contact button
        TextView ContactFacebook = findViewById(R.id.ContactFacebook);

        viewFavoriteStatus();

    }

    public void changeFavoriteStatus(View view) {
        mFacebookPost.setFavorite(!mFacebookPost.isFavorite());
        viewFavoriteStatus();
        updatePostInDB();
    }

    private void viewFavoriteStatus() {
        ImageButton imageButton = findViewById(R.id.favorite_button);
        if (mFacebookPost.isFavorite()) {
            imageButton.setBackgroundResource(R.drawable.glowfillheart);
            android.view.ViewGroup.LayoutParams params = imageButton.getLayoutParams();
            params.height = 66;
            params.width = 40;
            imageButton.setLayoutParams(params);
        } else {
            imageButton.setBackgroundResource(R.drawable.noglowheart);
            android.view.ViewGroup.LayoutParams params = imageButton.getLayoutParams();
            params.height = 75;
            params.width = 44;
            imageButton.setLayoutParams(params);
        }
    }

    private void updatePostInDB() {
        mPostViewModel.insert(mFacebookPost);
    }
}
