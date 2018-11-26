package com.huji.cse.flatfinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InMapApartmentsAdapter extends RecyclerView.Adapter<InMapApartmentsAdapter.ViewHolder> {

    private List<InMapApartment> mApartments;
    private Context mContext;

    public InMapApartmentsAdapter(List<InMapApartment> apartments, Context context) {
        mApartments = apartments;
        mContext = context;
    }

    @Override
    public InMapApartmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View inMapApartmentView = inflater.inflate(R.layout.in_map_apartment_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(inMapApartmentView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(InMapApartmentsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        InMapApartment apartment = mApartments.get(position);

        // Set item views based on your views and data model
        TextView priceTextView = viewHolder.priceTextView,
                 roommatesTextView = viewHolder.roommatesTextView,
                 addressTextview = viewHolder.addressTextView;
        priceTextView.setText(mContext.getString(R.string.inmap_price_view, apartment.getmPrice()));
        roommatesTextView.setText(mContext.getString(R.string.inmap_roommates_view, apartment.getmRoommatesAmount()));
        addressTextview.setText(mContext.getString(R.string.inmap_address_view, apartment.getmAddress()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mApartments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addressTextView,
                        priceTextView,
                        roommatesTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            addressTextView = (TextView) itemView.findViewById(R.id.inmap_apartment_address);
            priceTextView = (TextView) itemView.findViewById(R.id.inmap_apartment_price);
            roommatesTextView = (TextView) itemView.findViewById(R.id.inmap_apartment_roommates);
        }
    }

}
