package com.seuic.hayao.view;

import com.seuic.hayao.view.base.IBaseView;

public interface DataSyncView extends IBaseView {

    void showProgress();

    void dissmissProgress();

    void showErrorInfo(String info);

    void updateProgress(String progress);

    void showDownloadResult(int number);
}
