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

@Dao
public interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FacebookPost... posts);

    @Delete
    void deletePost(FacebookPost... posts);

    @Update
    void updatePost(FacebookPost post);

    @Query("SELECT * FROM post_database")
    LiveData<List<FacebookPost>> getAllPosts();


    @Query("SELECT * FROM post_database WHERE favorite")
    LiveData<List<FacebookPost>> getAllFavoritesPosts();

    @Query("SELECT * FROM post_database WHERE price < :maxPrice")
    LiveData<List<FacebookPost>>  getAllCheaperPosts(long maxPrice);

    @Query("SELECT * FROM post_database WHERE GPSlat BETWEEN :minLat AND :maxLat AND  GPSlong BETWEEN :minLong AND :maxLong")
    LiveData<List<FacebookPost>>  getAllPostInLocation(float minLat, float maxLat, float minLong, float maxLong);


    @Query("SELECT * FROM post_database WHERE numOfRoommates < :maxRoommates")
    LiveData<List<FacebookPost>>  getAllPostWithLessRoommates(long maxRoommates);

}
