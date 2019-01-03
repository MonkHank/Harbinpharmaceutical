package com.seuic.hayao.view;

import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.view.base.IBaseView;

import java.util.ArrayList;

public interface StockInPreSetView2 extends IBaseView {

    void queryCompanyList(ArrayList<SmartCorpInfo> companys);

    void showErrorMsg(String msg);

    void updateList();

    void showLoadingBar();

    void dismissLoadingBar();

    void showProgressDialog(String msg);

    void dissMissProgressDialog();

}
