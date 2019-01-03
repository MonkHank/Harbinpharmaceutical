package com.seuic.hayao.presenter;

import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.DataSyncView;

public interface DataSyncPresenter extends IPresenter<DataSyncView> {
    void startSyncContactCompany();
}
