package com.huji.cse.flatfinder;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.design.widget.FloatingActionButton;
import android.net.Uri;
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
        setContentView(R.layout.activity_listing_info);

        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE_KEY);
        mFacebookPost = bundle.getParcelable(Constants.FACEBOOK_KEY);

        fillContent();
    }

    /**
     * reduce the size of the bitmap image before displaying it
     * @param viewID imge view address
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
        loadImage(R.id.pic1,R.drawable.room);

        //image
        ImageView apartmentPic = findViewById(R.id.pic1);

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
    }

    /**
     * a helper function to change the favorite icon on screen
     */
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
}
