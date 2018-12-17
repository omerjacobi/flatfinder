package com.huji.cse.flatfinder.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.huji.cse.flatfinder.db.dao.PostDao;
import com.huji.cse.flatfinder.db.entity.FacebookPost;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FacebookPostRepository {
    private PostDao mPostDoa;
    private LiveData<List<FacebookPost>> mAllPost;
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    FacebookPostRepository(Application application) {
        PostDatabase db = PostDatabase.getDatabase(application);
        mPostDoa = db.postDao();
        mAllPost = mPostDoa.getAllPosts();
    }

    LiveData<List<FacebookPost>> getAllPosts() {
        return mAllPost;
    }


//    void checkIfExists(final String id) {
//        executor.execute(()->{
//            boolean isExists = mPostDoa.isPostExiset(id);
//        });
//    }
}
