package com.huji.cse.flatfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huji.cse.flatfinder.db.entity.FacebookPost;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;

import java.util.List;

public class InMapApartmentsAdapter extends RecyclerView.Adapter<InMapApartmentsAdapter.ViewHolder> {

    // A list of the current apartments held
    private List<FacebookPost> mApartments;
    private PostViewModel mPostViewModel;


    private final LayoutInflater mInflater;

    InMapApartmentsAdapter(Context context, PostViewModel postViewModel) {
        mInflater = LayoutInflater.from(context);
        mPostViewModel = postViewModel;
    }

    @Override
    public InMapApartmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inMapApartmentView = mInflater.inflate(R.layout.in_map_apartment_row, parent, false);
        return new ViewHolder(inMapApartmentView);
    }

    @Override
    public void onBindViewHolder(final InMapApartmentsAdapter.ViewHolder viewHolder, final int position) {
        if (mApartments != null) {
            final FacebookPost apartment = mApartments.get(position);
            final Context context = viewHolder.context;

            viewHolder.priceTextView.setText(String.valueOf(apartment.getPrice()));
            viewHolder.roommatesTextView.setText(String.valueOf(apartment.getNumOfRoommates()));
            viewHolder.addressTextView.setText(apartment.getAddress());
            viewFavoriteStatus(viewHolder.favoritesButton, apartment.isFavorite());

            // In case the user clicks on the container, he is transferred to the Apartment's info
            // Activity, which contains further details about the currently viewed apartment
            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FlatInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.FACEBOOK_KEY, apartment);
                    intent.putExtra(Constants.BUNDLE_KEY, bundle);
                    context.startActivity(intent);
                }
            });

            viewHolder.favoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean newFavStatus = !apartment.isFavorite();
                    apartment.setFavorite(newFavStatus);
                    viewFavoriteStatus(viewHolder.favoritesButton, newFavStatus);
                    mPostViewModel.insert(apartment);
                }
            });
        }
    }

    /**
     * Sets the currently held apartments list by given value
     */
    void setmApartments(List<FacebookPost> apartments){
        mApartments = apartments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // In case the mApartments data member isn't defined, returns 0
        if (mApartments != null) {
            return mApartments.size();
        }
        else return 0;
    }

    /**
     * a helper function to change the favorite icon on screen
     */
    private void viewFavoriteStatus(ImageView favoritesButton, boolean isFavorite) {
        if (isFavorite) {
            favoritesButton.setBackgroundResource(R.drawable.ic_favorite);
            android.view.ViewGroup.LayoutParams params = favoritesButton.getLayoutParams();
            params.height = 66;
            params.width = 40;
            favoritesButton.setLayoutParams(params);
        } else {
            favoritesButton.setBackgroundResource(R.drawable.ic_notfavorite);
            android.view.ViewGroup.LayoutParams params = favoritesButton.getLayoutParams();
            params.height = 66;
            params.width = 40;
            favoritesButton.setLayoutParams(params);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addressTextView,
                        priceTextView,
                        roommatesTextView;
        public CardView linearLayout;
        public Context context;
        public ImageView image,
                         favoritesButton;

        public ViewHolder(View itemView) {
            super(itemView);
            addressTextView = (TextView) itemView.findViewById(R.id.inmap_apartment_address);
            priceTextView = (TextView) itemView.findViewById(R.id.inmap_apartment_price);
            roommatesTextView = (TextView) itemView.findViewById(R.id.inmap_apartment_roommates);
            image=(ImageView)itemView.findViewById(R.id.inmap_apartment_image);
            linearLayout = (CardView) itemView.findViewById(R.id.inmap_apartment_card);
            favoritesButton = (ImageView) itemView.findViewById(R.id.favorite_button);
            context = itemView.getContext();
        }
    }
}
