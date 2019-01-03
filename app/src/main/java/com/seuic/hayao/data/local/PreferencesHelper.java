package com.seuic.hayao.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "ha_yao_pre_file";
    public static final String DEFAULT_SERVER_ADDRESS = "http://222.171.148.165/TTS";
    public static final String DEFAULT_UPDATE_SERVIER_ADDRESS = "http://www.hymsy.com.cn:10001";
    public static final int DEFAULT_DELETE_TIME = 30;

    public static class KEY {
        public static final String LAST_ACCOUNT = "latest_account";
        public static final String LAST_PASSWORD = "last_password";
        public static final String SERVER_ADDRESS = "server_address";
        public static final String UPDATE_SERVER_ADDRESS = "update_server_address";
        public static final String DELETE_TIME = "delete_time";
        public static final String CHECKED_VERSION = "checked_version";

    }

    private final SharedPreferences mPref;

    private String latestAccount;
    private String latestPassword;
    private String serverAddress;
    private String updateServerAddress;
    private int deleteTime;
    private int checkedVersion;

    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        init();
    }

    private void init() {
        latestAccount = mPref.getString(KEY.LAST_ACCOUNT, "");
        serverAddress = mPref.getString(KEY.SERVER_ADDRESS, DEFAULT_SERVER_ADDRESS);
        latestPassword = mPref.getString(KEY.LAST_PASSWORD, "");
        updateServerAddress = mPref.getString(KEY.UPDATE_SERVER_ADDRESS, DEFAULT_UPDATE_SERVIER_ADDRESS);
        deleteTime = mPref.getInt(KEY.DELETE_TIME, DEFAULT_DELETE_TIME);
        checkedVersion = mPref.getInt(KEY.CHECKED_VERSION, -1);
    }

    public int getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(int deleteTime) {
        this.deleteTime = deleteTime;
        mPref.edit().putInt(KEY.DELETE_TIME, deleteTime).commit();
    }

    public int getCheckedVersion() {
        return checkedVersion;
    }

    public void setCheckedVersion(int checkedVersion) {
        this.checkedVersion = checkedVersion;
        mPref.edit().putInt(KEY.CHECKED_VERSION, checkedVersion).commit();
    }

    public String getLatestAccount() {
        return latestAccount;
    }

    public void setLatestAccount(String latestAccount) {
        this.latestAccount = latestAccount;
        mPref.edit().putString(KEY.LAST_ACCOUNT, latestAccount).commit();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        mPref.edit().putString(KEY.SERVER_ADDRESS, serverAddress).commit();
    }

    public String getUpdateServerAddress() {
        return updateServerAddress;
    }

    public void setUpdateServerAddress(String updateServerAddress) {
        this.updateServerAddress = updateServerAddress;
        mPref.edit().putString(KEY.UPDATE_SERVER_ADDRESS, updateServerAddress).commit();
    }

    public String getLatestPassword() {
        return latestPassword;
    }

    public void setLatestPassword(String latestPassword) {
        this.latestPassword = latestPassword;
        mPref.edit().putString(KEY.LAST_PASSWORD, latestPassword).commit();
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

}
