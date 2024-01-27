package com.example.projektfryzjer.Database.Helpers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.projektfryzjer.Models.User;

public class SessionManager {
    private SharedPreferences.Editor isLoggedInEditor;
    private SharedPreferences.Editor userLoggedInEditor;
    private SharedPreferences prefLogin;
    private String KEY_IS_LOGGEDIN = "isLoggedIn";
    private String PREFLOGIN_NAME = "AndroidLogin";
    private SharedPreferences prefUser;
    private String KEY_LOGGED_USER = "loggedUser";
    private String PREFUSER_NAME = "AndroidLoggedUser";
    private int PRIVATE_MODE = 0;
    public SessionManager(Context context){
        prefLogin = context.getSharedPreferences(PREFLOGIN_NAME, PRIVATE_MODE);
        isLoggedInEditor = prefLogin.edit();
        prefUser = context.getSharedPreferences(PREFUSER_NAME, PRIVATE_MODE);
        userLoggedInEditor = prefUser.edit();
    }
    public void setLogin(boolean isLoggedIn, User user){
        isLoggedInEditor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        isLoggedInEditor.commit();
        if (user != null)
            userLoggedInEditor.putInt(KEY_LOGGED_USER, user.getUserId());
        else
            userLoggedInEditor.putInt(KEY_LOGGED_USER, 0);
        userLoggedInEditor.commit();
        Log.d(TAG, "User login session modified");
    }
    public boolean isLoggedIn(){
        return prefLogin.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public int loggedInUserId(){
        return prefUser.getInt(KEY_LOGGED_USER, 0);
    }
}