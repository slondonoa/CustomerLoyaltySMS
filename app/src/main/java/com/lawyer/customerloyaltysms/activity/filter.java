package com.lawyer.customerloyaltysms.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.lawyer.customerloyaltysms.MainActivity;
import com.lawyer.customerloyaltysms.R;
import com.lawyer.customerloyaltysms.data.DataBaseManager;
import com.lawyer.customerloyaltysms.entities.FilterSMS_entity;
import com.lawyer.customerloyaltysms.entities.ProcessSMS_entity;

import java.util.Calendar;

public class filter extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private DataBaseManager manager;
    ProgressDialog dialog =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Gestión de filtros");
        setSupportActionBar(toolbar);

        Button btnFilter=(Button) findViewById(R.id.butFilter);
        btnFilter.setOnClickListener(this);

    }

    public void onClick(final View v) {
        manager=new DataBaseManager(v.getContext());
        manager.Open(v.getContext());
        ProcessSMS_entity processSMS=manager.getProcessSMSActive();
        manager.Close(v.getContext());
        if (processSMS!=null && processSMS.Active!=null)
        {
            if(processSMS.Active==1) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Proceso de envío activo")
                        .setMessage("El proceso creado en la fecha "+ processSMS.DateProcess + " del cual se han enviado " + processSMS.SentSMS + " se encuentra activo. Desea finalizar este proceso?.")
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogM, int which) {
                                        dialog = ProgressDialog.show(filter.this, "",
                                                "Filtrando clientes. Por favor espere...", true);
                                        new Thread(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        filter(v);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (dialog != null) {
                                                                    dialog.cancel();
                                                                }
                                                                finish();
                                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                                startActivity(intent);
                                                                Thread.currentThread().interrupt();
                                                                dialogM.cancel();
                                                            }
                                                        });
                                                    }
                                                }
                                        ).start();

                                    }
                                })
                        .setNegativeButton("No",
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
            dialog = ProgressDialog.show(this, "",
                    "Filtrando clientes. Por favor espere...", true);

            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            filter(v);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (dialog != null) {
                                        dialog.cancel();
                                    }
                                    finish();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                    Thread.currentThread().interrupt();
                                }
                            });
                        }
                    }
            ).start();
        }

    }
    private  void filter(View v)
    {

        Spinner spCostumersatatus =(Spinner) findViewById(R.id.spclientsatatus);
        String strCostumerStatus = spCostumersatatus.getSelectedItem().toString();

        Spinner spProcessstatus = (Spinner) findViewById(R.id.spprocessstatus);
        String strProcessstatus = spProcessstatus.getSelectedItem().toString();

        Spinner spProcessed = (Spinner) findViewById(R.id.spProcessed);
        String strProcessed = spProcessed.getSelectedItem().toString();

        RadioGroup radiogroup =  (RadioGroup) findViewById(R.id.radioButtonGroup);
        int selectedId = radiogroup .getCheckedRadioButtonId();
        String radio=null;
        if (selectedId>=0)
        {
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            radio=radioButton.getText().toString();
        }

        String where= "WHERE";
        String descriptionWhere="";
        boolean generate=false;
        boolean and=false;

        if ( spProcessstatus.getSelectedItemPosition() > 0)
        {
            descriptionWhere= descriptionWhere + " - Estado del proceso: "+ strProcessstatus;
            generate=true;
            if (and){
                where= where + " " + " AND (ProcessStatus like '%" + strProcessstatus+"%' or ProcessStatus like '" + strProcessstatus+"%' or ProcessStatus like '%" + strProcessstatus+"' or ProcessStatus like '" + strProcessstatus+"')" ;
            }else {
                where= where + " " + "(ProcessStatus like '%" + strProcessstatus+"%' or ProcessStatus like '" + strProcessstatus+"%' or ProcessStatus like '%" + strProcessstatus+"' or ProcessStatus like '" + strProcessstatus+"')" ;
            }
            and=true;
        }

        if ( spCostumersatatus.getSelectedItemPosition() > 0)
        {
            descriptionWhere= descriptionWhere + " - Estado del cliente: "+ strCostumerStatus;
            generate=true;
            if (and) {
                where = where + " " + " AND (clientstatus like '%" + strCostumerStatus + "%' or clientstatus like '" + strCostumerStatus + "%' or clientstatus like '%" + strCostumerStatus + "' or clientstatus like '" + strCostumerStatus + "')";
            }else {
                where = where + " " + "(clientstatus like '%" + strCostumerStatus + "%' or clientstatus like '" + strCostumerStatus + "%' or clientstatus like '%" + strCostumerStatus + "' or clientstatus like '" + strCostumerStatus + "')";
            }
            and=true;
        }
        if ( spProcessed.getSelectedItemPosition() > 0)
        {
            descriptionWhere= descriptionWhere + " - Tramite: "+ strProcessed;
            generate=true;
            if (and) {
                where = where + " " + " AND (processed like '%" + strProcessed + "%' or processed like '" + strProcessed + "%' or processed like '%" + strProcessed + "' or processed like '" + strProcessed + "')";
            }else {
                where = where + " " + "(processed like '%" + strProcessed + "%' or processed like '" + strProcessed + "%' or processed like '%" + strProcessed + "' or processed like '" + strProcessed + "')";
            }
            and=true;
        }
        if (radio!=null) {
            if (radio.equals(getResources().getString(R.string.men))) {
                descriptionWhere = descriptionWhere + " - Sexo: " + radio;
                generate = true;
                if (and) {
                    where = where + " " + " AND (Sex like '%" + radio + "%' or Sex like '" + radio + "%' or Sex like '%" + radio + "' or Sex like '" + radio + "')";
                } else {
                    where = where + " " + "(Sex like '%" + radio + "%' or Sex like '" + radio + "%' or Sex like '%" + radio + "' or Sex like '" + radio + "')";
                }
                and = true;

            } else if (radio.equals(getResources().getString(R.string.woman))) {

                descriptionWhere = descriptionWhere + " - Sexo: " + radio;
                generate = true;
                if (and) {
                    where = where + " " + " AND (Sex like '%" + radio + "%' or Sex like '" + radio + "%' or Sex like '%" + radio + "' or Sex like '" + radio + "')";
                } else {
                    where = where + " " + "(Sex like '%" + radio + "%' or Sex like '" + radio + "%' or Sex like '%" + radio + "' or Sex like '" + radio + "')";
                }
                and = true;
            }
        }
        manager=new DataBaseManager(v.getContext());
        manager.Open(v.getContext());
        FilterSMS_entity filterSMS_entity=new FilterSMS_entity();
        filterSMS_entity.setFilterCustomer(where);
        filterSMS_entity.setFilterDescription(descriptionWhere);
        int countFilteredCustomer=manager.CustomerFilteredMark(filterSMS_entity);
        manager.Close(v.getContext());



    }
}
