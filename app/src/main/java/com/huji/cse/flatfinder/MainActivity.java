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
    CallbackManager callbackManager;
    private PostViewModel mViewModel;
    private Geocoder geocoder;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            TextView continueButton = findViewById(R.id.facebook_contact);
            if (isLoggedIn())
                continueButton.setVisibility(View.VISIBLE);
            else
                continueButton.setVisibility(View.INVISIBLE);
            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        geocoder = new Geocoder(this);
        timerHandler.postDelayed(timerRunnable, 0);
    }


    private void getPostsInGraph(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject facebookPosts = object;
                        try {
                            Parser.parse(object, mViewModel,geocoder);
                        } catch (JSONException e) {

                        }
                    }
                });
        request.setGraphPath(getString(R.string.path_of_facebook_group_with_filters));
        request.executeAsync();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void showToastWithInputMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showPrivacyMessage(View view) {
        ImageView lockImage = findViewById(R.id.privacyButton);
        float y = lockImage.getY();
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.privacy_message,
                (4 * (Toast.LENGTH_LONG)));
        toast.setGravity(Gravity.TOP, 0, (int) (3 * y));

        toast.show();
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void logInClicked(View view) {
        if (isLoggedIn()) {
            return;
        }
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("groups_access_member_info"));
        LoginManager.getInstance().registerCallback(callbackManager,
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

    public void continueClick(View view) {
            getPostsInGraph(AccessToken.getCurrentAccessToken());
    }
}