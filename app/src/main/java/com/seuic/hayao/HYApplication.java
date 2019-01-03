package com.seuic.hayao;

import android.app.Application;

public class HYApplication extends Application {

    private static HYApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static HYApplication getApplication() {
        return mApplication;
    }

}
