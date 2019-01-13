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

    /**
     * delete all the object in the database
     */
    public void deletePosts()
    {
        mPostDao.deleteAll();
    }

    /**
     * get all the object in the database
     * @return a livedata that it a list of all the facebook post
     */
    public LiveData<List<FacebookPost>> getAllPosts()
    {
        return mPostDao.getAllPosts();
    }

    /**
     * recevied a filter list of the posts in the database
     * @param minLat minimum latitude
     * @param maxLat maximum latitude
     * @param minLong minimum longitude
     * @param maxLong maximum longitude
     * @param maxPrice maximum price
     * @param maxRoommates maximum roomates
     * @param filterDistance a boolean to activate filter of distance
     * @param onlyFavorites to show only favorites
     * @return a livedata that it a list of  the facebook post after we filter them
     */
    public LiveData<List<FacebookPost>> getPostsFiltered(double minLat, double maxLat, double minLong,
                                                         double maxLong, long maxPrice, long maxRoommates,
                                                         boolean filterDistance, boolean onlyFavorites)
    {
        return mPostDao.getPostAfterFilter( minLat,  maxLat,  minLong,  maxLong,
                maxPrice,  maxRoommates,  filterDistance,  onlyFavorites);
    }

    public int isPostExists(String postId)
    {
         return mPostDao.isPostExiset(postId);
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
