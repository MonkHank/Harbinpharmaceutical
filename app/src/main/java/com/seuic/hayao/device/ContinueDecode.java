package com.seuic.hayao.device;

import android.content.Context;
import android.util.Log;

import com.seuic.scanner.IScanner;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;

public class ContinueDecode {

    static final String LOG_TAG = "ContinueDecode";

    static Thread mThread;
    static Context mContext;
    static ContinueDecode continueDecode;

    private static Scanner mScanner;

    static boolean running = false;

    static boolean terminate = true;

    private static Object mScannerLocker = new Object();

    static void init(Context context) {
        mContext = context;

        if (continueDecode == null) {
            continueDecode = new ContinueDecode();
        }

        if (mScanner == null) {
            mScanner = ScannerFactory.getScanner(mContext);
        }

    }

    public static void start(Context context) {

        init(context);

        terminate = false;

        if (mThread != null) {
            return;
        }

        mThread = new Thread(runnable);
        mThread.start();

        //禁止自动休眠
        WakeLockCtrl.lock(context);

    }

    public static void terminate() {
        terminate = true;
        if (mThread != null) {
            try {
                synchronized (mThread) {
                    mThread.notifyAll();
                }
                mThread.interrupt();
//                mThread.join();
                mThread = null;
            } catch (Exception e) {
                Log.i(LOG_TAG, "terminate : " + e.getMessage());
            } finally {
                running = false;
                if (mScanner != null) mScanner.stopScan();
                //释放自动休眠控制
                WakeLockCtrl.release();
            }
        }

    }

    public static boolean isRunning() {
        return running;
    }

    static Runnable runnable = new Runnable() {

        @Override
        public void run() {
            running = true;

            boolean isContinuousEnable = false;
            if (mScanner.getAllParams().get(IScanner.ParamCode.CONTINUOUS_SCAN_FLAGS, -1) != -1) {
//                isContinuousEnable = ScannerManager.getParams(IScanner.ParamCode.CONTINUOUS_SCAN_FLAGS) == 1;
                int value = mScanner.getParams(IScanner.ParamCode.CONTINUOUS_SCAN_FLAGS);
                isContinuousEnable = value == 1;
            }

            while (!terminate) {
                try {
                    synchronized (mScannerLocker) {
                        mScanner.startScan();
                    }
//                    synchronized (mThread) {
//                        if (isContinuousEnable) {
//                            continue;
//                        }
//							if (scnManager.getScanner() instanceof SE965Scanner){
//								mThread.wait(appconfig.getInterval() + 200);
//							}else{
//                              mThread.wait(1000 + 50);
//							}
//                    }
                } catch (Exception e) {

                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (mScannerLocker) {
                mScanner.stopScan();
            }
            running = false;
        }
    };
}
