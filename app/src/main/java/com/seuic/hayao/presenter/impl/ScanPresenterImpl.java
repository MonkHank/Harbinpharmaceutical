package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.adapter.CheckBoxAdapter;
import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.presenter.ScanPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.ScanView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ScanPresenterImpl extends BasePresenter<ScanView> implements ScanPresenter {
    private DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mSubscription2;
    private Subscription mSubscription3;
    private Subscription mSubscription4;

    public ScanPresenterImpl(ScanView view) {
        mDataManager = DataManager.getInstance();
        this.attachView(view);
    }

    @Override
    public void attachView(ScanView view) {
        this.mMvpView = view;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mSubscription2 != null) mSubscription2.unsubscribe();
        if (mSubscription3 != null) mSubscription3.unsubscribe();
        if (mSubscription4 != null) mSubscription4.unsubscribe();
    }

    @Override
    public void doConfirm(final Bill bill, final ArrayList<CheckBoxAdapter.CheckBoxItmeData> mCodeListData) {
        getMvpView().showProgressDialog("正在提交...");
        mSubscription = Observable.create(new Observable.OnSubscribe<ArrayList<Barcode>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Barcode>> subscriber) {
                ArrayList<Barcode> barcodeList = new ArrayList<Barcode>();
                for (CheckBoxAdapter.CheckBoxItmeData data : mCodeListData) {
                    Barcode code = new Barcode();
                    code.setBillNumber(bill.getBillNumber());
                    code.setBarcode(data.getBarCode());
                    code.setActDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    barcodeList.add(code);
                }
                subscriber.onNext(barcodeList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<ArrayList<Barcode>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(ArrayList<Barcode> barcodes) {
                return mDataManager.doConfirm(bill, barcodes);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dismissDialog();
                getMvpView().showResult("提交失败!" + e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getMvpView().dismissDialog();
                if (aBoolean) {
                    getMvpView().forwardToPreSet("提交成功！\n网络畅通情况系统每20分钟会自动上传单据");
                } else {
                    getMvpView().showResult("提交失败!");
                }
            }
        });
    }

    @Override
    public void doUpload(final Bill bill, final ArrayList<CheckBoxAdapter.CheckBoxItmeData> mCodeListData) {
        getMvpView().showProgressDialog("正在上传...");
        mSubscription2 = Observable.create(new Observable.OnSubscribe<ArrayList<Barcode>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Barcode>> subscriber) {
                ArrayList<Barcode> barcodeList = new ArrayList<Barcode>();
                for (CheckBoxAdapter.CheckBoxItmeData data : mCodeListData) {
                    Barcode code = new Barcode();
                    code.setBillNumber(bill.getBillNumber());
                    code.setBarcode(data.getBarCode());
                    code.setActDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    barcodeList.add(code);
                }
                subscriber.onNext(barcodeList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<ArrayList<Barcode>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(ArrayList<Barcode> barcodes) {
                return mDataManager.uploadBill(bill, barcodes);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dismissDialog();
                getMvpView().showResult("上传失败!" + e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getMvpView().dismissDialog();
                if (aBoolean) {
                    getMvpView().forwardToPreSet("上传成功！");
                } else {
                    getMvpView().showResult("上传失败!");
                }
            }
        });
    }

    @Override
    public void checkBarcodeInDB(final String code, String storeTypeId) {
        mSubscription3 = mDataManager.checkBarCode(code, storeTypeId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showResult(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getMvpView().onCheckFinish(code, aBoolean);
                    }
                });
    }

    private int time = 60;

    @Override
    public void resetTime() {
        this.time = 60;
    }

    @Override
    public void startTick() {
        mSubscription4 = Observable.interval(0, 1, TimeUnit.SECONDS).filter(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                return --time <= 0;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Long aLong) {
                        getMvpView().closeContinueScan();
                    }
                });
    }
}
