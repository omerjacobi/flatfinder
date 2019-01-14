package com.huji.cse.flatfinder;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.huji.cse.flatfinder.Parser.Parser;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private PostViewModel mViewModel;
    private Geocoder mGeocoder;
    private Handler mTimerHandler = new Handler();
    private Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            TextView continueButton = findViewById(R.id.facebook_contact);
            if (isLoggedIn())
                continueButton.setVisibility(View.VISIBLE);
            else
                continueButton.setVisibility(View.INVISIBLE);
            mTimerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        mGeocoder = new Geocoder(this);
        mTimerHandler.postDelayed(mTimerRunnable, 0);
    }


    /**
     * Interacts with Facebook Graph in order to get facebook post
     * @param token Access token
     */
    private void getPostsInGraph(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Parser.parse(object, mViewModel, mGeocoder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        request.setGraphPath(getString(R.string.path_of_facebook_group_with_filters));
        request.executeAsync();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    /**
     * In case of login error, Updates the user at the current status by Toast message
     * @param message the message we display
     */
    private void showToastWithInputMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showPrivacyMessage(View view) {
        mViewModel.deletePosts();
        ImageView lockImage = findViewById(R.id.privacyButton);
        float y = lockImage.getY();
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.privacy_message,
                (8 * (Toast.LENGTH_LONG)));
        toast.setGravity(Gravity.TOP, 0, (int) (3 * y));

        toast.show();
    }

    /**
     * Indicates whether the user is logged in to his facebook account
     */
    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /**
     * Handles the case when the user clicks on the "log in" button
     */
    public void logInClicked(View view) {
        if (isLoggedIn()) {
            return;
        }
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("groups_access_member_info"));
        LoginManager.getInstance().registerCallback(
                mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getPostsInGraph(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        showToastWithInputMessage(getString(R.string.cancel_login_message));
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showToastWithInputMessage(getString(R.string.error_login_message));
                    }
                });
    }

    /**
     * Handles the case when the user clicks on the "continue" button
     */
    public void continueClick(View view) {
            getPostsInGraph(AccessToken.getCurrentAccessToken());
    }
}