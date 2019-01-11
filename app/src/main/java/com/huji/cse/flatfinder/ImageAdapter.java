package com.huji.cse.flatfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
        urlList = list;
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
