package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.presenter.StockOutPreSetPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.StockOutPreSetView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class StockOutPreSetPresenterImpl extends BasePresenter<StockOutPreSetView> implements StockOutPreSetPresenter {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mSubscription1;
    private Subscription mSubscription2;

    public StockOutPreSetPresenterImpl(StockOutPreSetView view) {
        mDataManager = DataManager.getInstance();
        this.attachView(view);
    }

    @Override
    public void attachView(StockOutPreSetView view) {
        this.mMvpView = view;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mSubscription1 != null) mSubscription1.unsubscribe();
        if (mSubscription2 != null) mSubscription2.unsubscribe();
    }

    @Override
    public void generateBillNumber() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String billNumb = format.format(new Date());
        mSubscription1 = mDataManager.checkBill(billNumb).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (!aBoolean) {
                    getMvpView().onBillNumbGenerate(billNumb);
                }
            }
        });
    }

    @Override
    public void initBillTypeName(StoreKind kind) {
        mSubscription = mDataManager.queryStoreTypeByKind(kind).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<StoreTypeInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorMsg(e.getMessage());

                    }

                    @Override
                    public void onNext(ArrayList<StoreTypeInfo> storeTypeInfos) {
                        getMvpView().updateBillType(storeTypeInfos);
                    }
                });
    }

    @Override
    public void checkBillNumber(final String billNumber) {
        if (!billNumber.matches("^\\d+$")) {
            getMvpView().showErrorMsg("非法的订单号,请重新生成");
            return;
        }
        mSubscription2 = mDataManager.checkBill(billNumber).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getMvpView().onCheckFinished(aBoolean, billNumber);
            }
        });
    }
}
