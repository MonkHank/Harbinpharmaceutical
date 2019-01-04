package com.seuic.hayao.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.seuic.hayao.HYApplication;
import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.SmartProfileInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.data.bean.UserCorpToCorp;
import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.modelbean.EditCommonUseShow;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class DatabaseHelper {

    private static DatabaseHelper helper;
    private BriteDatabase mDb;

    private DatabaseHelper() {
        mDb = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(HYApplication.getApplication()), Schedulers.io());
    }

    public static DatabaseHelper getInstance() {
        if (helper == null) {
            helper = new DatabaseHelper();
        }
        return helper;
    }

    public Observable<SmartProfileInfo> getProfileInfoByName(final String name) {
        return Observable.create(new OnSubscribe<SmartProfileInfo>() {
            @Override
            public void call(Subscriber<? super SmartProfileInfo> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.ProfileInfoTable.TABLE_NAME + " where "
                            + Db.ProfileInfoTable.COLUMN_USERNAME + " = ?";
                    c = mDb.query(sql, name);
                    SmartProfileInfo info = null;
                    if (c.moveToNext()) {
                        info = Db.ProfileInfoTable.parseCursor(c);
                    }
                    subscriber.onNext(info);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Boolean> insertProfileInfo(final SmartProfileInfo info) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (info == null) {
                    subscriber.onError(new NullPointerException("info is null"));
                } else {
                    BriteDatabase.Transaction transaction = mDb.newTransaction();
                    try {
                        ContentValues values = Db.ProfileInfoTable.toContentValues(info);
                        int rows = mDb.update(Db.ProfileInfoTable.TABLE_NAME, values, Db.ProfileInfoTable.COLUMN_USERID +
                                " = ?", new String[]{info.getUserId() + ""});
                        if (rows == 0) {
                            mDb.insert(Db.ProfileInfoTable.TABLE_NAME, values);
                        }
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        transaction.markSuccessful();
                    } finally {
                        transaction.end();
                    }
                }
            }
        });
    }

    public Observable<Boolean> clearCorpInfo() {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    int row = mDb.delete(Db.CropInfoTable.TABLE_NAME, null);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> insertCropInfo(final SmartCorpInfo info) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (info == null) {
                    subscriber.onError(new RuntimeException("插入对象为Null"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    ContentValues values = Db.CropInfoTable.toContentValues(info);
                    int rows = mDb.update(Db.CropInfoTable.TABLE_NAME, values, Db.CropInfoTable.COLUMN_CORPID + " = ?", new String[]{info.getCorpId() + ""});
                    if (rows == 0) {
                        mDb.insert(Db.CropInfoTable.TABLE_NAME, values);
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> deleteUnUpdateData(final String userCorpId) {

        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    //DELETE FROM COMPANY WHERE ID = 7;
                    String sql = "delete from " + Db.UserCorp_CropTable.TABLE_NAME + " where "
                            + Db.UserCorp_CropTable.COLUMN_UPDATAFLAG + " = ? and "
                            + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?";
                    mDb.execute(sql, "0", userCorpId);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> setUpdateFlag(final String userCorpId, final String flag) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    //UPDATE COMPANY SET ADDRESS = 'Texas' WHERE ID = 6;
                    String sql = "update " + Db.UserCorp_CropTable.TABLE_NAME + " set "
                            + Db.UserCorp_CropTable.COLUMN_UPDATAFLAG + " = ? " + " where "
                            + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?";
                    mDb.execute(sql, flag, userCorpId);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Integer> insertDownloadInfo(final ArrayList<SmartCorpInfo> corps, final String userCorpId) {
        return Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (corps == null) {
                    subscriber.onError(new RuntimeException("插入对象为Null"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                String sql1 = "insert or ignore into " + Db.UserCorp_CropTable.TABLE_NAME + " ("
                        + Db.UserCorp_CropTable.COLUMN_CORPID + ","
                        + Db.UserCorp_CropTable.COLUMN_USERCORPID + ","
                        + Db.UserCorp_CropTable.COLUMN_COMMONUSE + ") VALUES (?,?,?)";

                String sql2 = "insert or replace into " + Db.CropInfoTable.TABLE_NAME + " ("
                        + Db.CropInfoTable.COLUMN_CORPCODE + ","
                        + Db.CropInfoTable.COLUMN_CORPID + ","
                        + Db.CropInfoTable.COLUMN_CORPNAME + ","
                        + Db.CropInfoTable.COLUMN_CORPPINYIN + ") VALUES (?,?,?,?)";
//                Cursor c = null;
                try {
//                    String sql = "select * from " + Db.UserCorp_CropTable.TABLE_NAME + " where " + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?";
//                    c = mDb.query(sql, userCorpId);
//                    ArrayList<String> infos = new ArrayList<String>();
//                    while (c.moveToNext()) {
//                        infos.add(c.getString(c.getColumnIndexOrThrow(Db.UserCorp_CropTable.COLUMN_CORPID)));
//                    }
                    for (SmartCorpInfo corp : corps) {
//                        infos.remove(corp.getCorpCode());
//                        mDb.execute(sql1, corp.getCorpId() + "", userCorpId, "11");

                        UserCorpToCorp userCorpToCorp = new UserCorpToCorp();
                        userCorpToCorp.setUpdateFlag("1");
                        userCorpToCorp.setUserCorpId(userCorpId);
                        userCorpToCorp.setCorpId(corp.getCorpId() + "");
                        ContentValues values = Db.UserCorp_CropTable.toContentValues(userCorpToCorp);
                        values.remove(Db.UserCorp_CropTable.COLUMN_COMMONUSE);
                        int rows = mDb.update(Db.UserCorp_CropTable.TABLE_NAME, values, Db.UserCorp_CropTable.COLUMN_CORPID + " = ? and "
                                + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?", new String[]{userCorpToCorp.getCorpId(), userCorpToCorp.getUserCorpId()});
                        if (rows == 0) {
                            values.put(Db.UserCorp_CropTable.COLUMN_COMMONUSE, "11");
                            mDb.insert(Db.UserCorp_CropTable.TABLE_NAME, values);
                        }

                        mDb.execute(sql2, corp.getCorpCode(), corp.getCorpId() + "", corp.getCorpName(), corp.getCorpPinyin());
//                        long row = mDb.insert(Db.UserCorp_CropTable.TABLE_NAME, Db.UserCorp_CropTable.toContentValues(userCorpToCorp));

//                        ContentValues values = Db.CropInfoTable.toContentValues(corp);
//                        int r = mDb.update(Db.CropInfoTable.TABLE_NAME, values, Db.CropInfoTable.COLUMN_CORPID + " = ?", new String[]{corp.getCorpId() + ""});
//                        if (r == 0) {
//                            mDb.insert(Db.CropInfoTable.TABLE_NAME, values);
//                        }
                    }
//                    if (infos != null && infos.size() > 0) {
//                        //  int row = mDb.delete(Db.BarCodeTable.TABLE_NAME, Db.BarCodeTable.COLUMN_BILLNUMBER + " = ?", number);
//                        for (String corpId : infos) {
//                            mDb.delete(Db.UserCorp_CropTable.TABLE_NAME, Db.UserCorp_CropTable.COLUMN_CORPID
//                                    + " = ? and " + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?", corpId, userCorpId);
//                            mDb.delete(Db.CropInfoTable.TABLE_NAME, Db.CropInfoTable.COLUMN_CORPID + " = ?", corpId);
//                        }
//                    }
                    subscriber.onNext(corps.size());
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<ArrayList<SmartCorpInfo>> getCorpInfoList() {
        return Observable.create(new OnSubscribe<ArrayList<SmartCorpInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SmartCorpInfo>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.CropInfoTable.TABLE_NAME;
                    c = mDb.query(sql);
                    ArrayList<SmartCorpInfo> infos = new ArrayList<SmartCorpInfo>();
                    while (c.moveToNext()) {
                        infos.add(Db.CropInfoTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<ArrayList<SmartCorpInfo>> getUserCorpInfos(final String userCorpId, final boolean isIn) {
        return Observable.create(new OnSubscribe<ArrayList<SmartCorpInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SmartCorpInfo>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    //select * from crop_info where Corpid in (select corpid from usercorp_crop where usercorpid = "67216")
                    String sql = "select * from " + Db.CropInfoTable.TABLE_NAME + " where "
                            + Db.CropInfoTable.COLUMN_CORPID + " in ( select " + Db.UserCorp_CropTable.COLUMN_CORPID + " from "
                            + Db.UserCorp_CropTable.TABLE_NAME + " where " + Db.UserCorp_CropTable.COLUMN_USERCORPID
                            + " = ? and " + Db.UserCorp_CropTable.COLUMN_COMMONUSE + " like ?" + ")";
                    c = mDb.query(sql, userCorpId, isIn ? "1_" : "_1");
                    ArrayList<SmartCorpInfo> infos = new ArrayList<SmartCorpInfo>();
                    while (c.moveToNext()) {
                        infos.add(Db.CropInfoTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<SmartCorpInfo> getCorpInfoById(final String id) {
        return Observable.create(new OnSubscribe<SmartCorpInfo>() {
            @Override
            public void call(Subscriber<? super SmartCorpInfo> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.CropInfoTable.TABLE_NAME + " where " + Db.CropInfoTable.COLUMN_CORPID + " = ?";
                    c = mDb.query(sql, id);
                    SmartCorpInfo info = null;
                    while (c.moveToNext()) {
                        info = Db.CropInfoTable.parseCursor(c);
                    }
                    subscriber.onNext(info);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Boolean> insertStoreTypeInfo(final StoreTypeInfo info) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (info == null) {
                    subscriber.onError(new RuntimeException("插入对象为Null"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    ContentValues values = Db.StoreTypeInfoTable.toContentValues(info);
                    int rows = mDb.update(Db.StoreTypeInfoTable.TABLE_NAME, values, Db.StoreTypeInfoTable.COLUMN_STORETYPE + " = ?", new String[]{info.getStoreType() + ""});
                    if (rows == 0) {
                        mDb.insert(Db.StoreTypeInfoTable.TABLE_NAME, values);
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> isBarcodeExist(final String barcode, final String storetypeid) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
//select count(*) from barcode where '3' in (select storetypeid from bills where billnumber in (select billnumber from barcode where barcode = '288563'))
                    //相同类型未上传单据条码中是否存在改条码
                    String sql = "select count(*) from barcode where ? in (select storetypeid from bills where billnumber in (select billnumber from barcode where barcode = ?)  and isupload = ?)";
//                    String sql = "select count(*) from " + Db.BarCodeTable.TABLE_NAME + " where " + Db.BarCodeTable.COLUMN_BARCODE + " = ?";
                    c = mDb.query(sql, storetypeid, barcode, "0");
                    ArrayList<StoreTypeInfo> infos = new ArrayList<StoreTypeInfo>();
                    while (c.moveToNext()) {
                        subscriber.onNext(c.getInt(0) > 0 ? true : false);
                    }
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Boolean> insertStoreTypeInfo(final ArrayList<StoreTypeInfo> infos) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (infos.size() == 0) {
                    subscriber.onError(new RuntimeException("插入集合为Null"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    for (StoreTypeInfo info : infos) {
                        ContentValues values = Db.StoreTypeInfoTable.toContentValues(info);
                        int rows = mDb.update(Db.StoreTypeInfoTable.TABLE_NAME, values, Db.StoreTypeInfoTable.COLUMN_STORETYPE + " = ?", new String[]{info.getStoreType() + ""});
                        if (rows == 0) {
                            mDb.insert(Db.StoreTypeInfoTable.TABLE_NAME, values);
                        }
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<ArrayList<StoreTypeInfo>> getStoreTypeInfoList() {
        return Observable.create(new OnSubscribe<ArrayList<StoreTypeInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<StoreTypeInfo>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.StoreTypeInfoTable.TABLE_NAME;
                    c = mDb.query(sql);
                    ArrayList<StoreTypeInfo> infos = new ArrayList<StoreTypeInfo>();
                    while (c.moveToNext()) {
                        infos.add(Db.StoreTypeInfoTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<ArrayList<StoreTypeInfo>> getStoreTypeInfoByKind(final StoreKind kind) {
        return Observable.create(new OnSubscribe<ArrayList<StoreTypeInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<StoreTypeInfo>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.StoreTypeInfoTable.TABLE_NAME + " where " + Db.StoreTypeInfoTable.COLUMN_STOREKIND + " = ?";
                    c = mDb.query(sql, kind.name());
                    ArrayList<StoreTypeInfo> infos = new ArrayList<StoreTypeInfo>();
                    while (c.moveToNext()) {
                        infos.add(Db.StoreTypeInfoTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<StoreTypeInfo> getStoreTypeInfoById(final String id) {
        return Observable.create(new OnSubscribe<StoreTypeInfo>() {
            @Override
            public void call(Subscriber<? super StoreTypeInfo> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.StoreTypeInfoTable.TABLE_NAME + " where " + Db.StoreTypeInfoTable.COLUMN_STORETYPE + " = ?";
                    c = mDb.query(sql, id);
                    StoreTypeInfo info = null;
                    while (c.moveToNext()) {
                        info = Db.StoreTypeInfoTable.parseCursor(c);
                    }
                    subscriber.onNext(info);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Boolean> deleteBillByNumber(final String number) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                Cursor c = null;
                try {
                    int row = mDb.delete(Db.BillsTable.TABLE_NAME, Db.BillsTable.COLUMN_BILLNUMBER + " = ?", number);
                    if (row > 0) {
                        subscriber.onNext(true);
                    } else {
                        subscriber.onNext(false);
                    }
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    if (c != null) c.close();
                    transaction.end();
                }
            }
        });
    }

    public Observable<Bill> deleteBillAndBarCodeByBill(final Bill bill) {
        return Observable.create(new OnSubscribe<Bill>() {
            @Override
            public void call(Subscriber<? super Bill> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                Cursor c = null;
                try {
                    int row = mDb.delete(Db.BillsTable.TABLE_NAME, Db.BillsTable.COLUMN_BILLNUMBER + " = ?", bill.getBillNumber());
                    int rows = mDb.delete(Db.BarCodeTable.TABLE_NAME, Db.BarCodeTable.COLUMN_BILLNUMBER + " = ?", bill.getBillNumber());
                    if (row > 0 && rows > 0) {
                        subscriber.onNext(bill);
                    } else {
                        throw new RuntimeException("问题单据，删除失败");
                    }
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    if (c != null) c.close();
                    transaction.end();
                }
            }
        });
    }

    public Observable<ArrayList<Bill>> getBillListByUserId(final String userId) {
        return Observable.create(new OnSubscribe<ArrayList<Bill>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Bill>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
//select  * from bills order by isupload asc limit 0,( select count(*) from bills where isupload = 0 )+50
                    String sql = "select * from " + Db.BillsTable.TABLE_NAME +
                            " where " + Db.BillsTable.COLUMN_CREATORID + " = ? " +
                            " ORDER BY " + Db.BillsTable.COLUMN_ISUPLOAD +
                            " ASC" + " limit 0,(select count(*) from " + Db.BillsTable.TABLE_NAME +
                            " where " + Db.BillsTable.COLUMN_ISUPLOAD + " = 0 )+50";
                    c = mDb.query(sql, userId);
                    ArrayList<Bill> infos = new ArrayList<Bill>();
                    while (c.moveToNext()) {
                        infos.add(Db.BillsTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<ArrayList<Bill>> getBillList() {
        return Observable.create(new OnSubscribe<ArrayList<Bill>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Bill>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.BillsTable.TABLE_NAME + " ORDER BY " + Db.BillsTable.COLUMN_ISUPLOAD + " ASC";
                    c = mDb.query(sql);
                    ArrayList<Bill> infos = new ArrayList<Bill>();
                    while (c.moveToNext()) {
                        infos.add(Db.BillsTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<ArrayList<Bill>> getUnUploadBillListByCreator(final String creator) {
        return Observable.create(new OnSubscribe<ArrayList<Bill>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Bill>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.BillsTable.TABLE_NAME + " where " + Db.BillsTable.COLUMN_ISUPLOAD + "= ? and " + Db.BillsTable.COLUMN_CREATORID + " = ?";
                    c = mDb.query(sql, "0", creator);
                    ArrayList<Bill> infos = new ArrayList<Bill>();
                    while (c.moveToNext()) {
                        infos.add(Db.BillsTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Bill> getBillByNumber(final String number) {
        return Observable.create(new OnSubscribe<Bill>() {
            @Override
            public void call(Subscriber<? super Bill> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.BillsTable.TABLE_NAME + " where " + Db.BillsTable.COLUMN_BILLNUMBER + " = ?";
                    c = mDb.query(sql, number);
                    Bill bill = null;
                    while (c.moveToNext()) {
                        bill = Db.BillsTable.parseCursor(c);
                    }
                    subscriber.onNext(bill);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Boolean> insertBill(final Bill bill) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (bill == null) {
                    subscriber.onError(new RuntimeException("插入对象为Null"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    ContentValues values = Db.BillsTable.toContentValues(bill);
                    int rows = mDb.update(Db.BillsTable.TABLE_NAME, values, Db.BillsTable.COLUMN_BILLNUMBER + " = ?", new String[]{bill.getBillNumber() + ""});
                    if (rows == 0) {
                        mDb.insert(Db.BillsTable.TABLE_NAME, values);
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Integer> getBarcodeNumberByBillNumber(final String billNumber) {

        return Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select count(*) from " + Db.BarCodeTable.TABLE_NAME + " where " + Db.BarCodeTable.COLUMN_BILLNUMBER + " = ?";
                    c = mDb.query(sql, billNumber);
                    while (c.moveToNext()) {
                        subscriber.onNext(c.getInt(0));
                    }
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<ArrayList<Barcode>> getBarcodeListByBillNumber(final String billNumber) {
        return Observable.create(new OnSubscribe<ArrayList<Barcode>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Barcode>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.BarCodeTable.TABLE_NAME + " where " + Db.BarCodeTable.COLUMN_BILLNUMBER + " = ?";
                    c = mDb.query(sql, billNumber);
                    ArrayList<Barcode> infos = new ArrayList<Barcode>();
                    while (c.moveToNext()) {
                        infos.add(Db.BarCodeTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<ArrayList<Barcode>> getBarcodeList() {
        return Observable.create(new OnSubscribe<ArrayList<Barcode>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Barcode>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.BarCodeTable.TABLE_NAME;
                    c = mDb.query(sql);
                    ArrayList<Barcode> infos = new ArrayList<Barcode>();
                    while (c.moveToNext()) {
                        infos.add(Db.BarCodeTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Boolean> insertBarcode(final Barcode barcode) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (barcode == null) {
                    subscriber.onError(new RuntimeException("插入null值"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    ContentValues values = Db.BarCodeTable.toContentValues(barcode);
                    mDb.insert(Db.BarCodeTable.TABLE_NAME, values);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> insertBarcodeList(final ArrayList<Barcode> barcodeList) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (barcodeList.size() == 0) {
                    subscriber.onError(new RuntimeException("插入列表为空"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    for (Barcode code : barcodeList) {
                        ContentValues values = Db.BarCodeTable.toContentValues(code);
                        mDb.insert(Db.BarCodeTable.TABLE_NAME, values);
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> deleteBarcodeByNumber(final String number) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                Cursor c = null;
                try {
                    int row = mDb.delete(Db.BarCodeTable.TABLE_NAME, Db.BarCodeTable.COLUMN_BILLNUMBER + " = ?", number);
                    if (row > 0) {
                        subscriber.onNext(true);
                    } else {
                        subscriber.onNext(false);
                    }
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    if (c != null) c.close();
                    transaction.end();
                }
            }
        });
    }

    public Observable<UserCorpToCorp> insertUserCorpToCorp(final UserCorpToCorp userCorpToCorp) {

        return Observable.create(new OnSubscribe<UserCorpToCorp>() {
            @Override
            public void call(Subscriber<? super UserCorpToCorp> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (userCorpToCorp == null) {
                    subscriber.onError(new RuntimeException("插入不能为空"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    ContentValues values = Db.UserCorp_CropTable.toContentValues(userCorpToCorp);
                    int rows = mDb.update(Db.UserCorp_CropTable.TABLE_NAME, values, Db.UserCorp_CropTable.COLUMN_CORPID + " = ? and "
                            + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?", new String[]{userCorpToCorp.getCorpId(), userCorpToCorp.getUserCorpId()});
                    if (rows == 0) {
                        long row = mDb.insert(Db.UserCorp_CropTable.TABLE_NAME, values);
                    }
                    subscriber.onNext(userCorpToCorp);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> updateUserCorpToCorp(final UserCorpToCorp userCorpToCorp) {

        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (userCorpToCorp == null) {
                    subscriber.onError(new RuntimeException("更新参数不能为空"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    ContentValues values = Db.UserCorp_CropTable.toContentValues(userCorpToCorp);
                    int rows = mDb.update(Db.UserCorp_CropTable.TABLE_NAME, values, Db.UserCorp_CropTable.COLUMN_CORPID + " = ? and "
                            + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?", new String[]{userCorpToCorp.getCorpId(), userCorpToCorp.getUserCorpId()});
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> deleteUserCorpToCorp(final String userCorpId) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                Cursor c = null;
                try {
                    int row = mDb.delete(Db.UserCorp_CropTable.TABLE_NAME, Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?", userCorpId);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    if (c != null) c.close();
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> updateUserCorpToCorpList(final ArrayList<EditCommonUseShow> userCorpToCorps) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (userCorpToCorps == null) {
                    subscriber.onError(new RuntimeException("更新参数不能为空"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    for (EditCommonUseShow temp : userCorpToCorps) {
                        UserCorpToCorp userCorpToCorp = temp.getCheckStatInfo();
                        ContentValues values = Db.UserCorp_CropTable.toContentValues(userCorpToCorp);
//                        mDb.delete(Db.UserCorp_CropTable.TABLE_NAME, Db.UserCorp_CropTable.COLUMN_CORPID + " = ? and "
//                                + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?", userCorpToCorp.getCorpId(), userCorpToCorp.getUserCorpId());
//                        mDb.insert(Db.UserCorp_CropTable.TABLE_NAME,values);
                        int rows = mDb.update(Db.UserCorp_CropTable.TABLE_NAME, values, Db.UserCorp_CropTable.COLUMN_CORPID + " = ? and "
                                + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?", new String[]{userCorpToCorp.getCorpId(), userCorpToCorp.getUserCorpId()});
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<ArrayList<UserCorpToCorp>> getUserCorpToCorpListByUserCorpId(final String userCorpId) {

        return Observable.create(new OnSubscribe<ArrayList<UserCorpToCorp>>() {
            @Override
            public void call(Subscriber<? super ArrayList<UserCorpToCorp>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select * from " + Db.UserCorp_CropTable.TABLE_NAME + " where " + Db.UserCorp_CropTable.COLUMN_USERCORPID + " = ?";
                    c = mDb.query(sql, userCorpId);
                    ArrayList<UserCorpToCorp> infos = new ArrayList<UserCorpToCorp>();
                    while (c.moveToNext()) {
                        infos.add(Db.UserCorp_CropTable.parseCursor(c));
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<ArrayList<EditCommonUseShow>> getUserCorpAndCommonUse(final String userCorpId) {
        return Observable.create(new OnSubscribe<ArrayList<EditCommonUseShow>>() {
            @Override
            public void call(Subscriber<? super ArrayList<EditCommonUseShow>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    //select t.* , crop.commonuse from crop_info t left join usercorp_crop crop on crop.corpid = t.corpid where crop.usercorpid = ?
                    String sql1 = "select t.* , crop.commonuse from " + Db.CropInfoTable.TABLE_NAME + " t left join " + Db.UserCorp_CropTable.TABLE_NAME + " crop on crop.corpid = t.corpid where crop.usercorpid = ?";
//                    String sql = "select * from " + Db.CropInfoTable.TABLE_NAME + " where "
//                            + Db.CropInfoTable.COLUMN_CORPID + " in ( select " + Db.UserCorp_CropTable.COLUMN_CORPID + " from "
//                            + Db.UserCorp_CropTable.TABLE_NAME + " where " + Db.UserCorp_CropTable.COLUMN_USERCORPID
//                            + " = ?";
                    c = mDb.query(sql1, userCorpId);
                    ArrayList<EditCommonUseShow> infos = new ArrayList<EditCommonUseShow>();
                    while (c.moveToNext()) {
                        EditCommonUseShow show = new EditCommonUseShow();
                        SmartCorpInfo info = Db.CropInfoTable.parseCursor(c);
                        UserCorpToCorp corpToCorp = new UserCorpToCorp();
                        show.setCorpInfo(info);
                        corpToCorp.setCorpId(info.getCorpId() + "");
                        corpToCorp.setCommonUse(c.getString(c.getColumnIndexOrThrow(Db.UserCorp_CropTable.COLUMN_COMMONUSE)));
                        corpToCorp.setUserCorpId(userCorpId);
                        show.setCheckStatInfo(corpToCorp);
                        show.setOriginState(c.getString(c.getColumnIndexOrThrow(Db.UserCorp_CropTable.COLUMN_COMMONUSE)));
                        infos.add(show);
                    }
                    subscriber.onNext(infos);
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }

    public Observable<Boolean> updateBillToModifyState(final String billNumber, final String state) {//3为修改状态
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (TextUtils.isEmpty(billNumber)) {
                    subscriber.onError(new RuntimeException("订单号为空"));
                    return;
                }
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    //UPDATE COMPANY SET ADDRESS = 'Texas' WHERE ID = 6;
                    mDb.execute("update " + Db.BillsTable.TABLE_NAME + " set " + Db.BillsTable.COLUMN_ISUPLOAD + " = ? " + " where " + Db.BillsTable.COLUMN_BILLNUMBER + " = ? ", state, billNumber);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> checkBill(final String billNumb) {
        return Observable.create(new OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor c = null;
                try {
                    String sql = "select count(*) from " + Db.BillsTable.TABLE_NAME + " where " + Db.BillsTable.COLUMN_BILLNUMBER + " = ?";
                    c = mDb.query(sql, billNumb);
                    while (c.moveToNext()) {
                        subscriber.onNext(c.getInt(0) > 0);
                    }
                    subscriber.onCompleted();
                } finally {
                    if (c != null) c.close();
                }
            }
        });
    }
}
