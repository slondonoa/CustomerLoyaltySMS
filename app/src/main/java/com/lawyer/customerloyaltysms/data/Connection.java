package com.lawyer.customerloyaltysms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stiven on 5/9/2016.
 */
public class Connection extends SQLiteOpenHelper{
    private  static final String DB_NAME="ClientsLoyaltySMS.sqlite";
    private static final  int DB_CHEMA_VERSION=2;

    public Connection(Context context) {
        super(context, DB_NAME, null, DB_CHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManager.CREATE_TABLE_SENDSMS);
        long i=0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
