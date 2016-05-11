package com.lawyer.customerloyaltysms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.lawyer.customerloyaltysms.data.DataBaseManager;
import com.lawyer.customerloyaltysms.entities.Customer_entity;
import com.lawyer.customerloyaltysms.fragments.Customers;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restService = new RestService();

        manager=new DataBaseManager(this);
        List<Customer_entity> lstperson=manager.getCustomerSMS("");
        if (lstperson.size()<1)
        {
            refreshCostumers();
        }else {
            countCostumers=lstperson.size();
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
        adapter.addFrag(new Fragment(), "Envió de mensajes");
        adapter.addFrag(new Fragment(), "Mensajes de cumpleaños");
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


}
