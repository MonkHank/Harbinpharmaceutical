package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.presenter.MainPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.MainView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainPresenterImpl extends BasePresenter<MainView> implements MainPresenter {

    private DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mSubscription1;

    public MainPresenterImpl(MainView view) {
        super(view);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mSubscription1 != null) mSubscription1.unsubscribe();
    }

    @Override
    public void updataNumber() {
        mSubscription = mDataManager.queryBillNumber().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (getMvpView() != null) {
                    getMvpView().showErrorMsg("加载未上传单号出错！");
                }
            }

            @Override
            public void onNext(Integer integer) {
                if (getMvpView() != null) {
                    getMvpView().updataNumber(integer);
                }
            }
        });
    }

    @Override
    public void checkCorpNumber() {
        mSubscription1 = mDataManager.checkCorpNumber()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView() != null) {
                            getMvpView().showErrorMsg(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            if (getMvpView() != null) {
                                getMvpView().showCorpNumberUpdate();
                            }
                        }
                    }
                });
    }
}
