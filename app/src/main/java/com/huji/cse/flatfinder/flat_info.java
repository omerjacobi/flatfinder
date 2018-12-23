package com.huji.cse.flatfinder;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class flat_info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_info);

        TextView apartment_price = findViewById(R.id.apartment_price);
        TextView apartment_address = findViewById(R.id.apartment_address);
        TextView apartment_post = findViewById(R.id.apartment_post);
        TextView ContactFacebook = findViewById(R.id.ContactFacebook);
        ScrollView apartment_picture = findViewById(R.id.apartment_picture);
        FloatingActionButton  backToMainMenu = findViewById(R.id.backToMainMenu);

        //make pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x =0;
        params.y =-20;
        getWindow().setAttributes(params);

    }
}
