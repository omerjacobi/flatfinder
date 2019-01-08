package com.huji.cse.flatfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import com.squareup.picasso.Picasso;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ViewHolder> {

    private List<String> urlList = new ArrayList<>();
    private LayoutInflater mInflater;


    imageAdapter(Context context,List<String> list ) {
        mInflater = LayoutInflater.from(context);
        urlList.add("https://scontent.xx.fbcdn.net/v/t1.0-9/s720x720/20621949_10212684141434198_3948566753019548363_n.jpg?_nc_cat=100&_nc_ht=scontent.xx&oh=5cc0b46da7ab21ff0e5a4e3a216e8702&oe=5CD764C0");
        urlList.add("https://scontent.xx.fbcdn.net/v/t1.0-9/s720x720/20621121_10212684141714205_3446409551912546005_n.jpg?_nc_cat=108&_nc_ht=scontent.xx&oh=7076e245e2aec2faf37ce01eb035ddb6&oe=5CBBCAC8");
    }

    @Override
    public imageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View imageAdapter = mInflater.inflate(R.layout.activity_flat_photos, parent, false);
        return new imageAdapter.ViewHolder(imageAdapter);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!urlList.isEmpty()){
            Context context = holder.context;
            Picasso.with(context).load(urlList.get(position)).fit().centerCrop().into(holder.addressPic);
        }
    }

    public int getItemCount(){
        // In case the mApartments data member isn't defined, returns 0
        if (urlList != null) {
            return urlList.size();
        } else return 0;
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        ImageView addressPic;
        public TextView php;
        LinearLayout linearLayout;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            addressPic = (ImageView) itemView.findViewById(R.id.apartment_pictures);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.photos_apartment_container);
            context = itemView.getContext();
        }
    }
}
