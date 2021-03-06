package com.huji.cse.flatfinder.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.huji.cse.flatfinder.db.dao.PostDao;
import com.huji.cse.flatfinder.db.entity.FacebookPost;

/**
 * a singleton object of post_database using room database
 */

@Database(entities = {FacebookPost.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PostDatabase extends RoomDatabase {

    public abstract PostDao postDao();

    private static volatile PostDatabase INSTANCE;

    /*build a database if its the first time the app created*/
    public static PostDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (PostDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PostDatabase.class, "posts_database").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
