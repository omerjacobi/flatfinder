package com.huji.cse.flatfinder.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.huji.cse.flatfinder.db.dao.PostDao;
import com.huji.cse.flatfinder.db.entity.FacebookPost;

/**
 * a singleton object of post_database using room database
 */

@Database(entities = {FacebookPost.class}, version = 1, exportSchema = false)
public abstract class PostDatabase extends RoomDatabase {

    public abstract PostDao postDao();

    private static volatile PostDatabase INSTANCE;

    public static PostDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (PostDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PostDatabase.class, "posts_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
