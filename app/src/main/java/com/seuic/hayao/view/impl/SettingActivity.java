package com.seuic.hayao.view.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.presenter.SettingPresenter;
import com.seuic.hayao.presenter.impl.SettingPresenterPresenterImpl;
import com.seuic.hayao.view.SettingView;
import com.seuic.update.UpdateManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, SettingView {

    @Bind(R.id.txt_ip_addrss)EditText mIPAddress;
    @Bind(R.id.btn_update_software) Button mUpdate;
    @Bind(R.id.address_group) RadioGroup mAddressGroupRg;
    @Bind(R.id.btn_confirm) Button mConfirm;
    @Bind(R.id.btn_cancel)Button mCancel;
    @Bind(R.id.delete_time_txt)EditText mDeleteTime;
    private UpdateManager mUpdateManager;
    private SettingPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("系统设置");
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //偷懒没用mvp，用了以前封装好的在线更新
        mUpdateManager = new UpdateManager(this);
        mPresenter = new SettingPresenterPresenterImpl(this);

        mUpdate.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String address = mPresenter.getAddress();
        mIPAddress.setText(address);
        mDeleteTime.setText(mPresenter.getDeleteTime() + "");
        mIPAddress.requestFocus();
        for (int i = 0; i < mAddressGroupRg.getChildCount(); i++) {
            ((RadioButton) mAddressGroupRg.getChildAt(i)).setChecked(false);
            String[] string = ((RadioButton) mAddressGroupRg.getChildAt(i)).getText().toString().split(" ");
            if (string[0].equals(address)) {
                ((RadioButton) mAddressGroupRg.getChildAt(i)).setChecked(true);
            }
        }

        mAddressGroupRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String[] string = ((RadioButton) group.findViewById(checkedId)).getText().toString().split(" ");
                mIPAddress.setText(string[0]);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdateManager = null;
    }

    private void save() {
        String address = mIPAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "IP 地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int delteTime = 30;
        try {
            delteTime = Integer.parseInt(mDeleteTime.getText().toString());
            if (delteTime < 0) {
                Toast.makeText(this, "保存时间不能小于0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "非法的保存时间", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.saveDeleteTime(delteTime);
        mPresenter.saveDeleteTime(delteTime);
        mPresenter.saveAddress(address);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_cancel:
                this.finish();
                break;
            case R.id.btn_confirm:
                save();
                break;
            case R.id.btn_update_software:
                mUpdateManager.showCheckUpdateDailog(mPresenter.getUpdateAddress());
                break;
            default:
                break;
        }
    }


    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onLoginSucess() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting_update_address) {
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_edit_text, null);
        dialog.setView(layout);
        dialog.setCancelable(false);
        dialog.setTitle("请输入更新服务器地址");
        final EditText addressTv = (EditText) layout.findViewById(R.id.update_address_text);
        addressTv.setText(mPresenter.getUpdateAddress());
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String address = addressTv.getText().toString();
                mPresenter.saveUpdateAddress(address);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        dialog.show();
    }
}
