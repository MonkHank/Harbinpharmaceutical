package com.seuic.hayao.view.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.local.AppCache;
import com.seuic.hayao.device.ScannerHelper;
import com.seuic.hayao.event.EventPosterHelper;
import com.seuic.hayao.event.data.ScanDataEvent;
import com.seuic.hayao.modelbean.StockInPreSetData;
import com.seuic.hayao.presenter.ScanPresenter;
import com.seuic.hayao.presenter.impl.ScanPresenterImpl;
import com.seuic.hayao.sound.SoundManager;
import com.seuic.hayao.util.ProgressDialogHelper;
import com.seuic.hayao.util.ToastUtil;
import com.seuic.hayao.view.ScanView;
import com.seuic.hayao.view.impl.StockInActivity;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 采购入库——扫描条码fragment，
 */
public class StockInScanFragment extends Fragment implements
        View.OnClickListener, AdapterView.OnItemClickListener, ScanView {

    @Bind(R.id.scan_bar_code_txt) EditText mScanBarCodeEt;
    @Bind(R.id.bar_code_right_btn) Button mBarCodeRightBtn;
    @Bind(R.id.bar_code_wrong_btn) Button mBarCodeWrongBtn;
    @Bind(R.id.scan_code_numb_txt) TextView mScanCodeNumbTv;
    @Bind(R.id.tabs)  TabLayout mTabLayout;
    @Bind(R.id.vp_view)ViewPager mViewPager;
    @Bind(R.id.btn_scan_confirm) Button mScanConfirmBtn;
    @Bind(R.id.btn_scan_upload) Button mScanUploadBtn;
    @Bind(R.id.continue_scan_switch)SwitchCompat mContinueScanSc;

    private StockInPreSetData mScanPreSetData;

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

    private ScanPresenter mPresenter;

    private ProgressDialogHelper mDialog;

    private boolean isContinueScan = false;

    private AlertDialog dialog;

    private SoundManager mSoundManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        EventPosterHelper.getInstance().getBus().register(this);
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        EventPosterHelper.getInstance().getBus().unregister(this);
        super.onPause();
        closeContinueScan();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        setContinueScan(false);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_in_scan, null);
        mScanPreSetData = (StockInPreSetData) getArguments().getSerializable("key_scan_value");
        mDialog = new ProgressDialogHelper(this.getActivity());
        setContinueScan(false);
        ButterKnife.bind(this, view);
        init(inflater);
        return view;
    }

    private void init(LayoutInflater inflater) {
        mSoundManager = SoundManager.getInstance();
        mPresenter = new ScanPresenterImpl(this);
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

        viewBillInfo = inflater.inflate(R.layout.view_tab_bill_info, null);
        viewCodeList = inflater.inflate(R.layout.view_tab_code_list, null);

        mViewBillInfoBillNumTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_bill_number_txt);
        mViewBillInfoBillNumTv.setText("单据号：" + mScanPreSetData.getBillNumb());
        mViewBillInfoBillTypeTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_bill_type_txt);
        mViewBillInfoBillTypeTv.setText("单据类型：" + mScanPreSetData.getBillType().getStoreTypeText());
        mViewBillInfoBillTimeTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_bill_time_txt);
        mViewBillInfoBillTimeTv.setText("创建时间：" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(mScanPreSetData.getCreateTime()));
        mViewBillInfoContactComTv = (TextView) viewBillInfo.findViewById(R.id.stock_in_contact_company_txt);
        mViewBillInfoContactComTv.setText("往来企业：" + mScanPreSetData.getContactCompany().getCorpName());
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
        mCodeListDataAdapter = new CheckBoxAdapter(this.getActivity(), mCodeListData);
        mCodeListLv.setAdapter(mCodeListDataAdapter);
        mCodeListLv.setOnItemClickListener(this);

        dialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
//        for (int i = 0; i < 1000; i++) {
//            CheckBoxAdapter.CheckBoxItmeData dd = mCodeListDataAdapter.new CheckBoxItmeData();
//            dd.setBarCode(1000000 + i + "");
//            dd.setIsChecked(false);
//            mCodeListData.add(dd);
//        }
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

    private void doConfirm() {
        Bill bill = new Bill();
        bill.setGenerateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mScanPreSetData.getCreateTime()));
        bill.setIsUpload("0");
        bill.setStoreTypeId(mScanPreSetData.getBillType().getStoreType() + "");
        bill.setContactCorpId(mScanPreSetData.getContactCompany().getCorpId() + "");
        bill.setBillNumber(mScanPreSetData.getBillNumb());
        bill.setCreatorId(AppCache.getInstance().getLoginInfo().getUserId() + "");

//        ArrayList<Barcode> barcodeList = new ArrayList<Barcode>();
//        for (CheckBoxAdapter.CheckBoxItmeData data : mCodeListData) {
//            Barcode code = new Barcode();
//            code.setBillNumber(bill.getBillNumber());
//            code.setBarcode(data.getBarCode());
//            code.setActDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//            barcodeList.add(code);
//        }

        if (mCodeListData.size() == 0) {
            showMsg("条码列表为空，不能提交");
            return;
        }

        mPresenter.doConfirm(bill, mCodeListData);
    }

    private void doUpload() {
        Bill bill = new Bill();
        bill.setGenerateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mScanPreSetData.getCreateTime()));
        bill.setIsUpload("0");
        bill.setStoreTypeId(mScanPreSetData.getBillType().getStoreType() + "");
        bill.setContactCorpId(mScanPreSetData.getContactCompany().getCorpId() + "");
        bill.setBillNumber(mScanPreSetData.getBillNumb());
        bill.setCreatorId(AppCache.getInstance().getLoginInfo().getUserId() + "");

//        ArrayList<Barcode> barcodeList = new ArrayList<Barcode>();
//        for (CheckBoxAdapter.CheckBoxItmeData data : mCodeListData) {
//            Barcode code = new Barcode();
//            code.setBillNumber(bill.getBillNumber());
//            code.setBarcode(data.getBarCode());
//            code.setActDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//            barcodeList.add(code);
//        }

        if (mCodeListData.size() == 0) {
            showMsg("条码列表为空，不能上传");
            return;
        }

        mPresenter.doUpload(bill, mCodeListData);
    }

    private void addBarCode() {
        String barcode = mScanBarCodeEt.getText().toString();
        if (TextUtils.isEmpty(barcode)) {
            showMsg("不能添加空条码！");
            return;
        }
        mPresenter.checkBarcodeInDB(barcode, mScanPreSetData.getBillType().getStoreType() + "");
    }

    @Subscribe
    public synchronized void OnBarcode(ScanDataEvent event) {
        mPresenter.resetTime();
        if (event.getBarCode().length() != 20) {
            Toast.makeText(this.getActivity(),"无效的条码",Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.checkBarcodeInDB(event.getBarCode(), mScanPreSetData.getBillType().getStoreType() + "");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCodeListData.get(position).setIsChecked(!mCodeListData.get(position).isChecked());
        mCodeListDataAdapter.notifyDataSetChanged();
    }

    public void updataScanNumber() {
        mScanCodeNumbTv.setText(mCodeListData.size() + "");
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


    private boolean isBarcodeInList(String barcode) {
        for (CheckBoxAdapter.CheckBoxItmeData data : mCodeListData) {
            if (barcode.equals(data.getBarCode())) {
                return true;
            }
        }
        return false;
    }

    private void showMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    @Override
    public void showResult(String result) {
        showMsg(result);
    }

    @Override
    public void showProgressDialog(String content) {
        mDialog.show(content);
    }

    @Override
    public void dismissDialog() {
        mDialog.dismiss();
    }

    @Override
    public void forwardToPreSet(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((StockInActivity) getActivity()).forwardToPreSet();
            }
        });
        builder.show();
    }

    @Override
    public void onCheckFinish(String barCode, boolean result) {
        if (isBarcodeInList(barCode)) {
            mSoundManager.play();
            if (isContinueScan) {
                ToastUtil.show(this.getActivity(), barCode + "该条码已经扫描！", 1000);
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

    private void setContinueScan(boolean on) {
        if (on) {
            mPresenter.resetTime();
            mPresenter.startTick();
        }
        isContinueScan = on;
        Intent intent = new Intent(ScannerHelper.ACTION_SCANNER_APP_CONTINUNE_SETTINGS);
//        intent.putExtra(ScannerHelper.TYPE_SCAN_CONTINUE, on);
        intent.putExtra("number", 0x0C);
        intent.putExtra("value", on ? 1 : 0);
        getActivity().sendBroadcast(intent);

        Intent intent1 = new Intent(ScannerHelper.ACTION_SCANNER_APP_SETTINGS);
        intent1.putExtra(ScannerHelper.TYPE_SCAN_CONTINUE, on);
        getActivity().sendBroadcast(intent1);

    }

    public Boolean canExit() {
        return mCodeListData.size() == 0;
    }

}
