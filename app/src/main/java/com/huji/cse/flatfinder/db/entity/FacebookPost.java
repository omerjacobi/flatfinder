package com.huji.cse.flatfinder.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/***
 * an entity for holding a single post from facebook
 */
@Entity(tableName = "post_database")
public class FacebookPost implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int keyId;

    @ColumnInfo(name = "post_id")
    private String id;

    @ColumnInfo(name = "post_time_created")
    private String created_time;

    public FacebookPost(int keyId, String id, String created_time, String message, String name,
                        String nameID, String picture, float GPSlat, float GPSlong, long price,
                        long numOfRoommates, boolean favorite, String address) {
        this.keyId = keyId;
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

    @ColumnInfo(name = "post_message")
    private String message;

    @ColumnInfo(name = "post_username")
    private String name;

    @ColumnInfo(name = "post_username_id")
    private String nameID;

    @ColumnInfo(name = "post_pictures")
    private String picture;

    @ColumnInfo(name = "gps_lat")
    private float GPSlat;

    @ColumnInfo(name = "gps_long")
    private float GPSlong;

    @ColumnInfo(name = "flat_price")
    private long price;

    @ColumnInfo(name = "number_of_roommates")
    private long numOfRoommates;

    @ColumnInfo(name = "favorite_post")
    private boolean favorite;

    @ColumnInfo(name = "flat_address")
    private String address;

    public int getKeyId() {
        return keyId;
    }

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

    public float getGPSlat() {
        return GPSlat;
    }

    public float getGPSlong() {
        return GPSlong;
    }

    public long getPrice() {
        return price;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.keyId);
        dest.writeString(this.id);
        dest.writeString(this.created_time);
        dest.writeString(this.message);
        dest.writeString(this.name);
        dest.writeString(this.nameID);
        dest.writeString(this.picture);
        dest.writeFloat(this.GPSlat);
        dest.writeFloat(this.GPSlong);
        dest.writeLong(this.price);
        dest.writeLong(this.numOfRoommates);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.address);
    }

    protected FacebookPost(Parcel in) {
        this.keyId = in.readInt();
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
