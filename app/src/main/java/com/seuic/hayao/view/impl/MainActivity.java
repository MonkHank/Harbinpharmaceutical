package com.seuic.hayao.view.impl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.data.local.AppCache;
import com.seuic.hayao.event.EventPosterHelper;
import com.seuic.hayao.event.data.BillNumberChangeEvent;
import com.seuic.hayao.presenter.impl.MainPresenterImpl;
import com.seuic.hayao.service.UploadService;
import com.seuic.hayao.view.MainView;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, MainView {

    @Bind(R.id.out_btn) Button mOutBtn;
    @Bind(R.id.in_btn)Button mInBtn;
    @Bind(R.id.data_sync_btn)Button mDataSyncBtn;
    @Bind(R.id.data_manager_btn)Button mDataManagerBtn;
    @Bind(R.id.log_out_btn)Button mLogoutBtn;
    @Bind(R.id.not_upload_number) TextView mNotUploadNumberTv;
    @Bind(R.id.return_manager_btn) Button mReturnManagerBtn;
    @Bind(R.id.notice_container)LinearLayout mNoticeContainer;

    private MainPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenterImpl(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mOutBtn.setOnClickListener(this);
        mInBtn.setOnClickListener(this);
        mDataSyncBtn.setOnClickListener(this);
        mDataManagerBtn.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);
        mReturnManagerBtn.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppCache.getInstance().getLoginInfo().getCorpName());
        setSupportActionBar(toolbar);
        if (!UploadService.isRunning(this)) {
            startService(UploadService.getStartIntent(this));
        }
        mPresenter.checkCorpNumber();
    }

    @Override
    protected void onResume() {
        mPresenter.updataNumber();
        EventPosterHelper.getInstance().getBus().register(this);
        super.onResume();
    }


    @Override
    protected void onPause() {
        EventPosterHelper.getInstance().getBus().unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        if (UploadService.isRunning(this)) {
            stopService(UploadService.getStartIntent(this));
        }
        super.onDestroy();
    }

    @Subscribe
    public void onBillNumberChange(BillNumberChangeEvent event) {
        mPresenter.updataNumber();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.out_btn:
                startActivity(new Intent(this, StockOutActivity.class));
                break;
            case R.id.in_btn:
                startActivity(new Intent(this, StockInActivity.class));
                break;
            case R.id.data_sync_btn:
                startActivity(new Intent(this, DataSyncActivity.class));
                break;
            case R.id.data_manager_btn:
                startActivity(new Intent(this, BillManagerActivity.class));
                break;
            case R.id.return_manager_btn:
                startActivity(new Intent(this, ReturnManagerActivity.class));
                break;
            case R.id.log_out_btn:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否注销系统？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AppCache.getInstance().setLoginInfo(null);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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
    public void onBackPressed() {
        logout();
    }

    @Override
    public void updataNumber(int number) {
//        if (number == 0) {
//            mNotUploadNumberTv.setTextColor(Color.RED);
//            mNoticeContainer.setVisibility(View.GONE);
//        } else {
//            mNoticeContainer.setVisibility(View.VISIBLE);
//            mNotUploadNumberTv.setTextColor(Color.RED);
//        }
        mNotUploadNumberTv.setText("未上传单据数：" + number);
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCorpNumberUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setMessage("往来企业有更新，是否前去下载？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, DataSyncActivity.class);
                startActivity(intent);
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_0 && event.getAction() == KeyEvent.ACTION_UP) {
            logout();
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_1 && event.getAction() == KeyEvent.ACTION_UP) {
            startActivity(new Intent(this, StockOutActivity.class));
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_2 && event.getAction() == KeyEvent.ACTION_UP) {
            startActivity(new Intent(this, StockInActivity.class));
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_3 && event.getAction() == KeyEvent.ACTION_UP) {
            startActivity(new Intent(this, BillManagerActivity.class));
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_4 && event.getAction() == KeyEvent.ACTION_UP) {
            startActivity(new Intent(this, ReturnManagerActivity.class));
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_5 && event.getAction() == KeyEvent.ACTION_UP) {
            startActivity(new Intent(this, DataSyncActivity.class));
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
