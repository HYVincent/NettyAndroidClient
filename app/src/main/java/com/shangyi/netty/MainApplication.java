package com.shangyi.netty;

import android.app.Application;
import android.content.Context;


/**
 * @author 徐飞
 * @version 2015/11/26 10:46
 */
public class MainApplication extends Application {
    private static MainApplication mainApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        PushClient.create();
    }
    public static Context getAppContext() {
        return mainApplication;
    }

}
