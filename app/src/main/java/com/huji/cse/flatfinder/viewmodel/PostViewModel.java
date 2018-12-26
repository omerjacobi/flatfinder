package com.huji.cse.flatfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.huji.cse.flatfinder.db.PostDatabase;
import com.huji.cse.flatfinder.db.dao.PostDao;
import com.huji.cse.flatfinder.db.entity.FacebookPost;

import java.util.List;


/**
 * a viewmodel that provide data to the UI in a lifecycle-conscious way
 */
public class PostViewModel extends AndroidViewModel {

    private PostDao mPostDao;

    public PostViewModel(Application application){
        super(application);
        PostDatabase db = PostDatabase.getDatabase(application);
        mPostDao = db.postDao();
    }

    LiveData<List<FacebookPost>> getAllCheaperPosts(long maxPrice)
    {
        return mPostDao.getAllCheaperPosts(maxPrice);
    }

    LiveData<List<FacebookPost>> getAllFavoritesPosts()
    {
        return mPostDao.getAllFavoritesPosts();
    }

    LiveData<List<FacebookPost>> getAllPostInLocation(float minLat, float maxLat, float minLong, float maxLong)

    {
        return mPostDao.getAllPostInLocation(minLat, maxLat,minLong,maxLong);
    }

    public LiveData<List<FacebookPost>> getAllPosts()
    {
        return mPostDao.getAllPosts();
    }

    public LiveData<List<FacebookPost>> getPostsFiltered(double minLat, double maxLat, double minLong,
                                                         double maxLong, long maxPrice, long maxRoommates)
    {
        return mPostDao.getPostAfterFilter( minLat,  maxLat,  minLong,  maxLong,  maxPrice,  maxRoommates);
    }

    public int isPostExists(String postId)
    {
         return mPostDao.isPostExiset(postId);
    }

    LiveData<List<FacebookPost>> getAllPostWithLessRoommates(long maxRoommates)
    {
        return mPostDao.getAllPostWithLessRoommates(maxRoommates);
    }

    public void insert (FacebookPost post){
        new insertAsyncTask(mPostDao).execute(post);
    }

    private static class insertAsyncTask extends AsyncTask<FacebookPost, Void, Void>{
        private PostDao mAsyncTaskDao;

        insertAsyncTask(PostDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FacebookPost... params) {
            mAsyncTaskDao.insert(params);
            return null;
        }

    }
    public void delete (FacebookPost post){
        new deleteAsyncTask(mPostDao).execute(post);
    }

    private static class deleteAsyncTask extends AsyncTask<FacebookPost, Void, Void>{
        private PostDao mAsyncTaskDao;

        deleteAsyncTask(PostDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FacebookPost... params) {
            mAsyncTaskDao.deletePost(params);
            return null;
        }

    }

    public void update (FacebookPost post){
        new deleteAsyncTask(mPostDao).execute(post);
    }

    private static class updateAsyncTask extends AsyncTask<FacebookPost, Void, Void>{
        private PostDao mAsyncTaskDao;

        updateAsyncTask(PostDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FacebookPost... params) {
            mAsyncTaskDao.updatePost(params[0]);
            return null;
        }

    }



}
