package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.adapter.CheckBoxAdapter;
import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.presenter.ModifyBillPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.ModifyBillView;

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

public class ModifyBillPresenterImpl extends BasePresenter<ModifyBillView> implements ModifyBillPresenter {

    private final DataManager mDataManager;
    private Subscription mSubscription1;
    private Subscription mSubscription2;
    private Subscription mSubscription3;
    private Subscription mSubscription4;
    private Subscription mSubscription5;
    private Subscription mSubscription6;
    private Subscription mSubscription7;
    private Subscription mSubscription8;
    private Subscription mSubscription9;

    private Bill mBill;
    private SmartCorpInfo mCorpInfo;
    private StoreTypeInfo mInfo;

    public ModifyBillPresenterImpl(ModifyBillView view) {
        mDataManager = DataManager.getInstance();
        this.attachView(view);
    }

    @Override
    public void attachView(ModifyBillView view) {
        this.mMvpView = view;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
        if (mSubscription1 != null) mSubscription1.unsubscribe();
        if (mSubscription2 != null) mSubscription2.unsubscribe();
        if (mSubscription3 != null) mSubscription3.unsubscribe();
        if (mSubscription4 != null) mSubscription4.unsubscribe();
        if (mSubscription5 != null) mSubscription5.unsubscribe();
        if (mSubscription6 != null) mSubscription6.unsubscribe();
        if (mSubscription7 != null) mSubscription7.unsubscribe();
        if (mSubscription8 != null) mSubscription8.unsubscribe();
        if (mSubscription9 != null) mSubscription9.unsubscribe();
    }


    @Override
    public void initData(final String billNumber) {
        getMvpView().showLoadingMsg("正在加载单据信息...");
        mSubscription1 = mDataManager.queryBill(billNumber).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Bill>() {
            @Override
            public void onCompleted() {
                getCorpInfo();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissLoadingMsg();
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(Bill bill) {
                mBill = bill;
            }
        });
    }

    private void getCorpInfo() {
        getMvpView().showLoadingMsg("正在加载往来企业信息...");
        mSubscription2 = mDataManager.queryContactCorpById(mBill.getContactCorpId()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SmartCorpInfo>() {
            @Override
            public void onCompleted() {
                getStroeTypeInfo();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissLoadingMsg();
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(SmartCorpInfo corpInfo) {
                mCorpInfo = corpInfo;
            }
        });
    }

    private void getStroeTypeInfo() {
        getMvpView().showLoadingMsg("正在加载单据类型信息...");
        mSubscription3 = mDataManager.queryStoreTypeById(mBill.getStoreTypeId()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<StoreTypeInfo>() {
            @Override
            public void onCompleted() {
                getBarCodeInfo();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissLoadingMsg();
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(StoreTypeInfo storeTypeInfo) {
                mInfo = storeTypeInfo;
            }
        });
    }

    private void getBarCodeInfo() {
        getMvpView().showLoadingMsg("正在加载条码类型信息...");
        mSubscription4 = mDataManager.queryBarcodeByBillNumber(mBill.getBillNumber())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Barcode>>() {
            @Override
            public void onCompleted() {
                getMvpView().dissMissLoadingMsg();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissLoadingMsg();
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(ArrayList<Barcode> barcodes) {
                getMvpView().OnDataInit(mBill, barcodes, mCorpInfo, mInfo);
            }
        });
    }


    @Override
    public void doConfirm(final Bill bill, final ArrayList<CheckBoxAdapter.CheckBoxItmeData> mCodeListData) {
        getMvpView().showLoadingMsg("正在提交...");
        mSubscription5 = Observable.create(new Observable.OnSubscribe<ArrayList<Barcode>>() {
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
                return mDataManager.deleteBeforeConfirm(bill, barcodes);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissLoadingMsg();
                getMvpView().showResult("提交失败!" + e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getMvpView().dissMissLoadingMsg();
                if (aBoolean) {
                    getMvpView().showResultAndForward("提交成功！");
                } else {
                    getMvpView().showResult("提交失败!");
                }
            }
        });
//        mSubscription5 = mDataManager.deleteBeforeConfirm(bill, barCodeList).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                getMvpView().dissMissLoadingMsg();
//                getMvpView().showResult("提交失败!" + e.getMessage());
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//                getMvpView().dissMissLoadingMsg();
//                if (aBoolean) {
//                    getMvpView().showResultAndForward("提交成功！");
//                } else {
//                    getMvpView().showResult("提交失败!");
//                }
//            }
//        });
    }

    @Override
    public void doUpload(final Bill bill, final ArrayList<CheckBoxAdapter.CheckBoxItmeData> mCodeListData) {
        getMvpView().showLoadingMsg("正在上传...");
        mSubscription6 = Observable.create(new Observable.OnSubscribe<ArrayList<Barcode>>() {
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
                return mDataManager.deleteBeforeUpload(bill, barcodes);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                getMvpView().dissMissLoadingMsg();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissMissLoadingMsg();
                getMvpView().showResult("上传失败!" + e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getMvpView().dissMissLoadingMsg();
                if (aBoolean) {
                    getMvpView().showResultAndForward("上传成功！");
                } else {
                    getMvpView().showResult("上传失败!");

                }
            }
        });
//        mSubscription6 = mDataManager.deleteBeforeUpload(bill, barCodeList).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                getMvpView().dissMissLoadingMsg();
//                getMvpView().showResult("上传失败!" + e.getMessage());
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//                getMvpView().dissMissLoadingMsg();
//                if (aBoolean) {
//                    getMvpView().showResultAndForward("上传成功！");
//                } else {
//                    getMvpView().showResult("上传失败!");
//
//                }
//            }
//        });
    }

    @Override
    public void checkBarcodeInDB(final String code, String storetypeid) {
        mSubscription7 = mDataManager.checkBarCode(code, storetypeid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
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

        mSubscription8 = Observable.interval(0, 1, TimeUnit.SECONDS).filter(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                return --time <= 0;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread()).take(1).subscribe(new Subscriber<Long>() {
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

    @Override
    public void recovryBill(String billNumber) {
        mSubscription9 = mDataManager.modifyBillState(billNumber, "0")
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getMvpView().finishUI();
                        }
                    }
                });
    }

}
