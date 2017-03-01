package com.lawyer.customerloyaltysms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stiven on 5/9/2016.
 */
public class Connection extends SQLiteOpenHelper{
    private  static final String DB_NAME="ClientsLoyaltySMS.sqlite";
    private static final  int DB_CHEMA_VERSION=3;

    public Connection(Context context) {
        super(context, DB_NAME, null, DB_CHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManager.CREATE_TABLE_SENDSMS);
        db.execSQL(DataBaseManager.CREATE_TABLE_PROCESSSMS);
        db.execSQL(DataBaseManager.CREATE_TABLE_FILTERSMS);
        db.execSQL(DataBaseManager.CREATE_TABLE_BIRTHDAYSMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseManager.CREATE_TABLE_SENDSMS);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseManager.CREATE_TABLE_PROCESSSMS);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseManager.CREATE_TABLE_FILTERSMS);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseManager.CREATE_TABLE_BIRTHDAYSMS);
        onCreate(db);
    }

}
