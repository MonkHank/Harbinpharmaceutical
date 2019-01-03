package com.seuic.hayao.device;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeLockCtrl {
	static WakeLock wakeLock;
	
	public static void lock(Context context){
		if(wakeLock == null){
			PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Scanner WakeLock");
			wakeLock.setReferenceCounted(false);
		}
		
		if(!wakeLock.isHeld()){
			wakeLock.acquire();
		}
	}
	
	public static void release(){
		if(wakeLock != null && wakeLock.isHeld()){
			wakeLock.release();
			wakeLock = null;
		}
	}
}
