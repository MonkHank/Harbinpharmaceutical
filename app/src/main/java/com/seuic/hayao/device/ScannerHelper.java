package com.seuic.hayao.device;

public class ScannerHelper {

    /**
     * Scanner Tool Default Action
     */
    //public final static String ACTION_SCAN_DECODE = "com.android.server.scannerservice.broadcast";
    public final static String ACTION_SCANNER_DECODE = "com.android.server.scannerservice.hayao.broadcast";
    public final static String KEY_BARCODE = "scannerdata";
    public final static String ACTION_SCANNER_ENABLED = "com.android.scanner.ENABLED";
    public final static String KEY_ENABLED = "enabled";
    public static final String ACTION_SCANNER_APP_SETTINGS = "com.android.scanner.service_settings";
    public static final String ACTION_SCANNER_APP_CONTINUNE_SETTINGS = "com.seuic.scanner.action.PARAM_SETTINGS";
    public static final String TYPE_SCAN_CONTINUE = "scan_continue";    //连续扫描
}
 