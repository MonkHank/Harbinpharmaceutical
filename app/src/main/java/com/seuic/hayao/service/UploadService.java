package com.seuic.hayao.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.local.AppCache;
import com.seuic.hayao.event.data.BillNumberChangeEvent;
import com.seuic.hayao.util.AndroidInfoGetter;
import com.seuic.hayao.util.NetworkUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UploadService extends Service {

    private DataManager mDataManager;
    private Subscription mSubscription;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (!NetworkUtil.isNetworkConnected(this) || AppCache.getInstance().getLoginInfo() == null) {
            stopSelf(startId);
            return START_NOT_STICKY;
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.uploadBills().onErrorReturn(new Func1<Throwable, Boolean>() {
            @Override
            public Boolean call(Throwable throwable) {
                throwable.printStackTrace();
                Log.d("uploading", throwable.getMessage());
                return false;
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) mDataManager.postEvent(new BillNumberChangeEvent());
                    }
                });

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        mDataManager = DataManager.getInstance();
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    public static boolean isRunning(Context context) {
        return AndroidInfoGetter.isServiceRunning(context, UploadService.class);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, UploadService.class);
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                if (!isRunning(context)) {
                    context.startService(getStartIntent(context));
                }
            } else {
                if (isRunning(context)) {
                    context.stopService(getStartIntent(context));
                }
            }
        }
    }
}
