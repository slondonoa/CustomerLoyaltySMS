package com.lawyer.customerloyaltysms.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lawyer.customerloyaltysms.data.definitions.CustomersSMS;
import com.lawyer.customerloyaltysms.data.definitions.FilterSMS;
import com.lawyer.customerloyaltysms.data.definitions.ProcessSMS;
import com.lawyer.customerloyaltysms.entities.Customer_entity;
import com.lawyer.customerloyaltysms.entities.FilterSMS_entity;
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
            CustomersSMS.CN_Sex + " TEXT, " +
            CustomersSMS.CN_birthdate + " TEXT, " +
            CustomersSMS.CN_processed + " TEXT, " +
            CustomersSMS.CN_clientstatus + " TEXT, " +
            CustomersSMS.CN_ProcessStatus + " TEXT, " +
            CustomersSMS.CN_sent + " INTEGER, " +
            CustomersSMS.CN_filtered + " INTEGER);";


    public static final String CREATE_TABLE_PROCESSSMS = "CREATE TABLE IF NOT EXISTS " + ProcessSMS.TABLE_NAME_PROCESSSMS + "(" +
            ProcessSMS.CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProcessSMS.CN_DateProcess + " TEXT, " +
            ProcessSMS.CN_Filtered + " TEXT, " +
            ProcessSMS.CN_Message + " TEXT, " +
            ProcessSMS.CN_SentSMS + " INTEGER, " +
            ProcessSMS.CN_Active + " INTEGER);";

    public static final String CREATE_TABLE_FILTERSMS = "CREATE TABLE IF NOT EXISTS " + FilterSMS.TABLE_NAME_FILTERSMS + "(" +
            FilterSMS.CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FilterSMS.CN_FilterCustomer + " TEXT, " +
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

    //metodo usado para obtener todos los clientes
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

    //metodo usado para obtner el numero de clientes filtados
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

    public int getCountCustomerSentSMS() {
        int count=0;
        String selectQuery = "SELECT COUNT(*) as count FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS +" WHERE " + CustomersSMS.CN_sent + "=1 and " + CustomersSMS.CN_filtered +"=1";
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

    //metodo para obtener los clientes que estan filtrados
    public List<Customer_entity> getCustomerSMS() {
        List<Customer_entity> CustomerList = new ArrayList<Customer_entity>();
        String selectQuery = "SELECT * FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS +" WHERE " + CustomersSMS.CN_filtered + "=1";
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_entity customer = new Customer_entity();
                customer.Id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_ID)));
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

    //metodo utilizado para consultar el cliente al cual se le va a enviar un mensaje
    public Customer_entity getCustomerToSendMessage() {
        Customer_entity customer = null;
        String selectQuery = "SELECT * FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS +" WHERE " + CustomersSMS.CN_sent + "=0 and " + CustomersSMS.CN_filtered + "=1 LIMIT 1;";
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                customer = new Customer_entity();
                customer.Id =Integer.parseInt(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_ID)));
                customer.Cell1=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Cell1)));
                customer.Cell2=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Cell2)));
                customer.Cell3=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Cell3)));
                customer.Document=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Document)));
                customer.IdPerson=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_IdPerson)));
                customer.LastName=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_LastName)));
                customer.Name=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_Name)));
                customer.Sent=(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_sent)));
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // return contact list
        return customer;
    }


    //marcar clientes filtrados
    public int CustomerFilteredMark(FilterSMS_entity filter) {
        //actualizacion de tabla de clientes para poner a todos los clientes como no filtrados
        UpdateNoFilteredCustomerSMS();
        //actualiza todos los procesos de envios como inactivos
        updateProcessSMS(0);
        int count=0;

        String selectQuery = "SELECT * FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS + " "+ filter.getFilterCustomer();
        Cursor cursor =db.rawQuery(selectQuery, null);
        boolean generate=false;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                count++;
                //marcar cliente como filtrado
                int id=Integer.parseInt(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_ID)));
                updateFilteredCustomer(id,1);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            generate=true;
        }
        if (generate)
        {
            //borrar los filtros existenes
            db.delete(FilterSMS.TABLE_NAME_FILTERSMS, null, null);
            //guardar el nuevo filtro
            InsertFilterSMS(filter);
        }
        // return contact list
        return count;
    }


    //metodo usado para insertar los clientes
    public  void InsertCostumers(List<Customer_entity> lstcustomers)
    {
        long v=0;
        //actualizarlos procesos activos
        db.delete(CustomersSMS.TABLE_NAME_CUSTOMERSSMS, null, null);
        updateProcessSMS(0);
        db.delete(FilterSMS.TABLE_NAME_FILTERSMS, null, null);
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
        values.put(CustomersSMS.CN_Sex,customer.Sex);
        values.put(CustomersSMS.CN_birthdate,customer.birthdate);
        values.put(CustomersSMS.CN_processed,customer.processed);
        values.put(CustomersSMS.CN_clientstatus,customer.clientstatus);
        values.put(CustomersSMS.CN_ProcessStatus,customer.ProcessStatus);
        values.put(CustomersSMS.CN_sent,customer.Sent);
        values.put(CustomersSMS.CN_filtered, customer.Filtered);
        return values;
    }

    public ContentValues ContentValuesProcessSMS(ProcessSMS_entity process)
    {
        ContentValues values=new ContentValues();
        values.put(ProcessSMS.CN_Active ,process.Active);
        values.put(ProcessSMS.CN_DateProcess ,process.DateProcess);
        values.put(ProcessSMS.CN_Filtered ,process.Filtered);
        values.put(ProcessSMS.CN_Message ,process.Message);
        values.put(ProcessSMS.CN_SentSMS ,process.SentSMS);
        return values;
    }

    public ContentValues ContentValuesFilterSMS(FilterSMS_entity filterSMS_entity)
    {
        ContentValues values=new ContentValues();
        values.put(FilterSMS.CN_FilterCustomer ,filterSMS_entity.getFilterCustomer());
        values.put(FilterSMS.CN_FilterDescription ,filterSMS_entity.getFilterDescription());
        return values;
    }

    //metodo usado para ionsertar el filtro generado
    public  void InsertFilterSMS(FilterSMS_entity filterSMS_entity)
    {
        long v=0;
        v= db.insert(FilterSMS.TABLE_NAME_FILTERSMS, null, ContentValuesFilterSMS(filterSMS_entity));
    }

    //metodo usado para obtener el filtro actual
    public FilterSMS_entity getFilterSMS() {
        FilterSMS_entity filterSMS = new FilterSMS_entity();
        String selectQuery = "SELECT * FROM " + FilterSMS.TABLE_NAME_FILTERSMS;
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                filterSMS.FilterCustomer=(cursor.getString(cursor.getColumnIndex(FilterSMS.CN_FilterCustomer)));
                filterSMS.FilterDescription=(cursor.getString(cursor.getColumnIndex(FilterSMS.CN_FilterDescription)));
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // return contact obj
        return filterSMS;
    }

    //metodo usado para insertar el proceso de envio de sms
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
                processSMS.Filtered=Integer.parseInt((cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Filtered))));
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
                processSMS.Filtered=Integer.parseInt((cursor.getString(cursor.getColumnIndex(ProcessSMS.CN_Filtered))));
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

    //metodo utilizado para actualizar los procesos de envio
    public void updateProcessSMS(int active) {
        ContentValues values = new ContentValues();
        values.put(ProcessSMS.CN_Active, active);
        List<ProcessSMS_entity> lstProcessSMS=getProcessSMS();
        for (ProcessSMS_entity process:lstProcessSMS) {
            db.update(ProcessSMS.TABLE_NAME_PROCESSSMS, values, ProcessSMS.CN_ID + "= ?", new String[]{String.valueOf(process.Id)});
        }
    }

    //metodo para actualizar los clientes filtrados
    public void updateFilteredCustomer(int customerId, int filter) {
        ContentValues values = new ContentValues();
        values.put(CustomersSMS.CN_filtered, filter);
        values.put(CustomersSMS.CN_sent, 0);
        db.update(CustomersSMS.TABLE_NAME_CUSTOMERSSMS, values, CustomersSMS.CN_ID + "= ?", new String[]{String.valueOf(customerId)});

    }

    //metodo usado para poner a los clientes como no filtrados
    public void UpdateNoFilteredCustomerSMS() {
        String selectQuery = "SELECT * FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS;
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int id=Integer.parseInt(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_ID)));
                updateFilteredCustomer(id,0);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    //metodo usado para el esatdo de envio de todos los clientes
    public void UpdateSentCustomerSMS(int sent) {
        String selectQuery = "SELECT * FROM " + CustomersSMS.TABLE_NAME_CUSTOMERSSMS;
        Cursor cursor =db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int id=Integer.parseInt(cursor.getString(cursor.getColumnIndex(CustomersSMS.CN_ID)));
                updateSentCustomer(id,sent);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    //metodo para actualizar el estado de enviado de un cliente
    public void updateSentCustomer(int customerId, int sent) {
        ContentValues values = new ContentValues();
        values.put(CustomersSMS.CN_sent, sent);
        db.update(CustomersSMS.TABLE_NAME_CUSTOMERSSMS, values, CustomersSMS.CN_ID + "= ?", new String[]{String.valueOf(customerId)});

    }

    //metodo para actualizar en la tabla de proceso cuantos mensajes se han enviado
    public void updateProcessSent(int idProcess, int sentSMS) {
        ContentValues values = new ContentValues();
        values.put(ProcessSMS.CN_SentSMS, sentSMS);
        db.update(ProcessSMS.TABLE_NAME_PROCESSSMS, values, ProcessSMS.CN_ID + "= ?", new String[]{String.valueOf(idProcess)});

    }


}
