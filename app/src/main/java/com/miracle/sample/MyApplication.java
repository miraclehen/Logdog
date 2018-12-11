package com.miracle.sample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.miracle.logdog.Config;
import com.miracle.logdog.Logdog;

/**
 * miracle
 * 2018/12/5 10:09
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        Logdog.enableDebug();
        Logdog.init(this, "http://192.168.6.176:8091");


    }
}
