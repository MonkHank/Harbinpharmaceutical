package com.seuic.hayao.view.impl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.seuic.hayao.R;
import com.seuic.hayao.adapter.TextViewCheckBoxAdapter;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.modelbean.EditCommonUseShow;
import com.seuic.hayao.presenter.EditCommonUsePresenter;
import com.seuic.hayao.presenter.impl.EditCommonUsePresenterImpl;
import com.seuic.hayao.util.ProgressDialogHelper;
import com.seuic.hayao.view.EditCommonUseView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditCommonUseActivity extends BaseActivity implements View.OnClickListener, EditCommonUseView {

    @Bind(R.id.contact_company_tv)EditText mContactCompanyTv;
    @Bind(R.id.corp_number_txt)TextView mCropNumberTv;
    @Bind(R.id.query_company_btn)Button mQueryCompanyBtn;
    @Bind(R.id.contact_company_list_lv) ListView mContactComListLv;
    @Bind(R.id.loading_pb) ProgressBar mLoadingPb;
    @Bind(R.id.btn_save)Button mSaveBtn;
    @Bind(R.id.btn_select_all) Button mSelectAllBtn;
    @Bind(R.id.btn_unselect_all) Button mUnselectAllBtn;

    private EditCommonUsePresenter mPresenter;

    private TextViewCheckBoxAdapter mAdapter;
    private ArrayList<EditCommonUseShow> mData;
    private ProgressDialogHelper mDialog;
    private String mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_edit_common_use);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mType = getIntent().getStringExtra("type");
        toolbar.setTitle("编辑常用" + ("in".equals(mType) ? "入库 " : "出库") + "往来企业");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCommonUseActivity.this.finish();
            }
        });

        mPresenter = new EditCommonUsePresenterImpl(this);
        mPresenter.setType("in".equals(mType));
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mDialog = new ProgressDialogHelper(this);
        mQueryCompanyBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mSelectAllBtn.setOnClickListener(this);
        mUnselectAllBtn.setOnClickListener(this);
        mData = new ArrayList<>();
        mAdapter = new TextViewCheckBoxAdapter(this, mData, mType);
        mContactComListLv.setAdapter(mAdapter);
        mContactComListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                char[] chars = mData.get(position).getCheckStatInfo().getCommonUse().toCharArray();
                int location = "in".equals(mType) ? 0 : 1;
                String result = null;
                String tem = (chars[location] + "").equals("1") ? "0" : "1";
                if (location == 0) {
                    result = tem + chars[1];
                } else {
                    result = chars[0] + tem;
                }
                mData.get(position).getCheckStatInfo().setCommonUse(result);
                mAdapter.notifyDataSetChanged();
                mPresenter.updataCorpNumber();
            }
        });
        mPresenter.loadAll(mContactCompanyTv);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.query_company_btn:
                mPresenter.queryCompany(null);
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.btn_select_all:
                selectAll();
                break;
            case R.id.btn_unselect_all:
                unSelectAll();
                break;
            default:
                break;
        }
    }

    private void unSelectAll() {
        for (int i = 0; i < mData.size(); i++) {
            char[] chars = mData.get(i).getCheckStatInfo().getCommonUse().toCharArray();
            int location = "in".equals(mType) ? 0 : 1;
            String result = null;
            if (location == 0) {
                result = "0" + chars[1];
            } else {
                result = chars[0] + "0";
            }
            mData.get(i).getCheckStatInfo().setCommonUse(result);
        }
        mAdapter.notifyDataSetChanged();
        mPresenter.updataCorpNumber();
    }

    private void selectAll() {
        for (int i = 0; i < mData.size(); i++) {
            char[] chars = mData.get(i).getCheckStatInfo().getCommonUse().toCharArray();
            int location = "in".equals(mType) ? 0 : 1;
            String result = null;
            if (location == 0) {
                result = "1" + chars[1];
            } else {
                result = chars[0] + "1";
            }
            mData.get(i).getCheckStatInfo().setCommonUse(result);
        }
        mAdapter.notifyDataSetChanged();
        mPresenter.updataCorpNumber();
    }

    private void save() {
        mPresenter.save();
    }

    @Override
    public void showProgress() {
        mLoadingPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void dissmissProgress() {
        mLoadingPb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorInfo(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMsg(String info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                EditCommonUseActivity.this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage(info);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditCommonUseActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void updateCorpList(ArrayList<EditCommonUseShow> datas) {
        mData.clear();
        for (EditCommonUseShow useshow : datas) {
            mData.add(useshow);
        }
        mAdapter.notifyDataSetChanged();
        mPresenter.updataCorpNumber();
    }

    @Override
    public void showProgressDialog(String msg) {
        mDialog.show(msg);
    }

    @Override
    public void dissmissProgressDialog() {
        mDialog.dismiss();
    }

    @Override
    public void updataCorpNumber(String number) {
        mCropNumberTv.setText(number);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_common_use, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_selected) {
            mPresenter.showSelected(true);
            return true;
        }

        if (id == R.id.action_unselected) {
            mPresenter.showSelected(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
