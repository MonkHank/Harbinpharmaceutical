package com.seuic.hayao.presenter;

import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.BillManagerView;

import java.util.ArrayList;

public interface BillManagerPresenter extends IPresenter<BillManagerView> {
    void queryByBillNumber(String billNumber);

    void queryByTime(String time);

    void queryAll();

    void exportBill(ArrayList<String> billNumbers);

    void deleteBill(ArrayList<String> billNumbers);

}
