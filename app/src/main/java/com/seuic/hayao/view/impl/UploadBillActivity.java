package com.seuic.hayao.view.impl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.seuic.hayao.R;
import com.seuic.hayao.common.BaseActivity;
import com.seuic.hayao.modelbean.BillManagerShow;
import com.seuic.hayao.presenter.BillManagerPresenter;
import com.seuic.hayao.presenter.impl.BillManagerPresenterImpl;
import com.seuic.hayao.util.AndroidInfoGetter;
import com.seuic.hayao.view.BillManagerView;
import com.seuic.listgrid.GridColumn;
import com.seuic.listgrid.ListGrid;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadBillActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.bill_number_et)
    EditText mBillNumbEt;
    @Bind(R.id.search_by_bill_btn)
    Button mSearchBtn;

    private ListGrid mGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bill);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("单据上传");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadBillActivity.this.finish();
            }
        });

        init();
    }

    private void init() {
        float mDensity = AndroidInfoGetter.getDensity(this);
        List<GridColumn> columns = new ArrayList<GridColumn>();
        columns.add(new GridColumn("单号", (int) (150 * mDensity)));
        columns.add(new GridColumn("往来企业", (int) (200 * mDensity)));
        columns.add(new GridColumn("单据类型", (int) (100 * mDensity)));
        mGrid = new ListGrid(this, columns, ListGrid.ColumnType.WIDTH, true);
        mGrid.setCellTextSize(16);
        mGrid.setFullRowSelect(true);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mGrid.setPadding(0, 0, 0, 0);
        mGrid.setLayoutParams(lp);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_by_bill_btn:
                break;
            case R.id.btnChangeDate:
                break;
            case R.id.change_time_btn:
                break;
            case R.id.btn_change_bill:
                break;
            case R.id.btn_export_bill:
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_del) {
            deleteBill();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteBill() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_manager, menu);
        return true;
    }

}
