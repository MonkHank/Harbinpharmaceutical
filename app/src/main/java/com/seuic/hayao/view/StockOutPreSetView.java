package com.seuic.hayao.view;

import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.view.base.IBaseView;

import java.util.ArrayList;

public interface StockOutPreSetView extends IBaseView {

    void onBillNumbGenerate(String bill);

    void showErrorMsg(String msg);

    void updateBillType(ArrayList<StoreTypeInfo> types);

    void onCheckFinished(Boolean isExit,String billNumber);

}
