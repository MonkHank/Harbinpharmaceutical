package com.seuic.hayao.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.sound.SoundManager;


public class BarCodeReceiver extends BroadcastReceiver {

    SoundManager manager = SoundManager.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ScannerHelper.ACTION_SCANNER_DECODE.equals(intent.getAction())) {
            String barcode = intent.getStringExtra(ScannerHelper.KEY_BARCODE);
            Log.d("hu", barcode);
            manager.playScan();
            DataManager.getInstance().onBarCodeReceive(barcode);//post scan data to data manager
        }
    }
}
