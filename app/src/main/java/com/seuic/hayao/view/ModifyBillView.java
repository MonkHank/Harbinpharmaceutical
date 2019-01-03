package com.seuic.hayao.view;

import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.view.base.IBaseView;

import java.util.ArrayList;

public interface ModifyBillView extends IBaseView {

    void OnDataInit(Bill bill, ArrayList<Barcode> barcodes, SmartCorpInfo corpInfo, StoreTypeInfo storeTypeInfo);

    void showLoadingMsg(String msg);

    void dissMissLoadingMsg();

    void showErrorMsg(String msg);

    void showResult(String result);

    void showResultAndForward(String result);

    void onCheckFinish(String barcode, boolean result);

    void closeContinueScan();

    void finishUI();

}
