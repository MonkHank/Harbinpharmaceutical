package com.seuic.hayao.view;

import com.seuic.hayao.modelbean.BillManagerShow;
import com.seuic.hayao.view.base.IBaseView;

import java.util.ArrayList;

public interface BillManagerView extends IBaseView {

    void updateBillList(ArrayList<BillManagerShow> bills);

    void showDialog(String msg);

    void showErrorMsg(String msg);

    void showloadingBar(String msg);

    void dissMissloadingBar();

}
