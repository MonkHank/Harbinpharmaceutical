package com.seuic.hayao.presenter;

import android.widget.TextView;

import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.StockOutPreSetView2;

public interface StockOutPreSetPresenter2 extends IPresenter<StockOutPreSetView2> {
    void queryCompany(String condition);

    void loadAll(TextView view, boolean isIn);

}
