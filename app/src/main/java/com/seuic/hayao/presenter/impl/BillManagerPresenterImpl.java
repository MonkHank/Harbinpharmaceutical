package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.modelbean.BillManagerShow;
import com.seuic.hayao.presenter.BillManagerPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.BillManagerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class BillManagerPresenterImpl extends BasePresenter<BillManagerView> implements BillManagerPresenter {

    private final DataManager mDataManager;
    private Subscription mSubscription1;
    private Subscription mSubscription2;
    private Subscription mSubscription3;
    private Subscription mSubscription4;
    private Subscription mSubscription5;

    public BillManagerPresenterImpl(BillManagerView view) {
        super(view);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription1 != null) mSubscription1.unsubscribe();
        if (mSubscription2 != null) mSubscription2.unsubscribe();
        if (mSubscription3 != null) mSubscription3.unsubscribe();
        if (mSubscription4 != null) mSubscription4.unsubscribe();
        if (mSubscription5 != null) mSubscription5.unsubscribe();
    }

    @Override
    public void queryByBillNumber(String billNumber) {
        getMvpView().showloadingBar("正在查询...");
        final ArrayList<BillManagerShow> shows = new ArrayList<BillManagerShow>();
        mSubscription1 = mDataManager.queryBillByNumber(billNumber).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BillManagerShow>() {
            @Override
            public void onCompleted() {
                getMvpView().dissMissloadingBar();
                getMvpView().updateBillList(shows);
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(BillManagerShow billManagerShow) {
                shows.add(billManagerShow);
            }
        });

    }

    @Override
    public void queryByTime(String time) {
        getMvpView().showloadingBar("正在查询...");
        final ArrayList<BillManagerShow> shows = new ArrayList<BillManagerShow>();
        mSubscription2 = mDataManager.queryBillByTime(time).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BillManagerShow>() {
            @Override
            public void onCompleted() {
                getMvpView().dissMissloadingBar();
                getMvpView().updateBillList(shows);
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissloadingBar();
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(BillManagerShow billManagerShow) {
                shows.add(billManagerShow);
            }
        });
    }

    @Override
    public void queryAll() {
        getMvpView().showloadingBar("正在加载单据信息...");
        mSubscription3 = mDataManager.queryAllBills().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BillManagerShow>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dissMissloadingBar();
                        getMvpView().showErrorMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BillManagerShow> show) {
                        getMvpView().dissMissloadingBar();
                        getMvpView().updateBillList((ArrayList<BillManagerShow>) show);
                    }
                });
    }

    @Override
    public void exportBill(ArrayList<String> billNumbers) {
        getMvpView().showloadingBar("正在生成文件...");
        mSubscription4 = mDataManager.exportBill(billNumbers).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissloadingBar();
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getMvpView().dissMissloadingBar();
                getMvpView().showDialog(aBoolean ? "生成成功！" : "生成失败！");
            }
        });

    }

    @Override
    public void deleteBill(ArrayList<String> billNumbers) {
        mSubscription5 = mDataManager.deleteBillAndBarcodeByNumber(billNumbers).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                getMvpView().showErrorMsg("删除成功！");
//                getMvpView().showDialog("删除成功！");
                queryAll();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showErrorMsg("删除失败！");
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }
}
