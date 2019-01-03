package com.seuic.hayao.presenter;

import android.widget.TextView;

import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.StockInPreSetView2;

public interface StockInPreSetPresenter2 extends IPresenter<StockInPreSetView2> {
    void queryCompany(String condition);

    void loadAll(TextView view,boolean isIn);

}
