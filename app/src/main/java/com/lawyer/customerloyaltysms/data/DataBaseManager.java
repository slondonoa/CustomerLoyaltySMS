package com.lawyer.customerloyaltysms.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lawyer.customerloyaltysms.data.definitions.CustomersSMS;
import com.lawyer.customerloyaltysms.data.definitions.FilterSMS;
import com.lawyer.customerloyaltysms.data.definitions.ProcessSMS;
import com.lawyer.customerloyaltysms.entities.Customer_entity;
import com.lawyer.customerloyaltysms.entities.ProcessSMS_entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stiven on 5/9/2016.
 */
public class DataBaseManager {

    public static final String CREATE_TABLE_SENDSMS = "CREATE TABLE IF NOT EXISTS " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS + "(" +
            CustomersSMS.CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CustomersSMS.CN_IdPerson + " TEXT, " +
            CustomersSMS.CN_Name + " TEXT, " +
            CustomersSMS.CN_LastName + " TEXT, " +
            CustomersSMS.CN_Document + " TEXT, " +
            CustomersSMS.CN_Cell1 + " TEXT, " +
            CustomersSMS.CN_Cell2 + " TEXT, " +
            CustomersSMS.CN_Cell3 + " TEXT, " +
            CustomersSMS.CN_sent + " INTEGER, " +
            CustomersSMS.CN_filtered + " INTEGER);";


    public static final String CREATE_TABLE_PROCESSSMS = "CREATE TABLE IF NOT EXISTS " + ProcessSMS.TABLE_NAME_PROCESSSMS + "(" +
            ProcessSMS.CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProcessSMS.CN_DateProcess + " TEXT, " +
            ProcessSMS.CN_Filter + " TEXT, " +
            ProcessSMS.CN_Message + " TEXT, " +
            ProcessSMS.CN_SentSMS + " INTEGER, " +
            ProcessSMS.CN_Active + " INTEGER);";

    public static final String CREATE_TABLE_FILTERSMS = "CREATE TABLE IF NOT EXISTS " + FilterSMS.TABLE_NAME_FILTERSMS + "(" +
            FilterSMS.CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FilterSMS.CN_FilteredCustomer + " TEXT, " +
            FilterSMS.CN_FilterDescription + " TEXT);";


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

    public int getCountCustomer() {
        int count=0;
        String selectQuery = "SELECT COUNT(*) as count FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS;
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                count=Integer.parseInt(cursor.getString(cursor.getColumnIndex("count")));
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // return contact list
        return count;
    }

    public int getCountCustomerSMS() {
        int count=0;
        String selectQuery = "SELECT COUNT(*) as count FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS +" WHERE " + CustomersSMS.CN_filtered + "=1";
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                count=Integer.parseInt(cursor.getString(cursor.getColumnIndex("count")));
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // return contact list
        return count;
    }



    public List<Customer_entity> getCustomerSMS() {
        List<Customer_entity> CustomerList = new ArrayList<Customer_entity>();
        String selectQuery = "SELECT * FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS +" WHERE " + CustomersSMS.CN_filtered + "=1";
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_entity customer = new Customer_entity();
                customer.Cell1=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Cell1)));
                customer.Cell2=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Cell2)));
                customer.Cell3=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Cell3)));
                customer.Document=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Document)));
                customer.IdPerson=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_IdPerson)));
                customer.LastName=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_LastName)));
                customer.Name=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Name)));
                customer.Sent=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_sent)));
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
        //actualizarlos procesos activos
        db.delete(CustomersSMS.TABLE_NAME_CUSTOMERSSMS, null, null);
        updateProcessSMS();
        for (Customer_entity customer:lstcustomers) {
            v= db.insert(CustomersSMS.TABLE_NAME_CUSTOMERSSMS, null, ContentValuesCustomer(customer));
            long g=v;
        }
    }


    public ContentValues ContentValuesCustomer(Customer_entity customer)
    {
        ContentValues values=new ContentValues();
        values.put(CustomersSMS.CN_IdPerson ,customer.IdPerson);
        values.put(CustomersSMS.CN_Name,customer.Name);
        values.put(CustomersSMS.CN_LastName,customer.LastName);
        values.put(CustomersSMS.CN_Document,customer.Document);
        values.put(CustomersSMS.CN_Cell1,customer.Cell1);
        values.put(CustomersSMS.CN_Cell2 ,customer.Cell2);
        values.put(CustomersSMS.CN_Cell3,customer.Cell3);
        values.put(CustomersSMS.CN_sent,customer.Sent);
        values.put(CustomersSMS.CN_filtered, customer.Filtered);
        return values;
    }

    public ContentValues ContentValuesProcessSMS(ProcessSMS_entity process)
    {
        ContentValues values=new ContentValues();
        values.put(ProcessSMS.CN_Active ,process.Active);
        values.put(ProcessSMS.CN_DateProcess ,process.DateProcess);
        values.put(ProcessSMS.CN_Filter ,process.Filter);
        values.put(ProcessSMS.CN_Message ,process.Message);
        values.put(ProcessSMS.CN_SentSMS ,process.SentSMS);
        return values;
    }

    public  void InsertProcessSMS(ProcessSMS_entity processSMS)
    {
        long v=0;
        v= db.insert(ProcessSMS.TABLE_NAME_PROCESSSMS, null, ContentValuesProcessSMS(processSMS));
    }

    public ProcessSMS_entity getProcessSMSActive() {
        ProcessSMS_entity processSMS = new ProcessSMS_entity();
        String selectQuery = "SELECT * FROM " + ProcessSMS.TABLE_NAME_PROCESSSMS +" WHERE " + ProcessSMS.CN_Active + "=1";
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                processSMS.Id= Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_ID)));
                processSMS.DateProcess=(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_DateProcess)));
                processSMS.Filter=(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Filter)));
                processSMS.Message=(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Message)));
                processSMS.SentSMS=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_SentSMS)));
                processSMS.Active=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Active)));
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // return contact obj
        return processSMS;
    }

    public List<ProcessSMS_entity> getProcessSMS() {
        List<ProcessSMS_entity> lstrocessSMS=new ArrayList<ProcessSMS_entity>();
        String selectQuery = "SELECT * FROM " + ProcessSMS.TABLE_NAME_PROCESSSMS;
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProcessSMS_entity processSMS = new ProcessSMS_entity();
                processSMS.Id= Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_ID)));
                processSMS.DateProcess=(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_DateProcess)));
                processSMS.Filter=(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Filter)));
                processSMS.Message=(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Message)));
                processSMS.SentSMS=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_SentSMS)));
                processSMS.Active=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Active)));
                lstrocessSMS.add(processSMS);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // return contact obj
        return lstrocessSMS;
    }


    public void updateProcessSMS() {
        ContentValues values = new ContentValues();
        values.put(ProcessSMS.CN_Active, 0);
        List<ProcessSMS_entity> lstProcessSMS=getProcessSMS();
        for (ProcessSMS_entity process:lstProcessSMS) {
            db.update(ProcessSMS.TABLE_NAME_PROCESSSMS, values, ProcessSMS.CN_ID + "= ?", new String[]{String.valueOf(process.Id)});
        }

    }

}
