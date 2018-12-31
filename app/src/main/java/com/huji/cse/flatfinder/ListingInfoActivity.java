package com.huji.cse.flatfinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListingInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_info);
        fillContent();
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
        loadImage(R.id.favoriteSelected,R.drawable.glowfillheart);
//        loadImage(R.id.favoriteUnselected,R.drawable.noglowheart);

        //image
        ImageView apartmentPic = findViewById(R.id.pic1);
//        Drawable pic = getDrawable(R.drawable.room);
//        apartmentPic.setImageDrawable(pic);

        //address
        TextView apartment_address = findViewById(R.id.apartment_address);
        apartment_address.setText("Azza 23, Jerusalem");

        //price
        TextView apartment_price = findViewById(R.id.apartment_price);
        apartment_price.setText("2100 nis");

        //number of roomates
        TextView rommates=findViewById(R.id.apartment_roommates);
        rommates.setText("3");

        //post message
        TextView apartment_post = findViewById(R.id.apartment_post);
        apartment_post.setText("hey looking for a new roommate for my awesome apartemnt." +
                "\n price:250 nis\n address: azza 30 Jerusalem\n number of roommates: 2\ncome see the" +
                "apartment tomorrow night at 1700\nno pets please!\nalso great locaion very " +
                "close to supermarkets,bus stations etc...");

        //contact button
        TextView ContactFacebook = findViewById(R.id.facebook_contact);

    }

    public void selectFavorite(View view) {
        ImageView selectFavorite=findViewById(R.id.favoriteSelected);
        selectFavorite.setVisibility(View.VISIBLE);
//        ImageView unselectFavorite=findViewById(R.id.favoriteUnselected);
//        unselectFavorite.setVisibility(View.INVISIBLE);

        //todo select favorite
    }

    public void unselectFavorite(View view) {
        ImageView selectFavorite=findViewById(R.id.favoriteSelected);
        selectFavorite.setVisibility(View.INVISIBLE);
//        ImageView unselectFavorite=findViewById(R.id.favoriteUnselected);
//        unselectFavorite.setVisibility(View.VISIBLE);

        //todo un-select favorite
    }
}
