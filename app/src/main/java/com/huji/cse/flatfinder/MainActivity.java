package com.huji.cse.flatfinder;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.huji.cse.flatfinder.db.entity.FacebookPost;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private JSONObject facebookPosts;
    private PostViewModel mViewModel;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Button continueButton=findViewById(R.id.continueButton);
            if(isLoggedIn())
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
        timerHandler.postDelayed(timerRunnable, 0);
    }


    private void getPostsInGraph(LoginResult loginResult) {
        AccessToken token = loginResult.getAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject facebookPosts = object;
                        try {
                            parser.parse(object, mViewModel);
                        } catch (JSONException e) {

                        }
                    }
                });
        request.setGraphPath(getString(R.string.path_of_facebook_group_with_filters));
        request.executeAsync();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void showToastWithInputMessage(String message){
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
        ImageView lockImage=findViewById(R.id.privacyButton);
        float y=lockImage.getY();
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.privacy_message,
                (4*(Toast.LENGTH_LONG)));
        toast.setGravity(Gravity.TOP,0,(int)(3*y));

        toast.show();
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void logInClicked(View view) {
        if(isLoggedIn()){
            return;
        }
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("groups_access_member_info"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getPostsInGraph(loginResult);
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
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}