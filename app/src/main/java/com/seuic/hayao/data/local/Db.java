package com.seuic.hayao.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.SmartProfileInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.data.bean.UserCorpToCorp;
import com.seuic.hayao.enums.CorpType;
import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.enums.StoreSort;

public final class Db {

    public static final class ProfileInfoTable {

        public static final String TABLE_NAME = "profile_info";

        public static final String COLUMN_USERID = "UserId";
        public static final String COLUMN_USERNAME = "UserName";
        public static final String COLUMN_USERDISPLAYNAME = "UserDisplayName";
        public static final String COLUMN_CORPID = "CorpId";
        public static final String COLUMN_CORPCODE = "CorpCode";
        public static final String COLUMN_CORPNAME = "CorpName";
        public static final String COLUMN_CORPTYPE = "CorpType";
        public static final String COLUMN_PASSWORD = "Password";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_USERID + " TEXT PRIMARY KEY, " +
                        COLUMN_USERNAME + " TEXT NOT NULL, " +
                        COLUMN_USERDISPLAYNAME + " TEXT NOT NULL, " +
                        COLUMN_CORPID + " TEXT NOT NULL, " +
                        COLUMN_CORPCODE + " TEXT NOT NULL, " +
                        COLUMN_CORPNAME + " TEXT NOT NULL, " +
                        COLUMN_CORPTYPE + " TEXT NOT NULL, " +
                        COLUMN_PASSWORD + " TEXT NOT NULL " +
                        " ); ";

        public static ContentValues toContentValues(SmartProfileInfo profile) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERID, profile.getUserId());
            values.put(COLUMN_USERNAME, profile.getUserName());
            values.put(COLUMN_USERDISPLAYNAME, profile.getUserDisplayName());
            values.put(COLUMN_CORPID, profile.getCorpId());
            values.put(COLUMN_CORPCODE, profile.getCorpCode());
            values.put(COLUMN_CORPNAME, profile.getCorpName());
            values.put(COLUMN_CORPTYPE, profile.getCorpType().name());
            values.put(COLUMN_PASSWORD, profile.getPassword());
            return values;
        }

        public static SmartProfileInfo parseCursor(Cursor cursor) {
            SmartProfileInfo profile = new SmartProfileInfo();
            profile.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERID))));
            profile.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            profile.setUserDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERDISPLAYNAME)));
            profile.setCorpId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPID))));
            profile.setCorpCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPCODE)));
            profile.setCorpName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPNAME)));
            profile.setCorpType(CorpType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPTYPE))));
            profile.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            return profile;
        }
    }

    public static final class CropInfoTable {

        public static final String TABLE_NAME = "crop_info";

        public static final String COLUMN_CORPCODE = "CorpCode";
        public static final String COLUMN_CORPID = "CorpId";
        public static final String COLUMN_CORPNAME = "CorpName";
        public static final String COLUMN_CORPPINYIN = "CorpPinyin";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_CORPID + " TEXT PRIMARY KEY, " +
                        COLUMN_CORPCODE + " TEXT NOT NULL, " +
                        COLUMN_CORPNAME + " TEXT NOT NULL, " +
                        COLUMN_CORPPINYIN + " TEXT NOT NULL " +
                        " ); ";

        public static ContentValues toContentValues(SmartCorpInfo corpInfo) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CORPCODE, corpInfo.getCorpCode());
            values.put(COLUMN_CORPID, corpInfo.getCorpId());
            values.put(COLUMN_CORPNAME, corpInfo.getCorpName());
            values.put(COLUMN_CORPPINYIN, corpInfo.getCorpPinyin());
            return values;
        }

        public static SmartCorpInfo parseCursor(Cursor cursor) {
            SmartCorpInfo info = new SmartCorpInfo();
            info.setCorpCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPCODE)));
            info.setCorpName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPNAME)));
            info.setCorpPinyin(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPPINYIN)));
            info.setCorpId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPID))));
            return info;
        }
    }

    public static final class StoreTypeInfoTable {

        public static final String TABLE_NAME = "store_type_info";

        public static final String COLUMN_STOREKIND = "storekind";
        public static final String COLUMN_STORESORT = "storesort";
        public static final String COLUMN_STORETYPE = "storetype";
        public static final String COLUMN_STORETYPEKEY = "storetypekey";
        public static final String COLUMN_HASBIZCORP = "hasbizcorp";
        public static final String COLUMN_HASRECCORP = "hasreccorp";
        public static final String COLUMN_UPSTORETYP = "upstoretype";
        public static final String COLUMN_DOWNSTORETYPE = "downstoretype";
        public static final String COLUMN_STORETYPETEXT = "storetypetext";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_STORETYPE + " TEXT PRIMARY KEY, " +
                        COLUMN_STOREKIND + " TEXT NOT NULL, " +
                        COLUMN_STORESORT + " TEXT NOT NULL, " +
                        COLUMN_STORETYPEKEY + " TEXT NOT NULL, " +
                        COLUMN_HASBIZCORP + " TEXT NOT NULL, " +
                        COLUMN_HASRECCORP + " TEXT NOT NULL, " +
                        COLUMN_UPSTORETYP + " TEXT NOT NULL, " +
                        COLUMN_DOWNSTORETYPE + " TEXT NOT NULL, " +
                        COLUMN_STORETYPETEXT + " TEXT NOT NULL " +
                        " ); ";

        public static ContentValues toContentValues(StoreTypeInfo storeTypeInfo) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_STOREKIND, storeTypeInfo.getStoreKind().name());
            values.put(COLUMN_STORESORT, storeTypeInfo.getStoreSort().name());
            values.put(COLUMN_STORETYPE, storeTypeInfo.getStoreType() + "");
            values.put(COLUMN_STORETYPEKEY, storeTypeInfo.getStoreTypeKey());
            values.put(COLUMN_HASBIZCORP, storeTypeInfo.isHasBizCorp() ? "true" : "false");
            values.put(COLUMN_HASRECCORP, storeTypeInfo.isHasRecCorp() ? "true" : "false");
            values.put(COLUMN_UPSTORETYP, storeTypeInfo.getUpStoreType());
            values.put(COLUMN_DOWNSTORETYPE, storeTypeInfo.getDownStoreType());
            values.put(COLUMN_STORETYPETEXT, storeTypeInfo.getStoreTypeText());
            return values;
        }

        public static StoreTypeInfo parseCursor(Cursor cursor) {
            StoreTypeInfo info = new StoreTypeInfo();
            info.setDownStoreType(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOWNSTORETYPE))));
            info.setUpStoreType(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPSTORETYP))));
            info.setStoreTypeText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORETYPETEXT)));
            info.setStoreType(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORETYPE))));
            info.setHasBizCorp(Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HASBIZCORP))));
            info.setHasRecCorp(Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HASRECCORP))));
            info.setStoreKind(StoreKind.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOREKIND))));
            info.setStoreSort(StoreSort.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORESORT))));
            info.setStoreTypeKey(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORETYPEKEY)));
            return info;
        }
    }

    public static final class BillsTable {

        public static final String TABLE_NAME = "bills";

        public static final String COLUMN_BILLNUMBER = "billnumber";
        public static final String COLUMN_CONTACTCORPID = "contactcorpid";
        public static final String COLUMN_STORETYPEID = "storetypeid";
        public static final String COLUMN_GENERATETIME = "generatetime";
        public static final String COLUMN_CREATORID = "creatorid";
        public static final String COLUMN_ISUPLOAD = "isupload";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_BILLNUMBER + " TEXT PRIMARY KEY, " +
                        COLUMN_CONTACTCORPID + " TEXT NOT NULL, " +
                        COLUMN_STORETYPEID + " TEXT NOT NULL, " +
                        COLUMN_GENERATETIME + " TEXT NOT NULL, " +
                        COLUMN_CREATORID + " TEXT NOT NULL, " +
                        COLUMN_ISUPLOAD + " TEXT NOT NULL " +
                        " ); ";

        public static ContentValues toContentValues(Bill bill) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BILLNUMBER, bill.getBillNumber());
            values.put(COLUMN_CONTACTCORPID, bill.getContactCorpId());
            values.put(COLUMN_STORETYPEID, bill.getStoreTypeId());
            values.put(COLUMN_ISUPLOAD, bill.getIsUpload());
            values.put(COLUMN_CREATORID, bill.getCreatorId());
            values.put(COLUMN_GENERATETIME, bill.getGenerateTime());
            return values;
        }

        public static Bill parseCursor(Cursor cursor) {
            Bill bill = new Bill();
            bill.setBillNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BILLNUMBER)));
            bill.setContactCorpId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACTCORPID)));
            bill.setStoreTypeId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORETYPEID)));
            bill.setIsUpload(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISUPLOAD)));
            bill.setGenerateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENERATETIME)));
            bill.setCreatorId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATORID)));
            return bill;
        }
    }

    public static final class BarCodeTable {

        public static final String TABLE_NAME = "barcode";

        public static final String COLUMN_BILLNUMBER = "billnumber";
        public static final String COLUMN_BARCODE = "barcode";
        public static final String COLUMN_ACTDATE = "actdate";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_BARCODE + " TEXT NOT NULL, " +
                        COLUMN_BILLNUMBER + " TEXT NOT NULL , " +
                        COLUMN_ACTDATE + " TEXT NOT NULL ," +
                        "PRIMARY KEY(" + COLUMN_BARCODE + ", " + COLUMN_BILLNUMBER + ")" +
                        " ); ";

        public static ContentValues toContentValues(Barcode code) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BILLNUMBER, code.getBillNumber());
            values.put(COLUMN_BARCODE, code.getBarcode());
            values.put(COLUMN_ACTDATE, code.getActDate());
            return values;
        }

        public static Barcode parseCursor(Cursor cursor) {
            Barcode barcode = new Barcode();
            barcode.setBillNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BILLNUMBER)));
            barcode.setBarcode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BARCODE)));
            barcode.setActDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTDATE)));
            return barcode;
        }
    }

    public static final class UserCorp_CropTable {

        public static final String TABLE_NAME = "usercorp_crop";

        public static final String COLUMN_USERCORPID = "usercorpid";
        public static final String COLUMN_CORPID = "corpid";
        public static final String COLUMN_COMMONUSE = "commonuse";
        public static final String COLUMN_UPDATAFLAG = "updataflag";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_USERCORPID + " TEXT NOT NULL , " +
                        COLUMN_CORPID + " TEXT NOT NULL ," +
                        COLUMN_COMMONUSE + " TEXT NOT NULL ," +
                        COLUMN_UPDATAFLAG + " TEXT ," +
                        "PRIMARY KEY(" + COLUMN_USERCORPID + ", " + COLUMN_CORPID + ")" +
                        " ); ";

        public static ContentValues toContentValues(UserCorpToCorp userCorpToCorp) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CORPID, userCorpToCorp.getCorpId());
            values.put(COLUMN_USERCORPID, userCorpToCorp.getUserCorpId());
            values.put(COLUMN_COMMONUSE, userCorpToCorp.getCommonUse());
            values.put(COLUMN_UPDATAFLAG, userCorpToCorp.getUpdateFlag());
            return values;
        }

        public static UserCorpToCorp parseCursor(Cursor cursor) {
            UserCorpToCorp userCorpToCorp = new UserCorpToCorp();
            userCorpToCorp.setCorpId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORPID)));
            userCorpToCorp.setUserCorpId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERCORPID)));
            userCorpToCorp.setCommonUse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMONUSE)));
            userCorpToCorp.setUpdateFlag(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMONUSE)));
            return userCorpToCorp;
        }
    }
}
