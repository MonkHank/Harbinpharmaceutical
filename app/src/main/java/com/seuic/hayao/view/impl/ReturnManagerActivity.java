package com.seuic.hayao.view.impl;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seuic.hayao.R;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.modelbean.StockInPreSetData;
import com.seuic.hayao.view.fragment.ReturnManagerPreSetFragment;
import com.seuic.hayao.view.fragment.ReturnManagerPreSetFragment2;
import com.seuic.hayao.view.fragment.ReturnManagerScanFragment;

import java.util.Date;

public class ReturnManagerActivity extends BaseActivity {

    private FragmentManager mFragmentManager;

    private String mBillNumber;
    private StoreTypeInfo mBillType;
    private SmartCorpInfo corpInfo;

    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("退货管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safeBack();
            }
        });
        mFragmentManager = getFragmentManager();
        init();
    }

    private void init() {
        forwardToPreSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void forwardToNext(String billNumber, StoreTypeInfo info) {
        ReturnManagerPreSetFragment2 fragment = new ReturnManagerPreSetFragment2();
        this.mBillNumber = billNumber;
        this.mBillType = info;
        Bundle b = new Bundle();
        b.putSerializable("type", mBillType.getStoreKind() == StoreKind.In);
        fragment.setArguments(b);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content_container, fragment, "pre_set2_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void forwardToScan(SmartCorpInfo info) {
        mMenu.getItem(0).setVisible(true);
        this.corpInfo = info;
        ReturnManagerScanFragment fragment = new ReturnManagerScanFragment();
        StockInPreSetData data = new StockInPreSetData();
        data.setBillNumb(this.mBillNumber);
        data.setBillType(this.mBillType);
        data.setContactCompany(this.corpInfo);
        data.setCreateTime(new Date());
        Bundle b = new Bundle();
        b.putSerializable("key_scan_value", data);
        fragment.setArguments(b);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content_container, fragment, "scan_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void forwardToPreSet() {
        if (mMenu != null) mMenu.getItem(0).setVisible(false);
        mBillNumber = null;
        mBillType = null;
        corpInfo = null;
        int backStackCount = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            getFragmentManager().popBackStack();
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content_container, new ReturnManagerPreSetFragment(), "pre_set1_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_manager, menu);
        mMenu = menu;
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_del) {
            ((ReturnManagerScanFragment) mFragmentManager.findFragmentByTag("scan_fragment")).delete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void safeBack() {
        ReturnManagerScanFragment fragment = (ReturnManagerScanFragment) mFragmentManager.findFragmentByTag("scan_fragment");
        if (fragment != null && !fragment.canExit()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReturnManagerActivity.this);
            builder.setCancelable(false);
            builder.setTitle("提示");
            builder.setMessage("该单据未保存提交是否返回？");
            builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("不提交,退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ReturnManagerActivity.this.finish();
                }
            });
            builder.show();
            return;
        }
        if (mFragmentManager.getBackStackEntryCount() == 1) {
            ReturnManagerActivity.this.finish();
        } else {
            mMenu.getItem(0).setVisible(false);
            mFragmentManager.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        safeBack();
    }

}
