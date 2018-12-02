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

    /**
     * Generates a list of InMapApartments
     * @param numOfApartments number of objects to create
     * @return an ArrayList that contains numOfApartments InMapApartments
     */
    public static ArrayList<InMapApartment> createApartmentsList(int numOfApartments) {
        ArrayList<InMapApartment> apartments = new ArrayList<>();

        for (int i = 0; i < numOfApartments; i++) {
            switch (i % 3) {
                case 0:
                    apartments.add(new InMapApartment(i * 500, i + 1, "Jerusalem Alfasi " + (i + 1)));
                    break;
                case 1:
                    apartments.add(new InMapApartment(i * 1000, i + 1, "Jerusalem Ben Yehuda " + (i * 10 + 1)));
                    break;
                case 2:
                default:
                    apartments.add(new InMapApartment(i * 800, i, "Jerusalem Jaffa " + (i * 5 + 2)));
            }
        }

        return apartments;
    }
}
