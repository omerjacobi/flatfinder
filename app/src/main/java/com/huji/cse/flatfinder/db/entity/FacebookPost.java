package com.huji.cse.flatfinder.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/***
 * an entity for holding a single post from facebook
 */
@Entity(tableName = "post_database")
public class FacebookPost {

    @PrimaryKey(autoGenerate = true)
    private int keyId;

    @ColumnInfo(name = "post_id")
    private String id;

    @ColumnInfo(name = "post_id")
    private Date created_time;

    private String message;

    private String name;

    private String nameID;

    private String picture;

    private float GPSlat;

    private float GPSlong;

    private long price;

    private long numOfRoommates;

    private boolean favorite;

    private String address;

    public FacebookPost(int keyId, String id, Date created_time, String message, String name, String nameID,
                        String picture, float GPSlat, float GPSlong, long price, long numOfRoommates,
                        boolean favorites, String address) {
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
        this.favorite = favorites;
        this.address = address;
    }
/*************************GETTERS********************/
    public String getId() {
        return id;
    }

    public Date getCreated_time() {
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

    public long getNumOfRoommates() {
        return numOfRoommates;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getAddress() {
        return address;
    }

    /*************************SETTERS********************/

    public void makeFavorite() {
        favorite = true;
    }


    public void makeUnFavorite() {
        favorite = false;
    }


}
