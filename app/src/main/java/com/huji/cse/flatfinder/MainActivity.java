package com.huji.cse.flatfinder;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
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


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private JSONObject facebookPosts;
    private PostViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
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
                        showToastWithInputMassage(getString(R.string.cancel_login_massage));
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showToastWithInputMassage(getString(R.string.error_login_massage));
                    }
                });
    }

    private void getPostsInGraph(LoginResult loginResult) {
        AccessToken token = loginResult.getAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        facebookPosts = object;
                        System.out.println(facebookPosts);
                        Parser parser= new Parser();
                        try {
                            parser.parse(object, mViewModel);
                        } catch (JSONException e) {

                        }
                    }
                });
        request.setGraphPath(getString(R.string.path_of_facebook_group_with_filters));
        request.executeAsync();
        mViewModel.getAllPosts().observe(this, new Observer<List<FacebookPost>>() {
            @Override
            public void onChanged(@Nullable List<FacebookPost> facebookPosts) {

            }
        });
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void showToastWithInputMassage(String massage) {
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