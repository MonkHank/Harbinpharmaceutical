package com.seuic.hayao.view.impl;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.seuic.hayao.event.EventPosterHelper;
import com.seuic.hayao.event.data.BillNumberChangeEvent;
import com.seuic.hayao.modelbean.BillManagerShow;
import com.seuic.hayao.presenter.impl.BillManagerPresenterImpl;
import com.seuic.hayao.util.AndroidInfoGetter;
import com.seuic.hayao.util.ProgressDialogHelper;
import com.seuic.hayao.view.BillManagerView;
import com.seuic.listgrid.GridColumn;
import com.seuic.listgrid.ListGrid;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BillManagerActivity extends BaseActivity implements BillManagerView,
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private static final int ROW_BILL_NUMBER = 0;
    private static final int ROW_CONTACT_COMPANY = 1;
    private static final int ROW_BILL_TYPE = 2;
    private static final int ROW_BILL_DATE = 3;
    private static final int ROW_CODE_NUMBER = 4;

    @Bind(R.id.bill_number_et)
    EditText mBillNumbEt;
    @Bind(R.id.search_by_bill_btn)
    Button mSearchBtn;

    @Bind(R.id.btnChangeDate)
    ImageView mChangeDateBtn;
    @Bind(R.id.tvDate)
    TextView mDate;
    @Bind(R.id.change_time_btn)
    Button mChangeTimeBtn;

    @Bind(R.id.data_container)
    RelativeLayout mDataContainerRl;

    @Bind(R.id.btn_change_bill)
    Button mChangeBillBtn;
    @Bind(R.id.btn_export_bill)
    Button mExportBillBtn;

    private ListGrid mGrid;
    private BillManagerPresenterImpl mPresenter;
    private ProgressDialogHelper mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(R.layout.activity_bill_manager);
        mDialog = new ProgressDialogHelper(this);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("单据管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillManagerActivity.this.finish();
            }
        });
        init();
    }

    private void init() {
        mPresenter = new BillManagerPresenterImpl(this);
        mSearchBtn.setOnClickListener(this);
        mChangeDateBtn.setOnClickListener(this);
        mChangeTimeBtn.setOnClickListener(this);
        mChangeBillBtn.setOnClickListener(this);
        mExportBillBtn.setOnClickListener(this);
        float mDensity = AndroidInfoGetter.getDensity(this);
        List<GridColumn> columns = new ArrayList<GridColumn>();
        columns.add(new GridColumn("单号", (int) (150 * mDensity)));
        columns.add(new GridColumn("往来企业", (int) (230 * mDensity)));
        columns.add(new GridColumn("单据类型", (int) (100 * mDensity)));
        columns.add(new GridColumn("订单时间", (int) (110 * mDensity)));
        columns.add(new GridColumn("条码个数", (int) (100 * mDensity)));
        mGrid = new ListGrid(this, columns, ListGrid.ColumnType.WIDTH, true);
        mGrid.setCellTextSize(18);
        mGrid.setFullRowSelect(true);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mGrid.setPadding(0, 0, 0, 0);
        mGrid.setLayoutParams(lp);
        mDataContainerRl.addView(mGrid);

        mDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }


    @Override
    protected void onResume() {
        mPresenter.queryAll();
        EventPosterHelper.getInstance().getBus().register(this);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_by_bill_btn:
                queryByNumber();
                break;
            case R.id.btnChangeDate:
                Calendar c = Calendar.getInstance();
                DatePickerDialog.newInstance(this, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show(getSupportFragmentManager(), "ss");
                break;
            case R.id.change_time_btn:
                queryByTime();
                break;
            case R.id.btn_change_bill:
                changeBill();
                break;
            case R.id.btn_export_bill:
                exportBill();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        EventPosterHelper.getInstance().getBus().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onBillChange(BillNumberChangeEvent event) {
        mPresenter.queryAll();
    }

    private void changeBill() {
        ArrayList<ListGrid.ListRow> selectRows = mGrid.getSelectRows();
        if (selectRows.size() == 0) {
            Toast.makeText(this.getApplication(), "请选择要修改的单据！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectRows.size() > 1) {
            showDialog("每次只能修改一个单据！");
            return;
        }

        if (mGrid.getSelectedRows().get(0).getCellTextColor(ROW_BILL_NUMBER) != Color.RED) {//简单的用颜色值判断是否上传了
            showDialog("该订单已经上传不能修改！");
            return;
        }

        Intent intent = new Intent(this, ModifyBillActivity.class);
        intent.putExtra("bill_number", mGrid.getSelectRows().get(0).getValue(ROW_BILL_NUMBER));
        startActivity(intent);
    }

    private void exportBill() {
        ArrayList<ListGrid.ListRow> selectRows = mGrid.getSelectRows();
        if (selectRows.size() == 0) {
            Toast.makeText(this.getApplication(), "请选择要导出的单据！", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> billNumbers = new ArrayList<String>();
        for (ListGrid.ListRow row : selectRows) {
            billNumbers.add(row.getValue(ROW_BILL_NUMBER));
        }
        mPresenter.exportBill(billNumbers);
    }

    private void queryByTime() {
        String time = mDate.getText().toString();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(this.getApplication(), "请设置时间", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.queryByTime(time);
    }

    private void queryByNumber() {
        String billNumber = mBillNumbEt.getText().toString();
        if (TextUtils.isEmpty(billNumber)) {
            mPresenter.queryAll();
//            Toast.makeText(this.getApplication(), "请填写要查询的单据号！", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.queryByBillNumber(billNumber);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        String monthString = (month + 1) + "";
        if (month + 1 < 10) {
            monthString = "0" + (month + 1);
        }
        String dayString = day + "";
        if (day < 10) {
            dayString = "0" + day;
        }
        mDate.setText(year + "-" + monthString + "-" + dayString);

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
        ArrayList<ListGrid.ListRow> selectRows = mGrid.getSelectRows();
        if (selectRows.size() == 0) {
            Toast.makeText(this.getApplication(), "请选择要删除的单据！", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> billNumbers = new ArrayList<String>();
        for (ListGrid.ListRow row : selectRows) {
            billNumbers.add(row.getValue(ROW_BILL_NUMBER));
        }
        mPresenter.deleteBill(billNumbers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_manager, menu);
        return true;
    }

    @Override
    public void updateBillList(ArrayList<BillManagerShow> bills) {
        mGrid.clearRows();
        for (BillManagerShow bill : bills) {
            ListGrid.ListRow row = mGrid.newRow();
            row.setValue(ROW_BILL_NUMBER, bill.getBillNumber());
            row.setValue(ROW_CONTACT_COMPANY, bill.getContactCorpName());
            row.setValue(ROW_BILL_TYPE, bill.getBillType());
            row.setValue(ROW_BILL_DATE, bill.getDate());
            row.setValue(ROW_CODE_NUMBER, bill.getCodeNumber());
            if (!bill.isUpload()) {
                row.setCellTextColor(ROW_BILL_NUMBER, Color.RED);
                row.setCellTextColor(ROW_CONTACT_COMPANY, Color.RED);
                row.setCellTextColor(ROW_BILL_TYPE, Color.RED);
                row.setCellTextColor(ROW_BILL_DATE, Color.RED);
                row.setCellTextColor(ROW_CODE_NUMBER, Color.RED);
            }
            mGrid.insertRow(row);
        }
    }

    @Override
    public void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BillManagerActivity.this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(this.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showloadingBar(String msg) {
        mDialog.show(msg);
    }

    @Override
    public void dissMissloadingBar() {
        mDialog.dismiss();
    }
}
