package com.huji.cse.flatfinder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
=======
import android.widget.Toast;
>>>>>>> d94c165a17c1dc6ff48c2a99d1f6b14a16e0b230

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
<<<<<<< HEAD
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
=======
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONObject;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private JSONObject facebookPosts;
>>>>>>> d94c165a17c1dc6ff48c2a99d1f6b14a16e0b230

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

<<<<<<< HEAD
=======
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("groups_access_member_info"));
>>>>>>> d94c165a17c1dc6ff48c2a99d1f6b14a16e0b230
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
<<<<<<< HEAD
                        // App code
                        loggedIn(loginResult);
=======
                        getPostsInGraph(loginResult);
>>>>>>> d94c165a17c1dc6ff48c2a99d1f6b14a16e0b230
                    }

                    @Override
                    public void onCancel() {
<<<<<<< HEAD
                        // App code
=======
                        showToastWithInputMassage(getString(R.string.cancel_login_massage));
>>>>>>> d94c165a17c1dc6ff48c2a99d1f6b14a16e0b230
                    }

                    @Override
                    public void onError(FacebookException exception) {
<<<<<<< HEAD
                        // App code
=======
                        showToastWithInputMassage(getString(R.string.error_login_massage));
>>>>>>> d94c165a17c1dc6ff48c2a99d1f6b14a16e0b230
                    }
                });
    }

<<<<<<< HEAD
    private void loggedIn(LoginResult loginResult) {
        AccessToken token=loginResult.getAccessToken();
        String userID=token.getUserId();
//        GraphRequest groups = new GraphRequest(userID)
=======
    private void getPostsInGraph(LoginResult loginResult) {
        AccessToken token=loginResult.getAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        facebookPosts = object;
                    }
                });
        request.setGraphPath(getString(R.string.path_of_facebook_group_with_filters));
        request.executeAsync();
    }

    private void showToastWithInputMassage(String massage){
        Context context = getApplicationContext();
        CharSequence text = massage;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
