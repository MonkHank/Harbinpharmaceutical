package com.seuic.hayao;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.seuic.hayao.data.bean.SmartStoreCodeInfo;
import com.seuic.hayao.data.bean.SmartStoreInfo;
import com.seuic.hayao.data.bean.SmartUploadResult;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.data.local.DatabaseHelper;
import com.seuic.hayao.data.remote.ServiceCallAction;
import com.seuic.hayao.enums.StoreKind;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest  {
    @Test
    public void test() throws Exception {
        ServiceCallAction action = ServiceCallAction.getInstance();
//        action.GetBizCorpCount(21);
        action.GetProfileInfo("cc", "1").getCorpId();
        ArrayList<StoreTypeInfo> infos = action.GetStoreTypes();

        for (int i = 0; i < infos.size(); i++) {
            DatabaseHelper.getInstance().insertStoreTypeInfo(infos.get(i)).subscribe();
        }

//        int count = action.GetBizCorpCount(action.GetProfileInfo("c1", "1").getCorpId());

    }
    @Test
    public void test1() {
        ServiceCallAction action = ServiceCallAction.getInstance();
        try {
            action.GetCurrentTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2() throws Exception {
        ServiceCallAction action = ServiceCallAction.getInstance();
        action.initHeader("c1", "1");
        action.GetBizCorpsPage(166100, 0, 1);
    }

    @Test
    public void testsss() {

        Observable.interval(1, TimeUnit.SECONDS).buffer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Long>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Long> longs) {
                Log.d("feng", longs + "");
            }
        });

//        Observable.just("Marry me!").retryWhen(new Func1<Observable<? extends Throwable>, Observable<Long>>() {
//            @Override
//            public Observable<Long> call(Observable<? extends Throwable> throwableObservable) {
//                return throwableObservable.zipWith(Observable.range(1, Integer.MAX_VALUE), new Func2<Throwable, Integer, Integer>() {
//                    @Override
//                    public Integer call(Throwable throwable, Integer i) {
//                        return i;
//                    }
//                }).concatMap(new Func1<Integer, Observable<? extends Long>>() {
//                    @Override
//                    public Observable<? extends Long> call(Integer retryCount) {
//                        return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS);
//                    }
//                });
//            }
//        }).subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                /*Never completed*/
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                /*No failure*/
//            }
//
//            @Override
//            public void onNext(String s) {
//                /*Endless  love*/
//            }
//        });
    }

    @Test
    public void testUpload() throws Exception {

        ServiceCallAction action = ServiceCallAction.getInstance();
        int id = action.GetProfileInfo("c2", "1").getCorpId();

        /*
         *企业Id ，企业编码，单据ID，单据号，单据时间，单据类型，单据类型文本，指派人，往来企业，往来企业编码，往来企业名称 填写
         *接受企业，接受企业编码，接受企业名称  ，单据明细，可以空着
         */
        SmartStoreInfo info = new SmartStoreInfo();
        info.setCorpId(id);
        info.setCorpCode("C2");
        info.setStoreId(UUID.randomUUID().toString());
        info.setStoreNo("20160419152134");
        info.setStoreDate(new Date());
        info.setStoreType(5);
        info.setStoreTypeText("销售出库");
        info.setStoreMan("c2");
        info.setBizCorpCode("C3");
        info.setBizCorpId(166102);
        info.setBizCorpName("测试专营3");
        info.setStoreKind(StoreKind.Out);

        ArrayList<SmartStoreCodeInfo> codes = new ArrayList<SmartStoreCodeInfo>();

        SmartStoreCodeInfo code = new SmartStoreCodeInfo();
        code.setActor("c2");
        code.setActTime(new Date());
        code.setByParent(false);
        code.setCodeId("81067290055741230253");
        code.setIsParentCode(false);
//        code.setIsParentCode(false);
//        code.setParentLevel();
//        code.setSavedCount();
//        code.setSavedCodeId();
//        code.setSavedCodeLevel();
        codes.add(code);

        SmartUploadResult ss = action.UploadStore(1047, info, codes, null);
        ss.getMessage();

    }

    @Test
    public void test11() {
    }

}