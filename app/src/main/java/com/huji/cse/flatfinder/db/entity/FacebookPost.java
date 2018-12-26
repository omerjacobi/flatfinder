package com.huji.cse.flatfinder.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/***
 * an entity for holding a single post from facebook in post_database
 */
@Entity(tableName = "post_database")
public class FacebookPost implements Parcelable {

    public FacebookPost(String id, String created_time, String message, String name,
                        String nameID, String picture, double GPSlat, double GPSlong, long price,
                        long numOfRoommates, boolean favorite, String address) {
        this.id = id;
        this.created_time = created_time;
        this.message = message;
        this.name = name;
        this.nameID = nameID;
        this.picture = picture;
        this.GPSlat = GPSlat;
        this.GPSlong = GPSlong;
        this.price = price;
        this.numOfRoommates = numOfRoommates;
        this.favorite = favorite;
        this.address = address;
    }



    /*the primary key of the database, containg the groups id and the post_id*/
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    private String id;
    /*the time the post created*/
    @ColumnInfo(name = "post_time_created")
    private String created_time;
    /*the content of the post*/
    @ColumnInfo(name = "post_message")
    private String message;
    /*the username (full name) of the account created the post*/
    @ColumnInfo(name = "post_username")
    private String name;
    /*the username (id number) of the account created the post*/
    @ColumnInfo(name = "post_username_id")
    private String nameID;
    /*a link to a picture in the post*/
    @ColumnInfo(name = "post_pictures")
    private String picture;
    /*the gps lat coordination of the post*/
    @ColumnInfo(name = "gps_lat")
    private double GPSlat;
    /*the gps long coordination of the post*/
    @ColumnInfo(name = "gps_long")
    private double GPSlong;
    /*the price of the appartment*/
    @ColumnInfo(name = "flat_price")
    private long price;
    /*number of roomates in the apartments*/
    @ColumnInfo(name = "number_of_roommates")
    private long numOfRoommates;
    /*is the flat marked as favorite by the user*/
    @ColumnInfo(name = "favorite_post")
    private boolean favorite;
    /*the full flat address*/
    @ColumnInfo(name = "flat_address")
    private String address;

    /*** object getters ***/
    public String getId() {
        return id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getNameID() {
        return nameID;
    }

    public String getPicture() {
        return picture;
    }

    public double getGPSlat() {
        return GPSlat;
    }

    public double getGPSlong() {
        return GPSlong;
    }

    public long getPrice() {
        return price;
    }


    public long getNumOfRoommates() {
        return numOfRoommates;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getAddress() {
        return address;
    }
/*** object setters ***/
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {

        return 0;
    }
/*** function that make the object  parcelable ***/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.created_time);
        dest.writeString(this.message);
        dest.writeString(this.name);
        dest.writeString(this.nameID);
        dest.writeString(this.picture);
        dest.writeDouble(this.GPSlat);
        dest.writeDouble(this.GPSlong);
        dest.writeLong(this.price);
        dest.writeLong(this.numOfRoommates);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.address);
    }

    protected FacebookPost(Parcel in) {
        this.id = in.readString();
        this.created_time = in.readString();
        this.message = in.readString();
        this.name = in.readString();
        this.nameID = in.readString();
        this.picture = in.readString();
        this.GPSlat = in.readFloat();
        this.GPSlong = in.readFloat();
        this.price = in.readLong();
        this.numOfRoommates = in.readLong();
        this.favorite = in.readByte() != 0;
        this.address = in.readString();
    }

    public static final Creator<FacebookPost> CREATOR = new Creator<FacebookPost>() {
        @Override
        public FacebookPost createFromParcel(Parcel source) {
            return new FacebookPost(source);
        }

        @Override
        public FacebookPost[] newArray(int size) {
            return new FacebookPost[size];
        }
    };
}
