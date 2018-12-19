package com.huji.cse.flatfinder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONObject;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
//    private JSONObject facebookPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("groups_access_member_info"));
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        getPostsInGraph(loginResult);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        showToastWithInputMessage(getString(R.string.cancel_login_message));
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        showToastWithInputMessage(getString(R.string.error_login_message));
//                    }
//                });
    }

    private void getPostsInGraph(LoginResult loginResult) {
        AccessToken token=loginResult.getAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject facebookPosts = object;
                    }
                });
        request.setGraphPath(getString(R.string.path_of_facebook_group_with_filters));
        request.executeAsync();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void showToastWithInputMessage(String message){
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showPrivacyMessage(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "This is a message displayed in a Toast",
                Toast.LENGTH_SHORT);

        toast.show();
    }

    public void logInClicked(View view) {
//        setContentView(R.layout.activity_main);

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
}
