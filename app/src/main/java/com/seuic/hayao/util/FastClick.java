package com.seuic.hayao.util;

import android.os.SystemClock;

public class FastClick {
    public static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
