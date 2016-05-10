package com.lawyer.customerloyaltysms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by stiven on 5/9/2016.
 */
public class DataBaseManager {
    public static final String TABLE_NAME_SENDSMS="SendSMS";
    public static String CN_IdPerson = "IdPerson";
    public static String CN_Name = "Name";
    public static String CN_LastName = "LastName";
    public static String CN_Document = "Document";
    public static String CN_Cell1 = "Cell1";
    public static String CN_Cell2 = "Cell2";
    public static String CN_Cell3 = "Cell3";
    public static String CN_sent = "Sent";
    public static String CN_ID = "Id";

    public static final String CREATE_TABLE_SENDSMS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SENDSMS + "(" +
            CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CN_IdPerson + " TEXT, " +
            CN_Name + " TEXT, " +
            CN_LastName + " TEXT, " +
            CN_Document + " TEXT, " +
            CN_Cell1 + " TEXT, " +
            CN_Cell2 + " TEXT, " +
            CN_Cell3 + " TEXT, " +
            CN_sent + " INTEGER);";

    private Connection cn;
    private SQLiteDatabase db;
    public DataBaseManager(Context context) {
        cn=new Connection(context);
        db=cn.getWritableDatabase();
    }

    public void  Close(Context context)
    {
        cn=new Connection(context);
        cn.close();
    }

    public void Open(Context context)
    {
        cn=new Connection(context);
        cn.getWritableDatabase();
    }


}
