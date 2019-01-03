package com.seuic.hayao.presenter;

import android.widget.TextView;

import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.StockInPreSetView;
import com.seuic.hayao.view.StockOutPreSetView;

public interface StockOutPreSetPresenter extends IPresenter<StockOutPreSetView> {
    void generateBillNumber();

    void initBillTypeName(StoreKind kind);

    void checkBillNumber(String billNumber);

}
