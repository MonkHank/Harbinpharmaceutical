package com.seuic.hayao.view.impl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.adapter.CheckBoxAdapter;
import com.seuic.hayao.adapter.ViewPagerAdapter;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.data.local.AppCache;
import com.seuic.hayao.device.ScannerHelper;
import com.seuic.hayao.event.EventPosterHelper;
import com.seuic.hayao.event.data.ScanDataEvent;
import com.seuic.hayao.presenter.ModifyBillPresenter;
import com.seuic.hayao.presenter.impl.ModifyBillPresenterImpl;
import com.seuic.hayao.sound.SoundManager;
import com.seuic.hayao.util.ProgressDialogHelper;
import com.seuic.hayao.util.ToastUtil;
import com.seuic.hayao.view.ModifyBillView;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ModifyBillActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ModifyBillView {

    @Bind(R.id.scan_bar_code_txt)
    EditText mScanBarCodeEt;

    @Bind(R.id.bar_code_right_btn)
    Button mBarCodeRightBtn;
    @Bind(R.id.bar_code_wrong_btn)
    Button mBarCodeWrongBtn;

    @Bind(R.id.scan_code_numb_txt)
    TextView mScanCodeNumbTv;

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.vp_view)
    ViewPager mViewPager;

    @Bind(R.id.btn_scan_confirm)
    Button mScanConfirmBtn;
    @Bind(R.id.btn_scan_upload)
    Button mScanUploadBtn;

    @Bind(R.id.continue_scan_switch)
    SwitchCompat mContinueScanSc;

    private List<View> mViewList = new ArrayList<View>();//页卡视图集合
    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合
    private View viewBillInfo, viewCodeList;//页卡视图

    TextView mViewBillInfoBillNumTv;
    TextView mViewBillInfoBillTypeTv;
    TextView mViewBillInfoBillTimeTv;
    TextView mViewBillInfoContactComTv;
    TextView mViewBillInfoCurrentBarcodeTv;

    private ListView mCodeListLv;
    private ArrayList<CheckBoxAdapter.CheckBoxItmeData> mCodeListData;
    private CheckBoxAdapter mCodeListDataAdapter;

    private Bill mBill;
    private ArrayList<Barcode> codes = new ArrayList<Barcode>();

    private ModifyBillPresenter mPresenter;

    private String mBillNumber;

    ProgressDialogHelper mDialogHelper;

    private boolean isContinueScan = false;

    private AlertDialog dialog;

    private StoreTypeInfo info;

    private SoundManager mSoundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(R.layout.activity_modify_bill);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("单据修改");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        mBillNumber = getIntent().getStringExtra("bill_number");
        init();
        mPresenter = new ModifyBillPresenterImpl(this);
        setContinueScan(false);
        mDialogHelper = new ProgressDialogHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventPosterHelper.getInstance().getBus().register(this);
        mPresenter.initData(mBillNumber);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyBillActivity.this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("您正在修改单据，现在退出您的修改将不被保存，是否继续退出？");
        builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.recovryBill(mBill.getBillNumber());
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        EventPosterHelper.getInstance().getBus().unregister(this);
        super.onPause();
        closeContinueScan();
    }

    @Override
    public void onStop() {
        setContinueScan(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void init() {
        mSoundManager = SoundManager.getInstance();
        mBarCodeRightBtn.setOnClickListener(this);
        mBarCodeWrongBtn.setOnClickListener(this);
        mScanConfirmBtn.setOnClickListener(this);
        mScanUploadBtn.setOnClickListener(this);
        mContinueScanSc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setContinueScan(isChecked);
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        viewBillInfo = inflater.inflate(R.layout.view_tab_bill_info, null);
        viewCodeList = inflater.inflate(R.layout.view_tab_code_list, null);

        mViewBillInfoBillNumTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_bill_number_txt);
        mViewBillInfoBillTypeTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_bill_type_txt);
        mViewBillInfoBillTimeTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_bill_time_txt);
        mViewBillInfoContactComTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_contact_company_txt);
        mViewBillInfoCurrentBarcodeTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_current_barcode_txt);

        mCodeListLv = (ListView) viewCodeList.findViewById(R.id.tab_code_list_lv);

        mViewList.add(viewBillInfo);
        mViewList.add(viewCodeList);
        mTitleList.add("单据信息");
        mTitleList.add("码列表");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        mCodeListData = new ArrayList<CheckBoxAdapter.CheckBoxItmeData>();
        mCodeListDataAdapter = new CheckBoxAdapter(this, mCodeListData);
        mCodeListLv.setAdapter(mCodeListDataAdapter);
        mCodeListLv.setOnItemClickListener(this);

        dialog = new AlertDialog.Builder(this).setTitle("提示").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();

    }

    public void delete() {
        ArrayList<CheckBoxAdapter.CheckBoxItmeData> deleteData = new ArrayList<CheckBoxAdapter.CheckBoxItmeData>();
        for (CheckBoxAdapter.CheckBoxItmeData data : mCodeListData) {
            if (data.isChecked()) {
                deleteData.add(data);
            }
        }

        if (deleteData.size() == 0) {
            showMsg("请勾选要删除的条码");
            return;
        }

        for (CheckBoxAdapter.CheckBoxItmeData del : deleteData) {
            mCodeListData.remove(del);
        }
        mCodeListDataAdapter.notifyDataSetChanged();
        updataScanNumber();
    }

    private void doConfirm() {

        Bill bill = new Bill();
        bill.setGenerateTime(mBill.getGenerateTime());
        bill.setIsUpload("0");
        bill.setStoreTypeId(mBill.getStoreTypeId());
        bill.setContactCorpId(mBill.getContactCorpId());
        bill.setBillNumber(mBill.getBillNumber());
        bill.setCreatorId(AppCache.getInstance().getLoginInfo().getUserId() + "");

        if (mCodeListData.size() == 0) {
            showErrorMsg("条码列表为空，不能提交!");
            return;
        }

        mPresenter.doConfirm(bill, mCodeListData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_del) {
            delete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Subscribe
    public synchronized void OnBarcode(ScanDataEvent event) {
        mPresenter.resetTime();
        if (event.getBarCode().length() != 20) {
            Toast.makeText(this,"无效的条码",Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.checkBarcodeInDB(event.getBarCode(), info.getStoreType() + "");
    }

    public void updataScanNumber() {
        mScanCodeNumbTv.setText(mCodeListData.size() + "");
    }

    private void showMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean isBarcodeInList(String barcode) {
        for (CheckBoxAdapter.CheckBoxItmeData data : mCodeListData) {
            if (barcode.equals(data.getBarCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.bar_code_right_btn:
                addBarCode();
                break;
            case R.id.bar_code_wrong_btn:
                mScanBarCodeEt.setText("");
                break;
            case R.id.btn_scan_confirm:
                doConfirm();
                break;
            case R.id.btn_scan_upload:
                doUpload();
                break;
            default:
                break;
        }

    }

    private void addBarCode() {

        String barcode = mScanBarCodeEt.getText().toString();
        if (TextUtils.isEmpty(barcode)) {
            showMsg("不能添加空条码！");
            return;
        }

        mPresenter.checkBarcodeInDB(barcode, info.getStoreType() + "");
    }


    private void doUpload() {
        Bill bill = new Bill();
        bill.setGenerateTime(mBill.getGenerateTime());
        bill.setIsUpload("0");
        bill.setStoreTypeId(mBill.getStoreTypeId());
        bill.setContactCorpId(mBill.getContactCorpId());
        bill.setBillNumber(mBill.getBillNumber());
        bill.setCreatorId(AppCache.getInstance().getLoginInfo().getUserId() + "");

        if (mCodeListData.size() == 0) {
            showMsg("条码列表为空，不能上传");
            return;
        }

        mPresenter.doUpload(bill, mCodeListData);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCodeListData.get(position).setIsChecked(!mCodeListData.get(position).isChecked());
        mCodeListDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnDataInit(Bill bill, ArrayList<Barcode> barcodes, SmartCorpInfo corpInfo, StoreTypeInfo storeTypeInfo) {
        this.mBill = bill;
        this.info = storeTypeInfo;
        codes.clear();
        mCodeListData.clear();
        for (Barcode code : barcodes) {
            codes.add(code);
        }
        mViewBillInfoBillNumTv.setText("单据号：" + bill.getBillNumber());
        mViewBillInfoBillTypeTv.setText("单据类型：" + storeTypeInfo.getStoreTypeText());
        mViewBillInfoBillTimeTv.setText("创建时间：" + bill.getGenerateTime());
        mViewBillInfoContactComTv.setText("往来企业：" + corpInfo.getCorpName());

        for (Barcode code : barcodes) {
            CheckBoxAdapter.CheckBoxItmeData data = mCodeListDataAdapter.new CheckBoxItmeData();
            data.setIsChecked(false);
            data.setBarCode(code.getBarcode());
            mCodeListData.add(data);
        }
        mCodeListDataAdapter.notifyDataSetChanged();
        updataScanNumber();
    }

    @Override
    public void showLoadingMsg(String msg) {
        mDialogHelper.show(msg);
    }

    @Override
    public void dissMissLoadingMsg() {
        mDialogHelper.dismiss();
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showResult(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage(result);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void showResultAndForward(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage(result);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ModifyBillActivity.this.finish();
            }
        });
        builder.show();
    }

    @Override
    public void onCheckFinish(String barCode, boolean result) {
        if (isBarcodeInList(barCode)) {
            mSoundManager.play();
            if (isContinueScan) {
                ToastUtil.show(this, barCode + "该条码已经扫描！", 1000);
            } else {
                dialog.setMessage(barCode + "\n该条码已经扫描！");
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
            return;
        }
        if (result) {
            mSoundManager.play();
            dialog.setMessage(barCode + "\n该条码在其他单据已经扫描！");
            if (!dialog.isShowing()) {
                dialog.show();
            }
            return;
        }
        mViewBillInfoCurrentBarcodeTv.setText(barCode);
        CheckBoxAdapter.CheckBoxItmeData data = mCodeListDataAdapter.new CheckBoxItmeData();
        data.setIsChecked(false);
        data.setBarCode(barCode);
        mCodeListData.add(data);
        mCodeListDataAdapter.notifyDataSetChanged();
        updataScanNumber();
        mScanBarCodeEt.setText("");
    }

    @Override
    public void closeContinueScan() {
        setContinueScan(false);
        mContinueScanSc.setChecked(false);
    }

    @Override
    public void finishUI() {
        this.finish();
    }

    private void setContinueScan(boolean on) {
        if (on) {
            mPresenter.resetTime();
            mPresenter.startTick();
        }
        isContinueScan = on;
        Intent intent = new Intent(ScannerHelper.ACTION_SCANNER_APP_CONTINUNE_SETTINGS);
        intent.putExtra("number", 0x0C);
        intent.putExtra("value", on ? 1 : 0);
        sendBroadcast(intent);

        Intent intent1 = new Intent(ScannerHelper.ACTION_SCANNER_APP_SETTINGS);
        intent1.putExtra(ScannerHelper.TYPE_SCAN_CONTINUE, on);
        sendBroadcast(intent1);
    }
}
