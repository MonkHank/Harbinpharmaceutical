package com.seuic.hayao.view;

import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.modelbean.EditCommonUseShow;
import com.seuic.hayao.view.base.IBaseView;

import java.util.ArrayList;

public interface EditCommonUseView extends IBaseView {

    void showProgress();

    void dissmissProgress();

    void showErrorInfo(String info);

    void showMsg(String info);

    void updateCorpList(ArrayList<EditCommonUseShow> datas);

    void showProgressDialog(String msg);

    void dissmissProgressDialog();

    void updataCorpNumber(String number);

}
