package com.seuic.hayao.view.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.adapter.StoreTypeAdapter;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.event.EventPosterHelper;
import com.seuic.hayao.event.data.ScanDataEvent;
import com.seuic.hayao.presenter.StockInPreSetPresenter;
import com.seuic.hayao.presenter.impl.StockInPreSetPresenterImpl;
import com.seuic.hayao.view.StockInPreSetView;
import com.seuic.hayao.view.impl.StockInActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 采购入库——单据号生成fragment
 */
public class StockInPreSetFragment extends Fragment implements View.OnClickListener, StockInPreSetView {

    @Bind(R.id.bills_number_et) EditText mBillsNumEt;
    @Bind(R.id.generate_number_btn)Button mGenNumBtn;
    @Bind(R.id.bills_type_sp)Spinner mBillTypeSp;
    @Bind(R.id.btn_sure)Button mSureBtn;
    @Bind(R.id.btn_back)Button mBackBtn;

    private StockInPreSetPresenter mPresenter;
    private StoreTypeAdapter mSpAdapter;

    private ArrayList<StoreTypeInfo> infos = new ArrayList<StoreTypeInfo>();

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
    public void onPause() {
        EventPosterHelper.getInstance().getBus().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_stock_in_pre_set, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mPresenter = new StockInPreSetPresenterImpl(this);
        mPresenter.generateBillNumber();
        mSpAdapter = new StoreTypeAdapter(this.getActivity(), infos);
        mBillTypeSp.setAdapter(mSpAdapter);
        mPresenter.initBillTypeName(StoreKind.In);
        mGenNumBtn.setOnClickListener(this);
        mSureBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.generate_number_btn: // 生成
                generate();
                break;
            case R.id.btn_sure:// 确定
                sure();
                break;
            case R.id.btn_back: // 返回
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    private void sure() {
        if (TextUtils.isEmpty(mBillsNumEt.getText().toString())) {
            Toast.makeText(getActivity().getApplication(), "请点击“生成”按钮生成单据号！", Toast.LENGTH_SHORT).show();
            return;
        }
        StoreTypeInfo ss = null;
        try {
            ss = (StoreTypeInfo) mSpAdapter.getItem(mBillTypeSp.getSelectedItemPosition());
        } catch (Exception e) {
//            Toast.makeText(getActivity(),"请稍等..",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        if (ss == null) {
            return;
        }
        if (TextUtils.isEmpty(mBillsNumEt.getText().toString())) {
            Toast.makeText(this.getActivity(), "请设置单据号", Toast.LENGTH_SHORT).show();
            return;
        }

        mPresenter.checkBillNumber(mBillsNumEt.getText().toString());
    }

    private void generate() {
        mPresenter.generateBillNumber();
    }

    @Override
    public void onBillNumbGenerate(String bill) {
        mBillsNumEt.setText(bill);
        mBillsNumEt.setSelection(bill.length());
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(getActivity().getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateBillType(ArrayList<StoreTypeInfo> types) {
        infos.clear();
        for (StoreTypeInfo info : types) {
            if ("采购入库".equals(info.getStoreTypeText())) {
                infos.add(0, info);
            } else {
                infos.add(info);
            }
        }
        mSpAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckFinished(Boolean isExit, String billNumber) {
        if (!isExit) {
            ((StockInActivity) getActivity()).forwardToNext(billNumber, (StoreTypeInfo) mSpAdapter.getItem(mBillTypeSp.getSelectedItemPosition()));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    this.getActivity());
            builder.setTitle("提示");
            builder.setMessage("该单据号已经存在，请重新生成单据号！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    @Subscribe
    public void OnBarcode(ScanDataEvent event) {
        mBillsNumEt.setText(event.getBarCode());
        mBillsNumEt.setSelection(event.getBarCode().length());
    }

}
