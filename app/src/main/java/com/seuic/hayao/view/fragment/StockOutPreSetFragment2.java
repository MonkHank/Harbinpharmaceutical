package com.seuic.hayao.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.adapter.TextViewAdapter;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.event.EventPosterHelper;
import com.seuic.hayao.event.data.ScanDataEvent;
import com.seuic.hayao.presenter.StockOutPreSetPresenter2;
import com.seuic.hayao.presenter.impl.StockOutPreSetPresenterImpl2;
import com.seuic.hayao.util.ProgressDialogHelper;
import com.seuic.hayao.view.StockOutPreSetView2;
import com.seuic.hayao.view.impl.StockOutActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockOutPreSetFragment2 extends Fragment implements View.OnClickListener, StockOutPreSetView2 {


    @Bind(R.id.contact_company_actv)
    EditText mContactComActv;
    @Bind(R.id.query_company_btn)
    Button mQueryComBtn;

    @Bind(R.id.btn_scan_confirm)
    Button mSureBtn;
    @Bind(R.id.btn_scan_back)
    Button mBackBtn;

    @Bind(R.id.contact_company_list_lv)
    ListView mContactComListLv;

    @Bind(R.id.loading_pb)
    ProgressBar mLoadingPb;

    private ArrayList<SmartCorpInfo> mCompanyList;
    private TextViewAdapter mAdapter;
    private StockOutPreSetPresenter2 mPresenter;

    private SmartCorpInfo mSelectCorp;
    private ProgressDialogHelper mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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

    @Subscribe
    public void OnBarcode(ScanDataEvent event) {
        mContactComActv.setText(event.getBarCode());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_stock_in_pre_set2, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mDialog = new ProgressDialogHelper(this.getActivity());
        mPresenter = new StockOutPreSetPresenterImpl2(this);
        mLoadingPb.setVisibility(View.INVISIBLE);
        mCompanyList = new ArrayList<SmartCorpInfo>();
        mAdapter = new TextViewAdapter(this.getActivity(), mCompanyList);
//        mContactComActv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (mContactComListLv.getVisibility() == View.INVISIBLE) {
//                    mContactComListLv.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s.toString())) {
//                    mSelectCorp = null;
//                }
//                String condition = s.toString();
//                mPresenter.queryCompany(condition);
//            }
//        });
        mContactComListLv.setAdapter(mAdapter);
        mContactComListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectCorp = (SmartCorpInfo) mAdapter.getItem(position);
                String text = mSelectCorp.getCorpName();
                mContactComActv.setText(text);
                mContactComActv.setSelection(text.length());
            }
        });
        mQueryComBtn.setOnClickListener(this);
        mSureBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mPresenter.loadAll(mContactComActv, false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.query_company_btn:
                sure();
//                query();
                break;
            case R.id.btn_scan_confirm:
                sure();
                break;
            case R.id.btn_scan_back:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    private void query() {
        mPresenter.queryCompany(null);
    }

    private void sure() {

        if (mSelectCorp == null) {
            Toast.makeText(getActivity().getApplication(), "请点选择往来企业", Toast.LENGTH_SHORT).show();
            return;
        }
        ((StockOutActivity) getActivity()).forwardToScan(mSelectCorp);
    }

    @Override
    public void queryCompanyList(ArrayList<SmartCorpInfo> companys) {
        mCompanyList.clear();
        for (SmartCorpInfo company : companys) {
            mCompanyList.add(company);
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(getActivity().getApplication(), msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void updateList() {
        mAdapter.notifyDataSetChanged();
        mContactComListLv.setSelection(0);
    }

    @Override
    public void showLoadingBar() {
        mLoadingPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingBar() {
        mLoadingPb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgressDialog(String msg) {
        mDialog.show(msg);
    }

    @Override
    public void dissMissProgressDialog() {
        mDialog.dismiss();
    }


}
