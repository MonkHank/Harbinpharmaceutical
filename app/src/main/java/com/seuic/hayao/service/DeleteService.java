package com.seuic.hayao.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.event.data.BillNumberChangeEvent;
import com.seuic.hayao.util.AndroidInfoGetter;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DeleteService extends Service {

    private DataManager mDataManager;
    private Subscription mSubscription;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.deleteOverTimeBill().onErrorReturn(new Func1<Throwable, Bill>() {
            @Override
            public Bill call(Throwable throwable) {
                return null;
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Bill>() {
                    @Override
                    public void onCompleted() {
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Bill bill) {
                        if (bill != null) {
                            mDataManager.postEvent(new BillNumberChangeEvent());
                        }
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
        return AndroidInfoGetter.isServiceRunning(context, DeleteService.class);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DeleteService.class);
    }

    /**
     * 日子发生变化时，系统发出一个广播，
     * 这个是在清单文件中注册的
     */
    public static class DeleteOnDateChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.DATE_CHANGED")) {
                if (!isRunning(context)) {
                    context.startService(getStartIntent(context));
                }
            }
        }
    }
}
