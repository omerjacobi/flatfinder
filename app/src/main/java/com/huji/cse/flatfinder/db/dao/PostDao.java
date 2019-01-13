package com.huji.cse.flatfinder.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.huji.cse.flatfinder.db.entity.FacebookPost;

import java.util.List;


/**
 * data access object that insert, update and queries the post_database
 */
@Dao
public interface PostDao {
    /*inset a an array of post to the database replace data if its already existing*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FacebookPost... posts);
    /*delete an array of post from the database*/
    @Delete
    void deletePost(FacebookPost... posts);
    /*update an array of post from the database, used post_id as a uinque id*/
    @Update
    void updatePost(FacebookPost post);
    /*get all the post from the database*/
    @Query("SELECT * FROM post_database")
    LiveData<List<FacebookPost>> getAllPosts();
    /*return number of items with the same id (probably 0 or 1 as post_id is key)*/
    @Query("SELECT COUNT(*) FROM post_database WHERE post_id = :post_id")
    int isPostExiset(String post_id);
    /*return post marked as favorites*/
    @Query("SELECT * FROM post_database WHERE favorite_post")
    LiveData<List<FacebookPost>> getAllFavoritesPosts();

    /*return post that is in an box of an given location by the gps coordinate*/
    @Query("SELECT * FROM post_database WHERE gps_lat BETWEEN :minLat AND :maxLat AND  gps_long BETWEEN :minLong AND :maxLong")
    LiveData<List<FacebookPost>>  getAllPostInLocation(float minLat, float maxLat, float minLong, float maxLong);
    /*return post with number of roomates lower than maxRoommates*/
    @Query("SELECT * FROM post_database WHERE number_of_roommates < :maxRoommates")
    LiveData<List<FacebookPost>>  getAllPostWithLessRoommates(long maxRoommates);
    @Query("SELECT * FROM post_database WHERE (((NOT :filterDistance) OR ((gps_lat BETWEEN :minLat AND :maxLat) AND  (gps_long" +
            " BETWEEN :minLong AND :maxLong))) AND (number_of_roommates <= :maxRoommates AND flat_price <= :maxPrice) AND ((NOT :onlyFavorites) OR favorite_post))")
    LiveData<List<FacebookPost>>  getPostAfterFilter(double minLat, double maxLat, double minLong,
                                                     double maxLong, long maxPrice, long maxRoommates,
                                                     boolean filterDistance, boolean onlyFavorites);

    @Query("DELETE FROM post_database")
    void deleteAll();

}
