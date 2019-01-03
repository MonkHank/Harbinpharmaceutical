package com.seuic.hayao.presenter.impl;

import android.text.TextUtils;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.presenter.StockInPreSetPresenter2;
import com.seuic.hayao.presenter.StockOutPreSetPresenter2;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.StockInPreSetView2;
import com.seuic.hayao.view.StockOutPreSetView2;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class StockOutPreSetPresenterImpl2 extends BasePresenter<StockOutPreSetView2> implements StockOutPreSetPresenter2 {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mSubscription2;
    private Subscription mSubscription3;

    private ArrayList<SmartCorpInfo> mCorps;

    public StockOutPreSetPresenterImpl2(StockOutPreSetView2 view) {
        mDataManager = DataManager.getInstance();
        this.attachView(view);
    }

    @Override
    public void attachView(StockOutPreSetView2 view) {
        this.mMvpView = view;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mSubscription2 != null) mSubscription2.unsubscribe();
        if (mSubscription3 != null) mSubscription3.unsubscribe();
    }

    @Override
    public void queryCompany(final String condition) {
        getMvpView().showLoadingBar();
        final ArrayList<SmartCorpInfo> ContactCompanys = new ArrayList<SmartCorpInfo>();
        mSubscription = Observable.just(condition).debounce(150, TimeUnit.MILLISECONDS).switchMap(new Func1<String, Observable<SmartCorpInfo>>() {
            @Override
            public Observable<SmartCorpInfo> call(final String conditions) {
                return Observable.from(mCorps).filter(new Func1<SmartCorpInfo, Boolean>() {
                    @Override
                    public Boolean call(SmartCorpInfo corpInfo) {
                        if (TextUtils.isEmpty(conditions)) {//没有限制全部返还
                            return true;
                        }

                        if (corpInfo.getCorpPinyin().contains(conditions.toUpperCase())) {//拼音
                            return true;
                        }

                        if ((corpInfo.getCorpCode() + "").contains(conditions.toUpperCase())) {//企业编码
                            return true;
                        }

                        if (corpInfo.getCorpName().contains(conditions)) {//企业名称
                            return true;
                        }
                        return false;
                    }
                });
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SmartCorpInfo>() {
            @Override
            public void onCompleted() {
                getMvpView().dismissLoadingBar();
                getMvpView().queryCompanyList(ContactCompanys);
                getMvpView().updateList();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dismissLoadingBar();
                getMvpView().showErrorMsg(e.getMessage());
            }

            @Override
            public void onNext(SmartCorpInfo corpInfo) {
                ContactCompanys.add(corpInfo);
            }
        });
    }

    @Override
    public void loadAll(final TextView view, boolean isIn) {
//        getMvpView().showProgressDialog("正在加载往来企业...");
        getMvpView().showLoadingBar();
        mSubscription2 = mDataManager.queryUserCommonCorps(isIn)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<SmartCorpInfo>>() {
                    @Override
                    public void onCompleted() {
//                        getMvpView().dissMissProgressDialog();
//                        getMvpView().queryCompanyList(mCorps);
                        bindQueryTextView(view);
                        getMvpView().dismissLoadingBar();
//                        getMvpView().updateList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dissMissProgressDialog();
                        getMvpView().showErrorMsg(e.getMessage());

                    }

                    @Override
                    public void onNext(ArrayList<SmartCorpInfo> info) {
                        mCorps = info;
                    }
                });
    }

    public void bindQueryTextView(TextView view) {
        mSubscription3 = RxTextView.textChanges(view).subscribeOn(AndroidSchedulers.mainThread()).debounce(600, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).map(new Func1<CharSequence, ArrayList<SmartCorpInfo>>() {
            @Override
            public ArrayList<SmartCorpInfo> call(final CharSequence charSequence) {
                ArrayList<SmartCorpInfo> infos = new ArrayList<SmartCorpInfo>();
                String condition = charSequence.toString();
                for (SmartCorpInfo corpInfo : mCorps) {
                    if (TextUtils.isEmpty(condition)) {//没有限制全部返还
                        infos.add(corpInfo);
                        continue;
                    }

                    if (corpInfo.getCorpPinyin().contains(condition.toUpperCase())) {//拼音
                        infos.add(corpInfo);
                        continue;
                    }

                    if ((corpInfo.getCorpCode() + "").contains(condition.toUpperCase())) {//企业编码
                        infos.add(corpInfo);
                        continue;
                    }

                    if (corpInfo.getCorpName().contains(condition)) {//企业名称
                        infos.add(corpInfo);
                        continue;
                    }

                }
                return infos;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<SmartCorpInfo>>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().dismissLoadingBar();
                        getMvpView().updateList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dismissLoadingBar();
                        getMvpView().showErrorMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<SmartCorpInfo> corpInfo) {
//                        getMvpView().dismissLoadingBar();
                        getMvpView().queryCompanyList(corpInfo);
                        getMvpView().updateList();
                    }
                });
    }
}
