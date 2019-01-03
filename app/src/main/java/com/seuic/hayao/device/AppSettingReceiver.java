package com.seuic.hayao.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppSettingReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

	}

//	public static final String ACTION_SCANNER_APP_SETTINGS = "com.android.scanner.service_settings";
//
//	private final String TYPE_PLAYSOUND = "sound_play";
//	private final String TYPE_VIBERATE = "viberate";
//	private final String TYPE_BOOT_START = "boot_start";
//	private final String TYPE_END_CHAR = "endchar";
//	private final String TYPE_BARCODE_SEND_MODE = "barcode_send_mode";
//	private final String TYPE_BARCODE_BROADCAST_ACTION = "action_barcode_broadcast";
//	private final String TYPE_BARCODE_BROADCAST_KEY = "key_barcode_broadcast";
//	private final String TYPE_SCAN_CONTINUE="scan_continue";	//连续扫描
//	private final String TYPE_INTERVAL="interval";	//连续扫面时间间隔
//	private final String TYPE_PREFIX="prefix";		//条码前缀参数
//	private final String TYPE_SUFFIX="suffix";		//条码后缀参数
//	private final String TYPE_ENDCHAR_ON_EMU = "end_char_on_emu"; //结束符以模拟按键方式发送
//	private final String TYPE_ENTER_EVENT = "end_event"; //默认广播时条码后添加回车事件
//	private Appconfig mAppconfig = null;
//
//	private Object mLocker = new Object();
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		if (mAppconfig == null) {
//			synchronized (mLocker) {
//				if (mAppconfig == null) {
//					mAppconfig = Appconfig.getInstance(context);
//				}
//			}
//		}
//
//		String action = intent.getAction();
//		if (ACTION_SCANNER_APP_SETTINGS.equals(action)) {
//			boolean configChanged = false;
//
//			if (intent.hasExtra(TYPE_BARCODE_BROADCAST_ACTION)) {
//				String actionBarcode = intent
//						.getStringExtra(TYPE_BARCODE_BROADCAST_ACTION);
//				if (!actionBarcode.equals(mAppconfig.getBcName())) {
//					mAppconfig.setBcName(actionBarcode);
//					configChanged = true;
//				}
//			}
//
//			if (intent.hasExtra(TYPE_BARCODE_BROADCAST_KEY)) {
//				String keyBarcode = intent
//						.getStringExtra(TYPE_BARCODE_BROADCAST_KEY);
//				if (!keyBarcode.equals(mAppconfig.getBcKey())) {
//					mAppconfig.setBcKey(keyBarcode);
//					configChanged = true;
//				}
//			}
//
//			if (intent.hasExtra(TYPE_BARCODE_SEND_MODE)) {
//				String sendType = intent.getStringExtra(TYPE_BARCODE_SEND_MODE);
//				try {
//					SendMode mode = SendMode.valueOf(sendType);
//					if (mode != mAppconfig.getSendMode()) {
//						mAppconfig.setSendMode(mode);
//						configChanged = true;
//					}
//				} catch (Exception e) {
//					Log.e("AppSettingReceiver", e.getMessage());
//				}
//			}
//
//			if (intent.hasExtra(TYPE_BOOT_START)) {
//				boolean bootStart = intent.getBooleanExtra(
//						TYPE_BOOT_START, mAppconfig.isBootstart());
//				if (bootStart != mAppconfig.isBootstart()) {
//					mAppconfig.setBootstart(bootStart);
//					configChanged = true;
//				}
//			}
//
//			if (intent.hasExtra(TYPE_END_CHAR)) {
//				String endChar = intent.getStringExtra(TYPE_END_CHAR);
//				try {
//					EndChar ec = EndChar.valueOf(endChar);
//					if (ec != mAppconfig.getEndChar()) {
//						mAppconfig.setEndChar(ec);
//						configChanged = true;
//					}
//				} catch (Exception e) {
//					Log.e("AppSettingReceiver", e.getMessage());
//				}
//			}
//
//			if (intent.hasExtra(TYPE_PLAYSOUND)) {
//				boolean playSound = intent.getBooleanExtra(TYPE_PLAYSOUND,
//						mAppconfig.isPlaysound());
//				if (playSound != mAppconfig.isPlaysound()) {
//					mAppconfig.setIsplaysound(playSound);
//					configChanged = true;
//				}
//			}
//
//			if (intent.hasExtra(TYPE_VIBERATE)) {
//				boolean viberate = intent.getBooleanExtra(TYPE_VIBERATE,
//						mAppconfig.isViberate());
//				if (viberate != mAppconfig.isViberate()) {
//					mAppconfig.setIsviberate(viberate);
//					configChanged = true;
//				}
//			}
//
//			if(intent.hasExtra(TYPE_SCAN_CONTINUE)){//连续扫描配置
//				boolean scanContinue = intent.getBooleanExtra(TYPE_SCAN_CONTINUE,
//						mAppconfig.isContinue());
//				if (scanContinue != mAppconfig.isContinue()) {
//					mAppconfig.setIscontinue(scanContinue);
//					configChanged = true;
//				}
//			}
//
//			if(intent.hasExtra(TYPE_INTERVAL)){//时间间隔
//				int timeInterval=intent.getIntExtra(TYPE_INTERVAL,
//						mAppconfig.getInterval());
//				if(timeInterval!=mAppconfig.getInterval()){
//					mAppconfig.setInterval(timeInterval);
//					configChanged=true;
//				}
//			}
//
//			if(intent.hasExtra(TYPE_PREFIX)){//条码前缀
//				String preFix=intent.getStringExtra(TYPE_PREFIX);
//				if(!preFix.equals(mAppconfig.getPrefix())){
//					mAppconfig.setPrefix(preFix);
//					configChanged=true;
//				}
//			}
//
//			if(intent.hasExtra(TYPE_SUFFIX)){//条码后缀
//				String sufFix=intent.getStringExtra(TYPE_SUFFIX);
//				if(!sufFix.equals(mAppconfig.getSuffix())){
//					mAppconfig.setSuffix(sufFix);
//					configChanged=true;
//				}
//			}
//
//			if(intent.hasExtra(TYPE_ENDCHAR_ON_EMU)){//以模拟按键方式发送结束符
//				boolean endChar = intent.getBooleanExtra(TYPE_ENDCHAR_ON_EMU, false);
//				if(endChar != (mAppconfig.isEndCharOnEmu())){
//					mAppconfig.setEndCharOnEmu(endChar);
//					configChanged=true;
//				}
//			}
//
//			if(intent.hasExtra(TYPE_ENTER_EVENT)){//默认广播时条码后添加回车事件
//				boolean isAddEnterEvent = intent.getBooleanExtra(TYPE_ENTER_EVENT, true);
//				if(isAddEnterEvent != (mAppconfig.isAddEnterEvent())){
//					mAppconfig.setAddEnterEvent(isAddEnterEvent);
//					configChanged = true;
//				}
//			}
//
//			if (configChanged) {
//				synchronized (mLocker) {
//					mAppconfig.save();
//				}
//			}
//		}
//	}

}
