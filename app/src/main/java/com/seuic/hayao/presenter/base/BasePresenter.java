package com.seuic.hayao.presenter.base;

import com.seuic.hayao.view.base.IBaseView;

public class BasePresenter<T extends IBaseView> {
    protected T mMvpView;

    protected boolean isViewAttached() {
        return mMvpView != null;
    }

    public BasePresenter(T mMvpView) {
        this.mMvpView = mMvpView;
    }

    protected T getMvpView() {
        return mMvpView;
    }

    protected void detachView() {
        mMvpView = null;
    }

    protected void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    protected static class MvpViewNotAttachedException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before"
                    + " requesting data to the Presenter");
        }
    }
}
