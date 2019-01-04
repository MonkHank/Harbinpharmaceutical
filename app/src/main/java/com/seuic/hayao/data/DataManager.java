package com.seuic.hayao.data;

import android.text.TextUtils;

import com.seuic.hayao.HYApplication;
import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.SmartProfileInfo;
import com.seuic.hayao.data.bean.SmartStoreCodeInfo;
import com.seuic.hayao.data.bean.SmartStoreInfo;
import com.seuic.hayao.data.bean.SmartUploadResult;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.data.bean.UserCorpToCorp;
import com.seuic.hayao.data.local.AppCache;
import com.seuic.hayao.data.local.DatabaseHelper;
import com.seuic.hayao.data.local.PreferencesHelper;
import com.seuic.hayao.data.remote.ServiceCallAction;
import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.event.EventPosterHelper;
import com.seuic.hayao.event.data.ScanDataEvent;
import com.seuic.hayao.modelbean.BillManagerShow;
import com.seuic.hayao.modelbean.EditCommonUseShow;
import com.seuic.hayao.modelbean.LoginInfo;
import com.seuic.hayao.util.AndroidInfoGetter;
import com.seuic.hayao.util.XmlGenerater;
import com.seuic.update.DownloadXMLParser;
import com.seuic.update.UpdateInfo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

public class DataManager {

    private static DataManager manager;
    private final DatabaseHelper mDataBaseHelper;
    private final EventPosterHelper mEventPoster;
    private final PreferencesHelper mPreferencesHelper;
    private final ServiceCallAction mService;
    private final AppCache mCache;

    private DataManager() {
        mDataBaseHelper = DatabaseHelper.getInstance();
        mEventPoster = EventPosterHelper.getInstance();
        mPreferencesHelper = new PreferencesHelper(HYApplication.getApplication());
        mService = ServiceCallAction.getInstance();
        mCache = AppCache.getInstance();
    }

    public static DataManager getInstance() {
        if (manager == null) {
            synchronized (DataManager.class) {
                if (manager == null) {
                    manager = new DataManager();
                }
            }
        }
        return manager;
    }

    public Observable<ArrayList<SmartCorpInfo>> queryUserCommonCorps(final boolean isIn) {
        return mDataBaseHelper.getUserCorpInfos(mCache.getLoginInfo().getCorpId() + "", isIn).subscribeOn(Schedulers.newThread());
    }

    public Observable<SmartCorpInfo> queryContactCompanyByCondition(final String condition) {

        return mDataBaseHelper.getUserCorpToCorpListByUserCorpId(mCache.getLoginInfo().getCorpId() + "").flatMap(new Func1<ArrayList<UserCorpToCorp>, Observable<UserCorpToCorp>>() {
            @Override
            public Observable<UserCorpToCorp> call(ArrayList<UserCorpToCorp> userCorpToCorps) {
                return Observable.from(userCorpToCorps);
            }
        }).filter(new Func1<UserCorpToCorp, Boolean>() {
            @Override
            public Boolean call(UserCorpToCorp userCorpToCorp) {
                return userCorpToCorp.getCommonUse().equals("1");
            }
        }).flatMap(new Func1<UserCorpToCorp, Observable<SmartCorpInfo>>() {
            @Override
            public Observable<SmartCorpInfo> call(final UserCorpToCorp userCorpToCorp) {
                return mDataBaseHelper.getCorpInfoById(userCorpToCorp.getCorpId());
            }
        }).filter(new Func1<SmartCorpInfo, Boolean>() {
            @Override
            public Boolean call(SmartCorpInfo corpInfo) {

                if (TextUtils.isEmpty(condition)) {//没有限制全部返还
                    return true;
                }

                if (corpInfo.getCorpPinyin().contains(condition.toUpperCase())) {//拼音
                    return true;
                }

                if ((corpInfo.getCorpCode() + "").contains(condition.toUpperCase())) {//企业编码
                    return true;
                }

                if (corpInfo.getCorpName().contains(condition)) {//企业名称
                    return true;
                }
                return false;
            }
        }).subscribeOn(Schedulers.newThread());

    }

    public Observable<ArrayList<EditCommonUseShow>> queryCorps() {

        return mDataBaseHelper.getUserCorpAndCommonUse(mCache.getLoginInfo().getCorpId() + "").subscribeOn(Schedulers.newThread());
//        return mDataBaseHelper.getUserCorpToCorpListByUserCorpId(mCache.getLoginInfo().getCorpId() + "").flatMap(new Func1<ArrayList<UserCorpToCorp>, Observable<UserCorpToCorp>>() {
//            @Override
//            public Observable<UserCorpToCorp> call(ArrayList<UserCorpToCorp> userCorpToCorps) {
//                return Observable.from(userCorpToCorps);
//            }
//        }).flatMap(new Func1<UserCorpToCorp, Observable<EditCommonUseShow>>() {
//            @Override
//            public Observable<EditCommonUseShow> call(final UserCorpToCorp userCorpToCorp) {
//                return mDataBaseHelper.getCorpInfoById(userCorpToCorp.getCorpId()).map(new Func1<SmartCorpInfo, EditCommonUseShow>() {
//                    @Override
//                    public EditCommonUseShow call(SmartCorpInfo corpInfo) {
//                        EditCommonUseShow show = new EditCommonUseShow();
//                        show.setCheckStatInfo(userCorpToCorp);
//                        show.setCorpInfo(corpInfo);
//                        return show;
//                    }
//                });
//            }
//        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<SmartCorpInfo> queryContactCorpById(final String id) {
        return mDataBaseHelper.getCorpInfoById(id).subscribeOn(Schedulers.newThread());
    }

    public Observable<Integer> syncContactCompany() {

        return mDataBaseHelper.setUpdateFlag(mCache.getLoginInfo().getCorpId() + "", "0").concatMap(new Func1<Boolean, Observable<? extends Integer>>() {
            @Override
            public Observable<? extends Integer> call(Boolean aBoolean) {
                return Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        try {
                            int corpCount = mService.GetBizCorpCount(mCache.getLoginInfo().getCorpId());
                            subscriber.onNext(corpCount);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(new RuntimeException("网络连接失败，请稍后再试！"));
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<Integer, Observable<ArrayList<SmartCorpInfo>>>() {
            @Override
            public Observable<ArrayList<SmartCorpInfo>> call(final Integer count) {
                return Observable.create(new Observable.OnSubscribe<ArrayList<SmartCorpInfo>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<SmartCorpInfo>> subscriber) {
                        int pageSize = count;
                        int i = 1;
                        while (pageSize > 500) {
                            pageSize = count / ++i;
                        }
                        try {
                            for (int j = 0; j < (count % pageSize != 0 ? count / pageSize + 1 : count / pageSize); j++) {
                                subscriber.onNext(mService.GetBizCorpsPage(mCache.getLoginInfo().getCorpId(), j, pageSize));
                            }
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(new RuntimeException("网络连接失败，请稍后再试！"));
                        }
                    }
                });
            }
        }).flatMap(new Func1<ArrayList<SmartCorpInfo>, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(final ArrayList<SmartCorpInfo> smartCorpInfos) {
                return mDataBaseHelper.insertDownloadInfo(smartCorpInfos, mCache.getLoginInfo().getCorpId() + "");
            }
        }).map(new Func1<Integer, Integer>() {
            int process = 0;

            @Override
            public Integer call(Integer number) {
                process = process + number;
                return process;
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                mDataBaseHelper.deleteUnUpdateData(mCache.getLoginInfo().getCorpId() + "").subscribeOn(Schedulers.newThread()).subscribe();
            }
        }).finallyDo(new Action0() {
            @Override
            public void call() {
//                mDataBaseHelper.setUpdateFlag(mCache.getLoginInfo().getCorpId() + "", "0").subscribeOn(Schedulers.newThread()).subscribe();
            }
        });

//        return Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                try {
//                    int corpCount = mService.GetBizCorpCount(mCache.getLoginInfo().getCorpId());
//                    subscriber.onNext(corpCount);
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                    e.printStackTrace();
//                }
//            }
//        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<Integer, Observable<ArrayList<SmartCorpInfo>>>() {
//            @Override
//            public Observable<ArrayList<SmartCorpInfo>> call(final Integer count) {
//                return Observable.create(new Observable.OnSubscribe<ArrayList<SmartCorpInfo>>() {
//                    @Override
//                    public void call(Subscriber<? super ArrayList<SmartCorpInfo>> subscriber) {
//                        int pageSize = count;
//                        int i = 1;
//                        while (pageSize > 500) {
//                            pageSize = count / ++i;
//                        }
//                        try {
//                            for (int j = 0; j < (count % pageSize != 0 ? count / pageSize + 1 : count / pageSize); j++) {
//                                subscriber.onNext(mService.GetBizCorpsPage(mCache.getLoginInfo().getCorpId(), j, pageSize));
//                            }
//                            subscriber.onCompleted();
//                        } catch (Exception e) {
//                            subscriber.onError(e);
//                        }
//                    }
//                });
//            }
//        }).flatMap(new Func1<ArrayList<SmartCorpInfo>, Observable<SmartCorpInfo>>() {
//            @Override
//            public Observable<SmartCorpInfo> call(final ArrayList<SmartCorpInfo> smartCorpInfos) {
//                return Observable.from(smartCorpInfos);
//            }
//        }).flatMap(new Func1<SmartCorpInfo, Observable<Boolean>>() {
//            @Override
//            public Observable<Boolean> call(SmartCorpInfo corpInfo) {
//
//                UserCorpToCorp userCorpToCorp = new UserCorpToCorp();
//                userCorpToCorp.setCorpId(corpInfo.getCorpId() + "");
//                userCorpToCorp.setUserCorpId(mCache.getLoginInfo().getCorpId() + "");
//                userCorpToCorp.setCommonUse("1");
//
//                return Observable.zip(mDataBaseHelper.insertCropInfo(corpInfo), mDataBaseHelper.insertUserCorpToCorp(userCorpToCorp), new Func2<Boolean, UserCorpToCorp, Boolean>() {
//                    @Override
//                    public Boolean call(Boolean aBoolean, UserCorpToCorp userCorpToCorp) {
//                        return aBoolean && userCorpToCorp != null;
//                    }
//                });
//            }
//        }).map(new Func1<Boolean, Integer>() {
//            int process = 0;
//
//            @Override
//            public Integer call(Boolean aBoolean) {
//                return aBoolean ? ++process : process;
//            }
//        });
    }

    public Observable<Boolean> login(final LoginInfo info) {
        final boolean isNetwork = AndroidInfoGetter.getNetworkType(HYApplication.getApplication()) > -1;
        Observable<Boolean> loginResult = null;
        if (isNetwork) {
            loginResult = Observable.create(new Observable.OnSubscribe<SmartProfileInfo>() {
                @Override
                public void call(Subscriber<? super SmartProfileInfo> subscriber) {//验证登陆
                    try {
                        SmartProfileInfo profileInfo = mService.GetProfileInfo(info.getAccount(), info.getPassword());
                        subscriber.onNext(profileInfo);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        e.printStackTrace();
                        subscriber.onError(new RuntimeException(e.getMessage().contains("用户名或密码") ? "用户名密码不正确" : "网络错误", e));
                    }
                }
                // 指定 subscribe()(Observable.OnSubscribe被激活时的线程) 发生在io线程
            }).subscribeOn(Schedulers.newThread())
                    .flatMap(new Func1<SmartProfileInfo, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(SmartProfileInfo smartProfileInfo) {
                            //插入数据库保存登陆信息
                            if (smartProfileInfo != null) {
                                mCache.setLoginInfo(smartProfileInfo);
                                mPreferencesHelper.setLatestAccount(smartProfileInfo.getUserName());
                                mPreferencesHelper.setLatestPassword(smartProfileInfo.getPassword());
                                mService.initHeader(smartProfileInfo.getUserName(), smartProfileInfo.getPassword());
                            }
                            return mDataBaseHelper.insertProfileInfo(smartProfileInfo);
                        }
                    }).flatMap(new Func1<Boolean, Observable<ArrayList<StoreTypeInfo>>>() {
                        @Override
                        public Observable<ArrayList<StoreTypeInfo>> call(final Boolean aBoolean) {
                            return Observable.create(new Observable.OnSubscribe<ArrayList<StoreTypeInfo>>() {
                                @Override
                                public void call(Subscriber<? super ArrayList<StoreTypeInfo>> subscriber) {
                                    //登陆成功后下载信息
                                    if (aBoolean) {
                                        try {
                                            subscriber.onNext(mService.GetStoreTypes());
                                            subscriber.onCompleted();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            subscriber.onError(new RuntimeException("网络繁忙，请稍后再试"));
                                        }
                                    } else {
                                        subscriber.onError(new RuntimeException("登陆失败！"));
                                    }
                                }
                            });
                        }
                    }).flatMap(new Func1<ArrayList<StoreTypeInfo>, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(ArrayList<StoreTypeInfo> storeTypeInfos) {
                            return mDataBaseHelper.insertStoreTypeInfo(storeTypeInfos);
                        }
                    });
        } else {
            loginResult = mDataBaseHelper.getProfileInfoByName(info.getAccount()).subscribeOn(Schedulers.newThread()).flatMap(new Func1<SmartProfileInfo, Observable<Boolean>>() {
                @Override
                public Observable<Boolean> call(final SmartProfileInfo smartProfileInfo) {
                    return Observable.create(new Observable.OnSubscribe<Boolean>() {
                        @Override
                        public void call(Subscriber<? super Boolean> subscriber) {
                            if (smartProfileInfo == null) {
                                subscriber.onError(new RuntimeException("未找到" + info.getAccount() + "用户，如果您是首次使用该用户登陆请首先联网校验！"));
                            } else if (smartProfileInfo.getPassword().equals(info.getPassword())) {
                                //设置缓存
                                mCache.setLoginInfo(smartProfileInfo);
                                //保留最后登陆用户名
                                mPreferencesHelper.setLatestAccount(smartProfileInfo.getUserName());
                                //保留最后登陆密码
                                mPreferencesHelper.setLatestPassword(smartProfileInfo.getPassword());
                                //初始化webservice请求校验头
                                mService.initHeader(smartProfileInfo.getUserName(), smartProfileInfo.getPassword());
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            } else {
                                subscriber.onNext(false);
                                subscriber.onCompleted();
                            }
                        }
                    });
                }
            });
        }
        return loginResult;
    }

    public Observable<ArrayList<StoreTypeInfo>> queryStoreTypeByKind(final StoreKind kind) {
        return mDataBaseHelper.getStoreTypeInfoByKind(kind).subscribeOn(Schedulers.newThread());
    }

    public Observable<ArrayList<StoreTypeInfo>> queryStoreType() {
        return mDataBaseHelper.getStoreTypeInfoList().subscribeOn(Schedulers.newThread());
    }

    public Observable<StoreTypeInfo> queryStoreTypeById(final String id) {
        return mDataBaseHelper.getStoreTypeInfoById(id).subscribeOn(Schedulers.newThread());
    }

    public Observable<Integer> queryBillNumber() {
        return mDataBaseHelper.getUnUploadBillListByCreator(mCache.getLoginInfo().getUserId() + "").map(new Func1<ArrayList<Bill>, Integer>() {
            @Override
            public Integer call(ArrayList<Bill> bills) {
                return bills.size();
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<Boolean> uploadBills() {
        return Observable.interval(1, 20, TimeUnit.MINUTES).subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Long, Observable<ArrayList<Bill>>>() {
                    @Override
                    public Observable<ArrayList<Bill>> call(Long aLong) {
                        return mDataBaseHelper.getUnUploadBillListByCreator(mCache.getLoginInfo().getUserId() + "");
                    }
                }).flatMap(new Func1<ArrayList<Bill>, Observable<Bill>>() {
                    @Override
                    public Observable<Bill> call(ArrayList<Bill> bills) {
                        return Observable.from(bills);
                    }
                }).flatMap(new Func1<Bill, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final Bill bill) {
                        return Observable.zip(mDataBaseHelper.getBarcodeListByBillNumber(bill.getBillNumber()),
                                mDataBaseHelper.getStoreTypeInfoById(bill.getStoreTypeId()),
                                mDataBaseHelper.getCorpInfoById(bill.getContactCorpId()),
                                new Func3<ArrayList<Barcode>, StoreTypeInfo, SmartCorpInfo, SmartUploadResult>() {
                                    @Override
                                    public SmartUploadResult call(ArrayList<Barcode> barcodes, StoreTypeInfo storeTypeInfo, SmartCorpInfo corpInfo) {
                                        SmartStoreInfo info = new SmartStoreInfo();
                                        info.setCorpId(mCache.getLoginInfo().getCorpId());
                                        info.setCorpCode(mCache.getLoginInfo().getCorpCode());
                                        info.setStoreId(UUID.randomUUID().toString());
                                        info.setStoreNo(bill.getBillNumber());
                                        try {
                                            info.setStoreDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bill.getGenerateTime()));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        info.setStoreType(storeTypeInfo.getStoreType());
                                        info.setStoreTypeText(storeTypeInfo.getStoreTypeText());
                                        info.setStoreMan(mCache.getLoginInfo().getUserName());
                                        info.setBizCorpCode(corpInfo.getCorpCode());
                                        info.setBizCorpId(corpInfo.getCorpId());
                                        info.setBizCorpName(corpInfo.getCorpName());
                                        info.setStoreKind(storeTypeInfo.getStoreKind());

                                        ArrayList<SmartStoreCodeInfo> codes = new ArrayList<SmartStoreCodeInfo>();
                                        for (Barcode code : barcodes) {
                                            SmartStoreCodeInfo codeUpload = new SmartStoreCodeInfo();
                                            codeUpload.setActor(mCache.getLoginInfo().getUserName());
                                            try {
                                                codeUpload.setActTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(code.getActDate()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            codeUpload.setCodeId(code.getBarcode());
                                            codeUpload.setByParent(false);
                                            codeUpload.setIsParentCode(false);
                                            codes.add(codeUpload);
                                        }
                                        SmartUploadResult result = null;
                                        try {
                                            result = mService.UploadStore(mCache.getLoginInfo().getUserId(), info, codes, null);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return result;
                                    }
                                }).flatMap(new Func1<SmartUploadResult, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(SmartUploadResult smartUploadResult) {
                                if (smartUploadResult != null && smartUploadResult.isSucceed() || smartUploadResult.getMessage().contains("单据已完成")) {
                                    bill.setIsUpload("1");
                                    return mDataBaseHelper.insertBill(bill);
                                }
                                return Observable.create(new Observable.OnSubscribe<Boolean>() {
                                    @Override
                                    public void call(Subscriber<? super Boolean> subscriber) {
                                        subscriber.onNext(false);
                                        subscriber.onCompleted();
                                    }
                                });
                            }
                        });
                    }
                });
    }

    public Observable<Boolean> doConfirm(final Bill bill, final ArrayList<Barcode> barCodeList) {
        return mDataBaseHelper.insertBill(bill).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                return mDataBaseHelper.insertBarcodeList(barCodeList);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<Boolean> deleteBeforeConfirm(final Bill bill, final ArrayList<Barcode> barCodeList) {

        return Observable.zip(mDataBaseHelper.deleteBillByNumber(bill.getBillNumber()), mDataBaseHelper.deleteBarcodeByNumber(bill.getBillNumber()), new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                return aBoolean && aBoolean2;
            }
        }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                return mDataBaseHelper.insertBill(bill).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        if (!aBoolean) throw new RuntimeException("处理订单失败");
                        return mDataBaseHelper.insertBarcodeList(barCodeList);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<Boolean> deleteBeforeUpload(final Bill bill, final ArrayList<Barcode> barCodeList) {

        return Observable.zip(mDataBaseHelper.deleteBillByNumber(bill.getBillNumber()), mDataBaseHelper.deleteBarcodeByNumber(bill.getBillNumber()), new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                return aBoolean && aBoolean2;
            }
        }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (aBoolean) {
                    bill.setIsUpload("0");
                }
                return mDataBaseHelper.insertBill(bill);
            }
        }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (!aBoolean) throw new RuntimeException("订单插入失败！");
                return mDataBaseHelper.insertBarcodeList(barCodeList);
            }
        }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                return Observable.zip(mDataBaseHelper.getStoreTypeInfoById(bill.getStoreTypeId()), mDataBaseHelper.getCorpInfoById(bill.getContactCorpId()), new Func2<StoreTypeInfo, SmartCorpInfo, SmartUploadResult>() {
                    @Override
                    public SmartUploadResult call(StoreTypeInfo storeTypeInfo, SmartCorpInfo corpInfo) {

                        SmartStoreInfo info = new SmartStoreInfo();
                        info.setCorpId(mCache.getLoginInfo().getCorpId());
                        info.setCorpCode(mCache.getLoginInfo().getCorpCode());
                        info.setStoreId(UUID.randomUUID().toString());
                        info.setStoreNo(bill.getBillNumber());
                        try {
                            info.setStoreDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bill.getGenerateTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        info.setStoreType(storeTypeInfo.getStoreType());
                        info.setStoreTypeText(storeTypeInfo.getStoreTypeText());
                        info.setStoreMan(mCache.getLoginInfo().getUserName());
                        info.setBizCorpCode(corpInfo.getCorpCode());
                        info.setBizCorpId(corpInfo.getCorpId());
                        info.setBizCorpName(corpInfo.getCorpName());
                        info.setStoreKind(storeTypeInfo.getStoreKind());

                        ArrayList<SmartStoreCodeInfo> codes = new ArrayList<SmartStoreCodeInfo>();
                        for (Barcode code : barCodeList) {
                            SmartStoreCodeInfo codeUpload = new SmartStoreCodeInfo();
                            codeUpload.setActor(mCache.getLoginInfo().getUserName());
                            try {
                                codeUpload.setActTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(code.getActDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            codeUpload.setCodeId(code.getBarcode());
                            codes.add(codeUpload);
                        }
                        SmartUploadResult result = null;
                        try {
                            result = mService.UploadStore(mCache.getLoginInfo().getUserId(), info, codes, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException("网络传输错误");
                        }
                        return result;
                    }
                }).flatMap(new Func1<SmartUploadResult, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final SmartUploadResult smartUploadResult) {
                        if (smartUploadResult.isSucceed() || smartUploadResult.getMessage().contains("单据已完成")) {
                            bill.setIsUpload("1");
                        } else {
                            throw new RuntimeException("上传失败" + smartUploadResult.getMessage());
                        }
                        return mDataBaseHelper.insertBill(bill);
                    }
                }).subscribeOn(Schedulers.newThread());
            }
        });
    }

    public Observable<Boolean> uploadBill(final Bill bill, final ArrayList<Barcode> barCodeList) {

        return Observable.zip(mDataBaseHelper.getStoreTypeInfoById(bill.getStoreTypeId()), mDataBaseHelper.getCorpInfoById(bill.getContactCorpId()), new Func2<StoreTypeInfo, SmartCorpInfo, SmartUploadResult>() {
            @Override
            public SmartUploadResult call(StoreTypeInfo storeTypeInfo, SmartCorpInfo corpInfo) {

                SmartStoreInfo info = new SmartStoreInfo();
                info.setCorpId(mCache.getLoginInfo().getCorpId());
                info.setCorpCode(mCache.getLoginInfo().getCorpCode());
                info.setStoreId(UUID.randomUUID().toString());
                info.setStoreNo(bill.getBillNumber());
                try {
                    info.setStoreDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bill.getGenerateTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                info.setStoreType(storeTypeInfo.getStoreType());
                info.setStoreTypeText(storeTypeInfo.getStoreTypeText());
                info.setStoreMan(mCache.getLoginInfo().getUserName());
                info.setBizCorpCode(corpInfo.getCorpCode());
                info.setBizCorpId(corpInfo.getCorpId());
                info.setBizCorpName(corpInfo.getCorpName());
                info.setStoreKind(storeTypeInfo.getStoreKind());

                ArrayList<SmartStoreCodeInfo> codes = new ArrayList<SmartStoreCodeInfo>();
                for (Barcode code : barCodeList) {
                    SmartStoreCodeInfo codeUpload = new SmartStoreCodeInfo();
                    codeUpload.setActor(mCache.getLoginInfo().getUserName());
                    try {
                        codeUpload.setActTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(code.getActDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException("网络错误", e);
                    }
                    codeUpload.setCodeId(code.getBarcode());
                    codeUpload.setByParent(false);
                    codeUpload.setIsParentCode(false);
                    codes.add(codeUpload);
                }
                SmartUploadResult result = null;
                try {
                    result = mService.UploadStore(mCache.getLoginInfo().getUserId(), info, codes, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("网络传输错误");
                }
                return result;
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<SmartUploadResult, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(SmartUploadResult smartUploadResult) {
                if (smartUploadResult != null && smartUploadResult.isSucceed() || smartUploadResult.getMessage().contains("单据已完成")) {
                    bill.setIsUpload("1");
                    return mDataBaseHelper.insertBill(bill);
                }
                return Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                    }
                });
            }
        }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (!aBoolean) throw new RuntimeException("订单插入失败！");
                return mDataBaseHelper.insertBarcodeList(barCodeList);
            }
        });
    }

    public void postEvent(Object event) {
        mEventPoster.postEventSafely(event);
    }

    public void onBarCodeReceive(String barcode) {
        ScanDataEvent event = new ScanDataEvent();
        event.setBarCode(barcode);
        mEventPoster.postEventSafely(event);
    }

    public String getLatestAccount() {
        return mPreferencesHelper.getLatestAccount();
    }

    public void saveAddress(String address) {
        mPreferencesHelper.setServerAddress(address);
    }

    public String getAddress() {
        return mPreferencesHelper.getServerAddress();
    }

    public Observable<List<BillManagerShow>> queryAllBills() {
        return mDataBaseHelper.getBillListByUserId(mCache.getLoginInfo().getUserId() + "")
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<ArrayList<Bill>, Observable<Bill>>() {
                    @Override
                    public Observable<Bill> call(ArrayList<Bill> bills) {
                        return Observable.from(bills);
                    }
                }).flatMap(new Func1<Bill, Observable<BillManagerShow>>() {
                    @Override
                    public Observable<BillManagerShow> call(final Bill bill) {
                        return Observable.zip(mDataBaseHelper.getBarcodeNumberByBillNumber(bill.getBillNumber()),
                                mDataBaseHelper.getStoreTypeInfoById(bill.getStoreTypeId()),
                                mDataBaseHelper.getCorpInfoById(bill.getContactCorpId()),
                                new Func3<Integer, StoreTypeInfo, SmartCorpInfo, BillManagerShow>() {
                                    @Override
                                    public BillManagerShow call(Integer barcodes, StoreTypeInfo info, SmartCorpInfo corpInfo) {
                                        BillManagerShow show = new BillManagerShow();
                                        show.setBillNumber(bill.getBillNumber());
                                        show.setBillType(info.getStoreTypeText());
                                        show.setContactCorpName(corpInfo.getCorpName());
                                        show.setCodeNumber(barcodes + "");
                                        show.setIsUpload(bill.getIsUpload().equals("1"));
                                        show.setDate(bill.getGenerateTime().substring(0, 10));
                                        return show;
                                    }
                                });
                    }
                }).toList();
    }

    public Observable<BillManagerShow> queryBillByNumber(final String billNumber) {

        return mDataBaseHelper.getBillList().subscribeOn(Schedulers.newThread()).flatMap(new Func1<ArrayList<Bill>, Observable<Bill>>() {
            @Override
            public Observable<Bill> call(ArrayList<Bill> bills) {
                return Observable.from(bills);
            }
        }).filter(new Func1<Bill, Boolean>() {
            @Override
            public Boolean call(Bill bill) {
                return bill.getBillNumber().equals(billNumber);
            }
        }).filter(new Func1<Bill, Boolean>() {
            @Override
            public Boolean call(Bill bill) {
                return bill.getCreatorId().equals(mCache.getLoginInfo().getUserId() + "");
            }
        }).flatMap(new Func1<Bill, Observable<BillManagerShow>>() {
            @Override
            public Observable<BillManagerShow> call(final Bill bill) {
                return Observable.zip(mDataBaseHelper.getBarcodeListByBillNumber(bill.getBillNumber()),
                        mDataBaseHelper.getStoreTypeInfoById(bill.getStoreTypeId()),
                        mDataBaseHelper.getCorpInfoById(bill.getContactCorpId()),
                        new Func3<ArrayList<Barcode>, StoreTypeInfo, SmartCorpInfo, BillManagerShow>() {
                            @Override
                            public BillManagerShow call(ArrayList<Barcode> barcodes, StoreTypeInfo info, SmartCorpInfo corpInfo) {
                                BillManagerShow show = new BillManagerShow();
                                show.setBillNumber(bill.getBillNumber());
                                show.setBillType(info.getStoreTypeText());
                                show.setContactCorpName(corpInfo.getCorpName());
                                show.setCodeNumber(barcodes.size() + "");
                                show.setDate(bill.getGenerateTime().substring(0, 10));
                                show.setIsUpload(bill.getIsUpload().equals("1"));
                                return show;
                            }
                        });
            }
        });

    }

    public Observable<Bill> queryBill(final String billNumber) {
        return mDataBaseHelper.updateBillToModifyState(billNumber, "3").flatMap(new Func1<Boolean, Observable<Bill>>() {
            @Override
            public Observable<Bill> call(Boolean aBoolean) {
                return mDataBaseHelper.getBillByNumber(billNumber);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<Boolean> modifyBillState(String billNumber, String state) {
        return mDataBaseHelper.updateBillToModifyState(billNumber, state).subscribeOn(Schedulers.newThread());
    }

    public Observable<BillManagerShow> queryBillByTime(final String time) {

        return mDataBaseHelper.getBillList().subscribeOn(Schedulers.newThread()).flatMap(new Func1<ArrayList<Bill>, Observable<Bill>>() {
            @Override
            public Observable<Bill> call(ArrayList<Bill> bills) {
                return Observable.from(bills);
            }
        }).filter(new Func1<Bill, Boolean>() {
            @Override
            public Boolean call(Bill bill) {
                return bill.getCreatorId().equals(mCache.getLoginInfo().getUserId() + "");
            }
        }).filter(new Func1<Bill, Boolean>() {
            @Override
            public Boolean call(Bill bill) {
                return bill.getGenerateTime().contains(time);
            }
        }).flatMap(new Func1<Bill, Observable<BillManagerShow>>() {
            @Override
            public Observable<BillManagerShow> call(final Bill bill) {
                return Observable.zip(mDataBaseHelper.getBarcodeListByBillNumber(bill.getBillNumber()),
                        mDataBaseHelper.getStoreTypeInfoById(bill.getStoreTypeId()),
                        mDataBaseHelper.getCorpInfoById(bill.getContactCorpId()),
                        new Func3<ArrayList<Barcode>, StoreTypeInfo, SmartCorpInfo, BillManagerShow>() {
                            @Override
                            public BillManagerShow call(ArrayList<Barcode> barcodes, StoreTypeInfo info, SmartCorpInfo corpInfo) {
                                BillManagerShow show = new BillManagerShow();
                                show.setBillNumber(bill.getBillNumber());
                                show.setBillType(info.getStoreTypeText());
                                show.setContactCorpName(corpInfo.getCorpName());
                                show.setCodeNumber(barcodes.size() + "");
                                show.setDate(bill.getGenerateTime().substring(0, 10));
                                show.setIsUpload(bill.getIsUpload().equals("1"));
                                return show;
                            }
                        });
            }
        });
    }

    public Observable<Boolean> deleteBillAndBarcodeByNumber(ArrayList<String> billNumbers) {

        return Observable.from(billNumbers).flatMap(new Func1<String, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(String billNumber) {
                return Observable.zip(mDataBaseHelper.deleteBillByNumber(billNumber), mDataBaseHelper.deleteBarcodeByNumber(billNumber), new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        return aBoolean && aBoolean2;
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<Boolean> exportBill(ArrayList<String> billNumbers) {

        return Observable.from(billNumbers).subscribeOn(Schedulers.newThread()).flatMap(new Func1<String, Observable<Bill>>() {
            @Override
            public Observable<Bill> call(String billNumber) {
                return mDataBaseHelper.getBillByNumber(billNumber);
            }
        }).flatMap(new Func1<Bill, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(final Bill bill) {
                return Observable.zip(mDataBaseHelper.getBarcodeListByBillNumber(bill.getBillNumber()), mDataBaseHelper.getStoreTypeInfoById(bill.getStoreTypeId()), mDataBaseHelper.getCorpInfoById(bill.getContactCorpId()), new Func3<ArrayList<Barcode>, StoreTypeInfo, SmartCorpInfo, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<Barcode> barcodes, StoreTypeInfo info, SmartCorpInfo corpInfo) {
                        return XmlGenerater.generateXMLFile(bill, barcodes, info, corpInfo);
                    }
                });
            }
        }).last();

    }

    public String getLatestPassword() {
        return mPreferencesHelper.getLatestPassword();
    }

    public Observable<Boolean> checkCorpNumber() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int corpCount = mService.GetBizCorpCount(mCache.getLoginInfo().getCorpId());
                    subscriber.onNext(corpCount);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new RuntimeException("获取往来企业数量失败！"));
                    e.printStackTrace();
                }
            }
        }).zipWith(mDataBaseHelper.getUserCorpToCorpListByUserCorpId(mCache.getLoginInfo().getCorpId() + ""), new Func2<Integer, ArrayList<UserCorpToCorp>, Boolean>() {
            @Override
            public Boolean call(Integer integer, ArrayList<UserCorpToCorp> userCorpToCorps) {
                return userCorpToCorps.size() != integer;
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<ArrayList<Barcode>> queryBarcodeByBillNumber(String billNumber) {
        return mDataBaseHelper.getBarcodeListByBillNumber(billNumber).subscribeOn(Schedulers.newThread());
    }

    public Observable<Boolean> saveCommonUse(ArrayList<EditCommonUseShow> allShowData) {

        return mDataBaseHelper.updateUserCorpToCorpList(allShowData).subscribeOn(Schedulers.io());
//        return Observable.from(allShowData).subscribeOn(Schedulers.newThread()).map(new Func1<EditCommonUseShow, UserCorpToCorp>() {
//            @Override
//            public UserCorpToCorp call(EditCommonUseShow editCommonUseShow) {
//                return editCommonUseShow.getCheckStatInfo();
//            }
//        }).flatMap(new Func1<UserCorpToCorp, Observable<Boolean>>() {
//            @Override
//            public Observable<Boolean> call(UserCorpToCorp userCorpToCorp) {
//                return mDataBaseHelper.updateUserCorpToCorp(userCorpToCorp);
//            }
//        });
    }

    public Observable<Boolean> checkBarCode(String barcode, String storetypeid) {
        return mDataBaseHelper.isBarcodeExist(barcode, storetypeid).subscribeOn(Schedulers.newThread());
    }

    public Observable<Bill> deleteOverTimeBill() {
        return mDataBaseHelper.getBillList().subscribeOn(Schedulers.newThread()).flatMap(new Func1<ArrayList<Bill>, Observable<Bill>>() {
            @Override
            public Observable<Bill> call(ArrayList<Bill> bills) {
                return Observable.from(bills);
            }
        }).filter(new Func1<Bill, Boolean>() {
            @Override
            public Boolean call(Bill bill) {
                boolean isUpload = bill.getIsUpload().equals("1");
                boolean isOverTime = false;
                if (mPreferencesHelper.getDeleteTime() >= 0) {
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bill.getGenerateTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE, mPreferencesHelper.getDeleteTime());
                    isOverTime = c.getTime().before(new Date());
                }
                return isUpload && isOverTime;
            }
        }).flatMap(new Func1<Bill, Observable<Bill>>() {
            @Override
            public Observable<Bill> call(Bill bill) {
                return mDataBaseHelper.deleteBillAndBarCodeByBill(bill);
            }
        });
    }

    public void saveUpdateAddress(String address) {
        mPreferencesHelper.setUpdateServerAddress(address);
    }

    public String getUpdateAddress() {
        return mPreferencesHelper.getUpdateServerAddress();
    }

    public int getDeleteTime() {
        return mPreferencesHelper.getDeleteTime();
    }

    public void saveDeleteTime(int time) {
        mPreferencesHelper.setDeleteTime(time);
    }

    public Observable<UpdateInfo> checkUpdate() {
        return Observable.create(new Observable.OnSubscribe<UpdateInfo>() {
            @Override
            public void call(Subscriber<? super UpdateInfo> subscriber) {
                UpdateInfo info = null;
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(mPreferencesHelper.getUpdateServerAddress() + "/update.xml").openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        info = DownloadXMLParser.parse(inputStream);
                    }
                    subscriber.onNext(info);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public int getUpdateCheckVersion() {
        return mPreferencesHelper.getCheckedVersion();
    }

    public void setUpdateCheckVersion(int version) {
        mPreferencesHelper.setCheckedVersion(version);
    }

    public Observable<Boolean> checkBill(String billNumb) {
        return mDataBaseHelper.checkBill(billNumb).subscribeOn(Schedulers.newThread());
    }
}
