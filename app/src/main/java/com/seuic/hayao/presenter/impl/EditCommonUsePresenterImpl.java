package com.seuic.hayao.presenter.impl;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.UserCorpToCorp;
import com.seuic.hayao.modelbean.EditCommonUseShow;
import com.seuic.hayao.presenter.EditCommonUsePresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.EditCommonUseView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class EditCommonUsePresenterImpl extends BasePresenter<EditCommonUseView> implements EditCommonUsePresenter {

    private final DataManager mDataManager;
    private Subscription mSubscription1;
    private Subscription mSubscription2;
    private Subscription mSubscription3;
    private Subscription mSubscription4;
    private Subscription mSubscription5;

    private ArrayList<EditCommonUseShow> allShowData;
    private boolean isInType;

    public EditCommonUsePresenterImpl(EditCommonUseView view) {
        mDataManager = DataManager.getInstance();
        this.attachView(view);
    }

    @Override
    public void attachView(EditCommonUseView view) {
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
    }

    @Override
    public void queryCompany(final String condition) {
        getMvpView().showProgress();
        final ArrayList<EditCommonUseShow> data = new ArrayList<EditCommonUseShow>();
        mSubscription1 = Observable.from(allShowData).filter(new Func1<EditCommonUseShow, Boolean>() {
            @Override
            public Boolean call(EditCommonUseShow editCommonUseShow) {
                if (TextUtils.isEmpty(condition)) {//没有限制全部返还
                    return true;
                }

                if (editCommonUseShow.getCorpInfo().getCorpPinyin().contains(condition.toUpperCase())) {//拼音
                    return true;
                }

                if ((editCommonUseShow.getCorpInfo().getCorpCode() + "").contains(condition.toUpperCase())) {//企业编码
                    return true;
                }

                if (editCommonUseShow.getCorpInfo().getCorpName().contains(condition)) {//企业名称
                    return true;
                }
                return false;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<EditCommonUseShow>() {
            @Override
            public void onCompleted() {
                getMvpView().dissmissProgress();
                getMvpView().updateCorpList(data);
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showErrorInfo(e.getMessage());
            }

            @Override
            public void onNext(EditCommonUseShow editCommonUseShow) {
                data.add(editCommonUseShow);
            }
        });
    }

    @Override
    public void save() {
        getMvpView().showProgressDialog("正在保存设置...");
        mSubscription2 = Observable.just(allShowData).flatMap(new Func1<ArrayList<EditCommonUseShow>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(ArrayList<EditCommonUseShow> editCommonUseShows) {
                ArrayList<EditCommonUseShow> modify = new ArrayList<EditCommonUseShow>();
                for (EditCommonUseShow show : editCommonUseShows) {
                    if (!show.getOriginState().equals(show.getCheckStatInfo().getCommonUse())) {
                        modify.add(show);
                    }
                }
                return mDataManager.saveCommonUse(modify);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                getMvpView().dissmissProgressDialog();
                getMvpView().showMsg("保存成功！");
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dissmissProgressDialog();
                e.printStackTrace();
                getMvpView().showErrorInfo(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
            }
        });

//        mSubscription2 = mDataManager.saveCommonUse(allShowData).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
//            @Override
//            public void onCompleted() {
//                getMvpView().dissmissProgressDialog();
//                getMvpView().showMsg("保存成功！");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                getMvpView().showErrorInfo(e.getMessage());
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//            }
//        });
    }

    @Override
    public void loadAll(final EditText view) {
        getMvpView().showProgressDialog("正在加载往来企业...");
        mSubscription3 = mDataManager.queryCorps().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ArrayList<EditCommonUseShow>>() {
            @Override
            public void onCompleted() {
                getMvpView().dissmissProgressDialog();
                getMvpView().updateCorpList(allShowData);
                updataCorpNumber();
                bindQueryTextView(view);
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showErrorInfo(e.getMessage());
            }

            @Override
            public void onNext(ArrayList<EditCommonUseShow> list) {
                allShowData = list;
            }
        });
    }

    @Override
    public void updataCorpNumber() {
        int i = 0;
        for (EditCommonUseShow show : allShowData) {
            char[] ss = show.getCheckStatInfo().getCommonUse().toCharArray();
            String temp = ss[isInType ? 0 : 1] + "";
            if (temp.equals("1")) {
                i++;
            }
        }
        getMvpView().updataCorpNumber(i + "/" + allShowData.size());
    }

    @Override
    public void showSelected(final boolean isSelected) {
        getMvpView().showProgress();
        final ArrayList<EditCommonUseShow> data = new ArrayList<EditCommonUseShow>();
        mSubscription4 = Observable.from(allShowData).filter(new Func1<EditCommonUseShow, Boolean>() {
            @Override
            public Boolean call(EditCommonUseShow editCommonUseShow) {
                char[] ss = editCommonUseShow.getCheckStatInfo().getCommonUse().toCharArray();
                String temp = ss[isInType ? 0 : 1] + "";
                return temp.equals(isSelected ? "1" : "0");
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<EditCommonUseShow>() {
            @Override
            public void onCompleted() {
                getMvpView().dissmissProgress();
                getMvpView().updateCorpList(data);
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showErrorInfo(e.getMessage());
            }

            @Override
            public void onNext(EditCommonUseShow editCommonUseShow) {
                data.add(editCommonUseShow);
            }
        });
    }

    @Override
    public void setType(boolean isInType) {
        this.isInType = isInType;
    }

    public void bindQueryTextView(TextView view) {
        mSubscription5 = RxTextView.textChanges(view).subscribeOn(AndroidSchedulers.mainThread()).debounce(600, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).map(new Func1<CharSequence, ArrayList<EditCommonUseShow>>() {
            @Override
            public ArrayList<EditCommonUseShow> call(final CharSequence charSequence) {
                ArrayList<EditCommonUseShow> infos = new ArrayList<EditCommonUseShow>();
                String condition = charSequence.toString();
                for (EditCommonUseShow corpInfo : allShowData) {
                    if (TextUtils.isEmpty(condition)) {//没有限制全部返还
                        infos.add(corpInfo);
                        continue;
                    }

                    if (corpInfo.getCorpInfo().getCorpPinyin().contains(condition.toUpperCase())) {//拼音
                        infos.add(corpInfo);
                        continue;
                    }

                    if ((corpInfo.getCorpInfo().getCorpCode() + "").contains(condition.toUpperCase())) {//企业编码
                        infos.add(corpInfo);
                        continue;
                    }

                    if (corpInfo.getCorpInfo().getCorpName().contains(condition)) {//企业名称
                        infos.add(corpInfo);
                        continue;
                    }

                }
                return infos;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<EditCommonUseShow>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorInfo(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<EditCommonUseShow> corpInfo) {
                        getMvpView().updateCorpList(corpInfo);
                    }
                });
    }


}
