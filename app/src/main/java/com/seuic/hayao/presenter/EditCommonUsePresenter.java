package com.seuic.hayao.presenter;

import android.widget.EditText;

import com.seuic.hayao.data.bean.UserCorpToCorp;
import com.seuic.hayao.modelbean.EditCommonUseShow;
import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.BillManagerView;
import com.seuic.hayao.view.EditCommonUseView;

import java.util.ArrayList;

public interface EditCommonUsePresenter extends IPresenter<EditCommonUseView> {

    void queryCompany(String condition);

    void save();

    void loadAll(EditText view);

    void updataCorpNumber();

    void showSelected(boolean isSelected);

    void setType(boolean isInType);

}
