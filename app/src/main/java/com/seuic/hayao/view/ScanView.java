package com.seuic.hayao.view;

import com.seuic.hayao.view.base.IBaseView;

import java.util.Map;

public interface ScanView extends IBaseView {

    void showResult(String result);

    void showProgressDialog(String content);

    void dismissDialog();

    void forwardToPreSet(String msg);

    void onCheckFinish(String barcode, boolean result);

    void closeContinueScan();

}
