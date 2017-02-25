package com.lawyer.customerloyaltysms;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lawyer.customerloyaltysms.activity.filter;
import com.lawyer.customerloyaltysms.data.DataBaseManager;
import com.lawyer.customerloyaltysms.entities.Customer_entity;
import com.lawyer.customerloyaltysms.entities.ProcessSMS_entity;
import com.lawyer.customerloyaltysms.fragments.Birthday;
import com.lawyer.customerloyaltysms.fragments.Customers;
import com.lawyer.customerloyaltysms.fragments.SendingSms;
import com.lawyer.customerloyaltysms.services.CustomerLoyalty_service;
import com.lawyer.customerloyaltysms.services.RestService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    RestService restService;
    private DataBaseManager manager;
    ProgressDialog dialog =null;
    Integer countCostumers;
    Thread Updatethread=null;
    int cont=0;

    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        activity = this;
        restService = new RestService();

        manager=new DataBaseManager(this);
        int countAllCustomer=manager.getCountCustomer();
        if (countAllCustomer<1)
        {
            refreshCostumers();
        }else {
            countCostumers=manager.getCountCustomerSMS();
        }
        manager.Close(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.mipmap.ic_logo);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);  flecha de atras

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if (Updatethread!=null)
        {
            Updatethread.interrupt();
        }
        UpdateList();


        if (!checkPermissionPhoneState()) {

            requestPermissionPhoneState();

        } else {

            Toast.makeText(MainActivity.this, "Tiene permisos para el estado del telefono", Toast.LENGTH_LONG).show();

        }


        if (!checkPermissionSMS()) {

            requestPermissionSMS();

        } else {

            Toast.makeText(MainActivity.this, "Tiene permisos de SMS", Toast.LENGTH_LONG).show();

        }



    }

    private void UpdateList()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                    while (true) {
                        if(cont>0) {
                            int index = viewPager.getCurrentItem();
                            ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
                            /*
                            Fragment fragment = adapter.getItem(0);
                            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.detach(fragment);
                            ft.attach(fragment);
                            ft.commit();
                            /*

                            /*
                            Fragment fragment2 = adapter.getItem(1);
                            final FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.detach(fragment2);
                            ft2.attach(fragment2);
                            ft2.commit();
                            */
                            Thread.sleep(10000);
                        }
                        cont++;
                    }

                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        };

        Updatethread = new Thread(runnable);
        Updatethread.start();

    }

    private void refreshCostumers(){
        //Call to server to grab list of student records. this is a asyn
        try {
            if (conecNetWork()) {
                dialog = ProgressDialog.show(this, "",
                        "Actualizando clientes. Por favor espere...", true);
                restService.getService().getCostumers(new Callback<List<Customer_entity>>() {
                    @Override
                    public void success(List<Customer_entity> customers, Response response) {
                        List<Customer_entity> lstcustomers = customers;
                        countCostumers=lstcustomers.size();
                        manager.Open(MainActivity.this);
                        manager.InsertCostumers(lstcustomers);
                        manager.Close(MainActivity.this);
                        if (dialog != null) {
                            dialog.cancel();
                        }
                        Toast.makeText(MainActivity.this, "Clientes actualizados", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }else {

                showAlertDialog(this,"Conexión a internet","Usted no cuenta con conexión a internet para la actualizacion de clientes",true);
            }
        }catch (Exception e){
            if (dialog != null) {
                dialog.cancel();
            }
            showAlertDialog(this,"Error","No fue posible realizar la actualización de clientes, por favor intentelo de nuevo",true);
        };

    }

    protected Boolean conecNetWork(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (countCostumers!= null)
        {
            adapter.addFrag(new Customers(), "Clientes ("+countCostumers.toString()+")");
        }else {
            adapter.addFrag(new Customers(), "Clientes");
        }
        adapter.addFrag(new SendingSms(), "Envió de mensajes");
        adapter.addFrag(new Birthday(), "Cumpleaños");
        //app:tabMode="scrollable"
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            //se debe validar que no tenga procesos de envio activos para poder actullizar
            final AlertDialog.Builder builderGeneral = new AlertDialog.Builder(this);
            builderGeneral.setTitle("Información")
                    .setMessage("Al usted realizar la actualización de clientes se finalizaran los filtros y procesos de envío de mensajes actualmente activos. Desea continuar con la actualización?")
                    .setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refresh();
                                    if(Updatethread!=null){
                                        Updatethread.interrupt();
                                    }
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
            builderGeneral.create();
            builderGeneral.show();

        }
        if(id == R.id.action_filter){
            Intent intent = new Intent(this, filter.class);
            startActivity(intent);
        }
        if(id == R.id.action_reload){
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    private void  refresh(){

        manager=new DataBaseManager(this);
        ProcessSMS_entity processSMS=manager.getProcessSMSActive();
        manager.Close(this);
        if (processSMS.Active!=null)
        {
            if(processSMS.Active==1) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Proceso de envío activo")
                        .setMessage("El proceso creado en la fecha "+ processSMS.DateProcess + " del cual se han enviado " + processSMS.SentSMS + " mensajes se encuentra activo. Desea finalizar este proceso?.")
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        refreshCostumers();
                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                builder.create();
                builder.show();
            }

        }else {
            refreshCostumers();
        }

    }

    private boolean checkPermissionPhoneState(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermissionPhoneState(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_PHONE_STATE)){

            Toast.makeText(context,"Tiene permisos sobre el estado del telefono",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermissionSMS(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermissionSMS(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.SEND_SMS)){

            Toast.makeText(context,"Tiene permisos sobre envio de SMS",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.SEND_SMS},PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(context,"Permisos generados",Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(context,"Permisos no generados",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
