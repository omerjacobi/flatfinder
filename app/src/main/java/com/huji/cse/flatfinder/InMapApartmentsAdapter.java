package com.huji.cse.flatfinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huji.cse.flatfinder.db.entity.FacebookPost;

import java.util.List;

public class InMapApartmentsAdapter extends RecyclerView.Adapter<InMapApartmentsAdapter.ViewHolder> {

    class ApartmentViewHolder extends RecyclerView.ViewHolder{
        private final TextView priceTextView ;
        private final TextView roommatesTextView ;
        private final TextView addressTextview ;

        private ApartmentViewHolder(View itemView){
            super(itemView);
            priceTextView = itemView.findViewById(R.id.inmap_apartment_price);
            roommatesTextView = itemView.findViewById(R.id.inmap_apartment_roommates);
            addressTextview = itemView.findViewById(R.id.inmap_apartment_address);
        }

    }

    private List<FacebookPost> mApartments;
    private final LayoutInflater mInflater;

    InMapApartmentsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public InMapApartmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inMapApartmentView = mInflater.inflate(R.layout.in_map_apartment_row, parent, false);
        return new ViewHolder(inMapApartmentView);
    }

    @Override
    public void onBindViewHolder(InMapApartmentsAdapter.ViewHolder viewHolder, int position) {
        if (mApartments != null) {
            FacebookPost apartment = mApartments.get(position);

            viewHolder.priceTextView.setText(String.valueOf(apartment.getPrice()));
            viewHolder.roommatesTextView.setText(String.valueOf(apartment.getNumOfRoommates()));
            viewHolder.addressTextView.setText(apartment.getAddress());
        }
        else {
            //TODO else
        }
    }
    void setmApartments(List<FacebookPost> posts){
        mApartments = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mApartments != null) {
            return mApartments.size();
        }
        else return 0;
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
