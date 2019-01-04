package com.seuic.hayao.presenter;

import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.ReturnManagerPreSetView;
import com.seuic.hayao.view.StockInPreSetView2;

public interface ReturnManagerPresenter {
    void generateBillNumber();

    void initBillTypeName();

    void checkBillNumber(String billNumber);
}
