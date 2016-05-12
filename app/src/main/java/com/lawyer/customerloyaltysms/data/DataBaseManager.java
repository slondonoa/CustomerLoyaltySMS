package com.lawyer.customerloyaltysms.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lawyer.customerloyaltysms.entities.Customer_entity;

import java.util.ArrayList;
import java.util.List;

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
    public static String CN_filtered = "Filtered";

    public static final String CREATE_TABLE_SENDSMS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SENDSMS + "(" +
            CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CN_IdPerson + " TEXT, " +
            CN_Name + " TEXT, " +
            CN_LastName + " TEXT, " +
            CN_Document + " TEXT, " +
            CN_Cell1 + " TEXT, " +
            CN_Cell2 + " TEXT, " +
            CN_Cell3 + " TEXT, " +
            CN_sent + " INTEGER, " +
            CN_filtered + " INTEGER);";

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

    //sedebe actualizar con la configuracion de filtros
    //db.execSQL("UPDATE Usuarios SET nombre='usunuevo' WHERE codigo=6 ");

    public List<Customer_entity> getCustomerSMS(String filter) {
        List<Customer_entity> CustomerList = new ArrayList<Customer_entity>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_SENDSMS; //  +" "+ filter; //+ " LIMIT 5; ";
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_entity customer = new Customer_entity();
                customer.Cell1=(cursor.getString(cursor.getColumnIndex(CN_Cell1)));
                customer.Cell2=(cursor.getString(cursor.getColumnIndex(CN_Cell2)));
                customer.Cell3=(cursor.getString(cursor.getColumnIndex(CN_Cell3)));
                customer.Document=(cursor.getString(cursor.getColumnIndex(CN_Document)));
                customer.IdPerson=(cursor.getString(cursor.getColumnIndex(CN_IdPerson)));
                customer.LastName=(cursor.getString(cursor.getColumnIndex(CN_LastName)));
                customer.Name=(cursor.getString(cursor.getColumnIndex(CN_Name)));
                customer.Sent=(cursor.getString(cursor.getColumnIndex(CN_sent)));
                // Adding customer to list
                CustomerList.add(customer);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // return contact list
        return CustomerList;
    }

    public  void InsertCostumers(List<Customer_entity> lstcustomers)
    {
        long v=0;
        db.delete(TABLE_NAME_SENDSMS, null, null);
        for (Customer_entity customer:lstcustomers) {
            v= db.insert(TABLE_NAME_SENDSMS, null, ContentValuesCustomer(customer));
            long g=v;
        }
    }


    public ContentValues ContentValuesCustomer(Customer_entity customer)
    {
        ContentValues values=new ContentValues();
        values.put(CN_IdPerson ,customer.IdPerson);
        values.put(CN_Name,customer.Name);
        values.put(CN_LastName,customer.LastName);
        values.put(CN_Document,customer.Document);
        values.put(CN_Cell1,customer.Cell1);
        values.put(CN_Cell2 ,customer.Cell2);
        values.put(CN_Cell3,customer.Cell3);
        values.put(CN_sent,customer.Sent);
        values.put(CN_filtered, customer.Filtered);
        return values;
    }

}
