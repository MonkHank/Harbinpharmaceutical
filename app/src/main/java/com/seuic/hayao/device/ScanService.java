package com.seuic.hayao.device;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.seuic.hayao.R;
import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.sound.SoundManager;
import com.seuic.hayao.util.AndroidInfoGetter;
import com.seuic.hayao.util.FastClick;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.IScanner;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

public class ScanService extends Service implements DecodeInfoCallBack {
    private final String LOG_TAG = "ScanService";

    private static final int NOTIFICATION_ID = 0x124452;

    private Thread mThread;

    private Scanner mScanner;

    private boolean isOpened;

    private DataManager mDataManager;

    private boolean isContinueScan = false;

    private ScanSettingReceiver mContinueScanReceiver;

    private ScreenOnOffReceiver mScreenOnOffReceiver;

    SoundManager manager = SoundManager.getInstance();

    boolean isScreenOn = true;

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(
                        BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.logo))
                .setAutoCancel(false)
                .setContentTitle("扫描服务启动");

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        this.startForeground(NOTIFICATION_ID, notification);

        mDataManager = DataManager.getInstance();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent intent1 = new Intent("com.android.scanner.service_settings");//禁止扫描工具开机自启动
        intent1.putExtra("boot_start",false);
        getApplicationContext().sendBroadcast(intent);

        if (!isOpened) {
            isOpened = mScanner.open();
        }

        mContinueScanReceiver = new ScanSettingReceiver();
        IntentFilter ContinueScanFilter = new IntentFilter();
        ContinueScanFilter.addAction(ScannerHelper.ACTION_SCANNER_APP_CONTINUNE_SETTINGS);
        registerReceiver(mContinueScanReceiver, ContinueScanFilter);

        mScreenOnOffReceiver = new ScreenOnOffReceiver();
        IntentFilter screenOffIntentFilter = new IntentFilter();
        screenOffIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenOffIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenOnOffReceiver, screenOffIntentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void init() {

        mScanner = ScannerFactory.getScanner(getApplicationContext());
        mScanner.setDecodeInfoCallBack(this);
        isOpened = mScanner.open();

        if (mScanner != null) {
            mScanner.setParams(IScanner.ParamCode.CODE128_LEN_MIN, 6);
            mScanner.setParams(IScanner.ParamCode.CODE128_LEN_MAX, 55);
            mThread = new Thread(runnable);
            mThread.start();
        }

    }


    @Override
    public void onDestroy() {
        this.stopForeground(true);

        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }

        if (mScanner != null) {
            mScanner.setDecodeInfoCallBack(null);
            mScanner.close();
        }
        isOpened = false;
        if (mContinueScanReceiver != null) {
            unregisterReceiver(mContinueScanReceiver);
        }
        if (mScreenOnOffReceiver != null) {
            unregisterReceiver(mScreenOnOffReceiver);
        }

        super.onDestroy();
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            int ret = ScannerKey.open();
            if (ret > -1) {
                while (true) {
                    ret = ScannerKey.getKeyEvent();
                    if (isScreenOn) {
                        switch (ret) {
                            case ScannerKey.KEY_DOWN:
                                if (isContinueScan) {
                                    synchronized (ScanService.class) {
                                        if (!ContinueDecode.isRunning()) {
                                            if (!FastClick.isFastClick()) {
                                                ContinueDecode.start(ScanService.this);
                                            }
                                        } else {
                                            Log.d("hayao", "stop Continue");
                                            ContinueDecode.terminate();
                                        }
                                    }
                                } else {
                                    mScanner.startScan();
                                }
                                break;
                            case ScannerKey.KEY_UP:
                                if (!isContinueScan) {
                                    mScanner.stopScan();
                                }
                                break;
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onDecodeComplete(DecodeInfo info) {
        sendBarcode(info);
    }

    void sendBarcode(DecodeInfo info) {

        if (info.barcode.length()<6) {//剔除长度小于6的条码
            return;
        }

        if (manager != null) {
            manager.playScan();
        }

        if (info != null && mDataManager != null) {
            mDataManager.onBarCodeReceive(info.barcode);
        }
    }

    private String filterInvisibleChars(String barcode) {
        StringBuilder stb = new StringBuilder(barcode);
        int index = 0;
        while (index < stb.length()) {
            char c = stb.charAt(index);
            if (c <= 0x20) {
                stb.deleteCharAt(index);
            } else {
                index++;
            }
        }

        return stb.length() == barcode.length() ? barcode : stb.toString();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ScanService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidInfoGetter.isServiceRunning(context, ScanService.class);
    }

    public class ScanSettingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            isContinueScan = intent.getIntExtra("value", 0) == 0 ? false : true;
            if (isContinueScan) {
                mScanner.setParams(IScanner.ParamCode.CONTINUOUS_SCAN_FLAGS, 1);
                mScanner.setParams(IScanner.ParamCode.EXP_LEVEL, 1);
            } else {
                ContinueDecode.terminate();
                mScanner.stopScan();
                mScanner.setParams(IScanner.ParamCode.CONTINUOUS_SCAN_FLAGS, 0);
                mScanner.setParams(IScanner.ParamCode.EXP_LEVEL, 0);
            }
        }
    }

    public class ScreenOnOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                isScreenOn = true;
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                isScreenOn = false;
            }
        }
    }

}
