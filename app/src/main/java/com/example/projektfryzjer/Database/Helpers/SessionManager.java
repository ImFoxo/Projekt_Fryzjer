package com.example.projektfryzjer.Database.Helpers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.projektfryzjer.Models.User;

public class SessionManager {
    private SharedPreferences.Editor isLoggedInEditor;
    private SharedPreferences.Editor userIdLoggedInEditor;
    private SharedPreferences.Editor userNameLoggedInEditor;
    private SharedPreferences prefLogin;
    private String KEY_IS_LOGGEDIN = "isLoggedIn";
    private String PREFLOGIN_NAME = "AndroidLogin";
    private SharedPreferences prefUserId;
    private String KEY_LOGGED_USERID = "loggedUserId";
    private String PREFUSERID_NAME = "AndroidLoggedUserId";
    private SharedPreferences prefUsername;
    private String KEY_LOGGED_USERNAME = "loggedUsername";
    private String PREFUSERNAME_NAME = "AndroidLoggedUsername";
    private int PRIVATE_MODE = 0;
    public SessionManager(Context context){
        prefLogin = context.getSharedPreferences(PREFLOGIN_NAME, PRIVATE_MODE);
        isLoggedInEditor = prefLogin.edit();
        prefUserId = context.getSharedPreferences(PREFUSERID_NAME, PRIVATE_MODE);
        userIdLoggedInEditor = prefUserId.edit();
        prefUsername = context.getSharedPreferences(PREFUSERNAME_NAME, PRIVATE_MODE);
        userNameLoggedInEditor = prefUsername.edit();
    }
    public void setLogin(boolean isLoggedIn, User user){
        isLoggedInEditor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        isLoggedInEditor.commit();
        if (user != null) {
            userIdLoggedInEditor.putInt(KEY_LOGGED_USERID, user.getUserId());
            userNameLoggedInEditor.putString(KEY_LOGGED_USERNAME, user.getUsername());
        }
        else {
            userIdLoggedInEditor.putInt(KEY_LOGGED_USERID, 0);
            userNameLoggedInEditor.putString(KEY_LOGGED_USERNAME, null);
        }
        userIdLoggedInEditor.commit();
        userNameLoggedInEditor.commit();
        Log.d(TAG, "User login session modified");
    }
    public boolean isLoggedIn(){
        return prefLogin.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public int loggedInUserId(){
        return prefUserId.getInt(KEY_LOGGED_USERID, 0);
    }
    public String loggedInUsername() {
        return prefUsername.getString(KEY_LOGGED_USERNAME, null);
    }
}