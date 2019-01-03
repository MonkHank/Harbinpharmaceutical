package com.seuic.hayao.presenter;

import com.seuic.hayao.adapter.CheckBoxAdapter;
import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.BillManagerView;
import com.seuic.hayao.view.ModifyBillView;

import java.util.ArrayList;

public interface ModifyBillPresenter extends IPresenter<ModifyBillView> {

    void initData(String billNumber);

    void doConfirm(Bill bill, ArrayList<CheckBoxAdapter.CheckBoxItmeData> mCodeListData);

    void doUpload(Bill bill, ArrayList<CheckBoxAdapter.CheckBoxItmeData> mCodeListData);

    void checkBarcodeInDB(String code, String storeTypeId);

    void resetTime();

    void startTick();

    void recovryBill(String billNumber);
}
