package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.presenter.ReturnManagerPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.ReturnManagerPreSetView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ReturnManagerPreSetPresenterImpl extends BasePresenter<ReturnManagerPreSetView> implements ReturnManagerPresenter {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mSubscription1;
    private Subscription mSubscription2;

    public ReturnManagerPreSetPresenterImpl(ReturnManagerPreSetView view) {
        super(view);
        mDataManager = DataManager.getInstance();
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
    public void initBillTypeName() {
        mSubscription = mDataManager.queryStoreType().observeOn(AndroidSchedulers.mainThread())
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
