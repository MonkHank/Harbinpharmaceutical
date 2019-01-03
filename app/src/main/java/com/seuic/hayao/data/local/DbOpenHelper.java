package com.seuic.hayao.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + File.separator + "hayao" + File.separator + "hayao.db";
    public static final int DATABASE_VERSION = 3;

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(Db.ProfileInfoTable.CREATE);
            db.execSQL(Db.CropInfoTable.CREATE);
            db.execSQL(Db.StoreTypeInfoTable.CREATE);
            db.execSQL(Db.BillsTable.CREATE);
            db.execSQL(Db.BarCodeTable.CREATE);
            db.execSQL(Db.UserCorp_CropTable.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 2 && oldVersion == 1) {//1向2升级 常用标记为改为11,00格式分别 代表入库 出库的常用往来企业
            db.beginTransaction();
            String sql = "update usercorp_crop set commonuse = '11' where commonuse = '1'";
            String sql1 = "update usercorp_crop set commonuse = '00' where commonuse = '0'";
            try {
                db.execSQL(sql);
                db.execSQL(sql1);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        if (newVersion == 3 && oldVersion == 2) {//2 向3升级
            db.beginTransaction();
            try {
                db.execSQL("ALTER TABLE usercorp_crop RENAME TO t_usercorp_crop");
                db.execSQL(Db.UserCorp_CropTable.CREATE);
                String cpy = "INSERT INTO usercorp_crop (usercorpid,corpid,commonuse,updataflag) SELECT usercorpid,corpid,commonuse,'0' FROM t_usercorp_crop";
                db.execSQL(cpy);
                db.execSQL("DROP TABLE t_usercorp_crop");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        if (newVersion == 3 && oldVersion == 1) {
            db.beginTransaction();
            String sql = "update usercorp_crop set commonuse = '11' where commonuse = '1'";
            String sql1 = "update usercorp_crop set commonuse = '00' where commonuse = '0'";
            try {
                db.execSQL(sql);
                db.execSQL(sql1);

                db.execSQL("ALTER TABLE usercorp_crop RENAME TO t_usercorp_crop");
                db.execSQL(Db.UserCorp_CropTable.CREATE);
                String cpy = "INSERT INTO usercorp_crop (usercorpid,corpid,commonuse,updataflag) SELECT usercorpid,corpid,commonuse,'0' FROM t_usercorp_crop";
                db.execSQL(cpy);
                db.execSQL("DROP TABLE t_usercorp_crop");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}