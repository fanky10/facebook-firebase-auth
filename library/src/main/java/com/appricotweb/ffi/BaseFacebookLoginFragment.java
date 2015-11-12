package com.appricotweb.ffi;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by facundo.scoccia on 11/12/2015.
 */
public class BaseFacebookLoginFragment extends Fragment {
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, getFacebookLoginCallback());
    }

    private FacebookCallback getFacebookLoginCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // perform profile login
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                // we use the new updated info
                                // if trying to use Profile.getCurrentProfile
                                // then add profile tracking
                                String email = me.optString("email");
                                String firstName = me.optString("first_name");
                                String lastName = me.optString("last_name");

                                if (response.getError() != null) {
                                    // fire some error
                                } else {
                                    // fire everything ok!
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // fire cancel
            }

            @Override
            public void onError(FacebookException exception) {
                // fire error
            }
        };
    }

    private final void doFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
                getActivity(),
                Arrays.asList("email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
