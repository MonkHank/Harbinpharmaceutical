package com.seuic.hayao.view.impl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.device.BarCodeReceiver;
import com.seuic.hayao.device.ScanService;
import com.seuic.hayao.modelbean.LoginInfo;
import com.seuic.hayao.presenter.LoginPresenter;
import com.seuic.hayao.presenter.impl.LoginPresenterImpl;
import com.seuic.hayao.service.DeleteService;
import com.seuic.hayao.util.AndroidInfoGetter;
import com.seuic.hayao.util.ProgressDialogHelper;
import com.seuic.hayao.view.LoginView;
import com.seuic.update.UpdateInfo;
import com.seuic.update.UpdateManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {

    @Bind(R.id.txt_account)EditText mAccount;
    @Bind(R.id.txt_password)EditText mPassword;
    @Bind(R.id.btn_setting) Button mSettingBtn;
    @Bind(R.id.btn_login) Button mLoginBtn;
    @Bind(R.id.btn_cancel) Button mBackBtn;
    @Bind(R.id.txt_version)TextView mVersion;
    @Bind(R.id.sn_txt)TextView mSNTv;

    private LoginPresenter mPresenter;
    private ProgressDialogHelper mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPresenter = new LoginPresenterImpl(this);
        mDialog = new ProgressDialogHelper(this);
        mSettingBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);

        if (!DeleteService.isRunning(this)) {
            startService(DeleteService.getStartIntent(this));
        }

        if (!ScanService.isRunning(getApplicationContext())) {
            startService(ScanService.getStartIntent(getApplicationContext()));
        }
        initScanSetting();
    }

    private void initScanSetting() {

//        Intent intent = new Intent("com.android.scanner.service_settings");//关闭扫描工具声音
//        intent.putExtra("sound_play", false);
//        sendBroadcast(intent);

//        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        int volume = max * 100 / 100;
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String versionString = "版本号：" + AndroidInfoGetter.getVersion(getApplication());
        mVersion.setText(versionString);
        String last = mPresenter.getLastAccount();
        if (!TextUtils.isEmpty(last)) {
            mAccount.setText(mPresenter.getLastAccount());
            mPassword.requestFocus();
        }
        String pwd = mPresenter.getLastPassword();
        if (!TextUtils.isEmpty(pwd)) {
            mPassword.setText(mPresenter.getLastPassword());
            mPassword.setSelection(pwd.length());
        }
        mSNTv.setText("SN：" + AndroidInfoGetter.getDeviceSN().toUpperCase());
        mPresenter.checkUpdate();
    }

    @Override
    public void showUpdate(final UpdateInfo info) {
        boolean canUpdate = AndroidInfoGetter.getVersionCode(this) < info.getVersion();
        if (canUpdate && mPresenter.getCheckVersion() < info.getVersion()) {
            String tile = "软件版本更新";
            ArrayList<String> description = info.getDescription();
            StringBuilder sb = new StringBuilder();
            sb.append("发现新版本," + "大小：" + info.getSize() + "\n");
            sb.append("更新内容：" + "\n");
            for (String s : description) {
                sb.append(s);
                sb.append("\n");
            }
            String massage = sb.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(tile);
            builder.setMessage(massage);
            builder.setCancelable(false);
            builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new UpdateManager(LoginActivity.this).setUpdateInfo(info,mPresenter.getUpdateAddress()).showDownloadDialog();
                }
            });
            builder.setNeutralButton("以后再说", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("不再提醒", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoginActivity.this, "该版本更新将不在提示，如需更新请进入\"设置\"界面更新", Toast.LENGTH_LONG).show();
                    mPresenter.setCheckVesion(info.getVersion());
                    dialog.dismiss();
                }
            });
            AlertDialog noticeDialog = builder.create();
            noticeDialog.show();
        }
    }

    @Override
    public void onBackPressed() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                LoginActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否退出应用系统？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stopService(ScanService.getStartIntent(LoginActivity.this));
                AndroidInfoGetter.toggleComponent(LoginActivity.this, BarCodeReceiver.class, false);
                exit();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(DeleteService.getStartIntent(this));
    }

    @Override
    public void showProgressDialog() {
        mDialog.show("正在登陆中...");
    }

    @Override
    public void hideProgressDialog() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSucess() {
        initScanSetting();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_cancel:
                exit();
                break;
            default:
                break;
        }
    }

    private void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void login() {
        String account = mAccount.getText().toString();
        String pwd = mPassword.getText().toString();

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginInfo info = new LoginInfo();
        info.setPassword(pwd);
        info.setAccount(account);
        mPresenter.login(info);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            login();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
