package com.seuic.hayao.data.local;

import com.seuic.hayao.data.bean.SmartProfileInfo;

public class AppCache {

    private static AppCache mCache;

    private SmartProfileInfo loginInfo;

    public SmartProfileInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(SmartProfileInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    private AppCache() {
    }

    public static AppCache getInstance() {
        if (mCache == null) {
            mCache = new AppCache();
        }
        return mCache;
    }
}
