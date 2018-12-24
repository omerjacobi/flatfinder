package com.huji.cse.flatfinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
//import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class flat_info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_info);
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
        loadImage(R.id.favoriteSelected,R.drawable.glowfillheart);
        loadImage(R.id.favoriteUnselected,R.drawable.noglowheart);

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
                " \n price:250 nis\n address: azza 30 Jerusalem\n number of roommates: 2");

        //contact button
        TextView ContactFacebook = findViewById(R.id.ContactFacebook);

    }

    public void selectFavorite(View view) {
        ImageView selectFavorite=findViewById(R.id.favoriteSelected);
        selectFavorite.setVisibility(View.VISIBLE);
        ImageView unselectFavorite=findViewById(R.id.favoriteUnselected);
        unselectFavorite.setVisibility(View.INVISIBLE);

        //todo select favorite
    }

    public void unselectFavorite(View view) {
        ImageView selectFavorite=findViewById(R.id.favoriteSelected);
        selectFavorite.setVisibility(View.INVISIBLE);
        ImageView unselectFavorite=findViewById(R.id.favoriteUnselected);
        unselectFavorite.setVisibility(View.VISIBLE);

        //todo un-select favorite
    }
}
