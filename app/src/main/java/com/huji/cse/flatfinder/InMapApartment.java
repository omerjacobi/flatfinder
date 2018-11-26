package com.huji.cse.flatfinder;

import java.util.ArrayList;

public class InMapApartment {
    private int mPrice;
    private int mRoommatesAmount;
    private String mAddress;
    // TODO: add photo


    public InMapApartment(int mPrice, int mRoommatesAmount, String mAddress) {
        this.mPrice = mPrice;
        this.mRoommatesAmount = mRoommatesAmount;
        this.mAddress = mAddress;
    }

    public String getmPrice() {
        return Integer.toString(mPrice);
    }

    public String getmRoommatesAmount() {
        return Integer.toString(mRoommatesAmount);
    }

    public String getmAddress() {
        return mAddress;
    }

    public static ArrayList<InMapApartment> createApartmentsList(int numOfApartments) {
        ArrayList<InMapApartment> apartments = new ArrayList<>();

        for (int i = 0; i < numOfApartments; i++) {
            apartments.add(new InMapApartment(i * 500, i + 1, "Jerusalem Aza " + (i + 1)));
        }

        return apartments;
    }
}
