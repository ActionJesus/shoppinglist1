package com.example.lars.blankactivity;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

/**
 * Created by Lars on 13-12-2016.
 */

public class MyApplication extends Application {

    public void onCreate(){
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()){
            Log.d("firebase","persistance enabled");
        }else{
            Log.d("firebase","persistance not enabled");
        }
    }
}
