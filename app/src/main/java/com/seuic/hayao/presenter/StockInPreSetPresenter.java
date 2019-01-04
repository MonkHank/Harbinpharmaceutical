package com.seuic.hayao.presenter;

import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.StockInPreSetView;

public interface StockInPreSetPresenter {
    void generateBillNumber();

    void initBillTypeName(StoreKind kind);

    void checkBillNumber(String billNumber);
}
