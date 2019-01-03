package com.seuic.hayao.view.impl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.presenter.DataSyncPresenter;
import com.seuic.hayao.presenter.impl.DataSyncPresenterImpl;
import com.seuic.hayao.util.ProgressDialogHelper;
import com.seuic.hayao.view.DataSyncView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 下载往来企业
 */
public class DataSyncActivity extends BaseActivity implements DataSyncView, View.OnClickListener {

    @Bind(R.id.download_contact_company_btn) Button mContactCompanyBtn;
    @Bind(R.id.edit_common_use_out_btn) Button mPlanTaskBtn;
    @Bind(R.id.edit_common_use_btn) Button mEditCommonUseBtn;
    @Bind(R.id.progress_container_ll) LinearLayout mProgressContainerLl;
    @Bind(R.id.down_load_number_txt) TextView mDownloadNumberTv;

    DataSyncPresenter mPresenter;

    private ProgressDialogHelper mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sync);
        mDialog = new ProgressDialogHelper(this);
        // 屏幕常亮
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPresenter = new DataSyncPresenterImpl(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("下载往来企业");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSyncActivity.this.finish();
            }
        });
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContactCompanyBtn.setOnClickListener(this);
        mPlanTaskBtn.setOnClickListener(this);
        mEditCommonUseBtn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.download_contact_company_btn: // 下载往来企业
                mDownloadNumberTv.setText("0");
                mPresenter.startSyncContactCompany();
                break;
            case R.id.edit_common_use_out_btn: // 编辑常用出库
                Intent intent = new Intent(this, EditCommonUseActivity.class);
                intent.putExtra("type", "out");
                startActivity(intent);
                break;
            case R.id.edit_common_use_btn: // 编辑常用入库
                Intent intent1 = new Intent(this, EditCommonUseActivity.class);
                intent1.putExtra("type", "in");
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void showProgress() {
//        mProgressContainerLl.setVisibility(View.VISIBLE);
        mDialog.show("正在下载往来企业...");
    }

    @Override
    public void dissmissProgress() {
        mDialog.dismiss();
//        mProgressContainerLl.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorInfo(String info) {
        Toast.makeText(this.getApplication(), info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateProgress(String progress) {
//        mDownloadNumberTv.setText(progress);
        mDialog.show("已下载往来企业数量：" + progress);
    }

    @Override
    public void showDownloadResult(int number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataSyncActivity.this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("更新往来企业成功，共下载往来企业 " + number + " 个");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
