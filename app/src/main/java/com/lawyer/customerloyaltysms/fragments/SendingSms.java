package com.lawyer.customerloyaltysms.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lawyer.customerloyaltysms.R;
import com.lawyer.customerloyaltysms.activity.filter;
import com.lawyer.customerloyaltysms.data.DataBaseManager;
import com.lawyer.customerloyaltysms.entities.Customer_entity;
import com.lawyer.customerloyaltysms.entities.FilterSMS_entity;
import com.lawyer.customerloyaltysms.entities.ProcessSMS_entity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendingSms extends Fragment{
    private DataBaseManager manager;
    TextView txtMessage;
    PhoneStateListener listener;

    private TextInputLayout textInputLayoutMessage;

    public static SendingSms newInstance() {
        return new SendingSms();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_sending_sms, container, false);

        TextView txtNumberOfCustomers= (TextView) view.findViewById(R.id.txtNumberOfCustomers);
        TextView txtDescriptionOfFilter= (TextView)  view.findViewById(R.id.txtDescriptionOfFilter);
        txtMessage= (TextView)  view.findViewById(R.id.txtMessage);
        textInputLayoutMessage=(TextInputLayout) view.findViewById(R.id.textInputLayoutMessage);
        manager=new DataBaseManager(getActivity());
        manager.Open(getActivity());
        int numberOfCustomers=manager.getCountCustomerSMS();
        String strnumberOfCustomers=Integer.toString(numberOfCustomers);
        txtNumberOfCustomers.setText(strnumberOfCustomers);
        FilterSMS_entity filterSMS_entity;
        filterSMS_entity=manager.getFilterSMS();
        String descriptionFilter="";
        if (filterSMS_entity.FilterDescription!="" && filterSMS_entity.FilterDescription!= null)
        {
            descriptionFilter=filterSMS_entity.FilterDescription;
        }else
        {
            descriptionFilter="Sin información";
        }
        txtDescriptionOfFilter.setText(descriptionFilter);


        Button btnlast=(Button) view.findViewById(R.id.butSending);
        btnlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateMessage()) {
                    ProcessSMS_entity processSMS = manager.getProcessSMSActive();
                    if (processSMS.Active != null) {
                        if (processSMS.Active == 1) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Proceso de envío activo")
                                    .setMessage("El proceso creado en la fecha " + processSMS.DateProcess + " del cual se han enviado" + processSMS.SentSMS + " se encuentra activo. Desea finalizar este proceso?.")
                                    .setPositiveButton("Si",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    manager.updateProcessSMS(0);
                                                    SendingSMS();
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

                    } else {
                        SendingSMS();
                    }
                }else {
                    return;
                }


            }
        });

        manager.Close(getActivity());
        return view;
    }

    public void SendingSMS() {
        String message= txtMessage.getText().toString();
            manager=new DataBaseManager(getActivity());
            manager.Open(getActivity());
            int numberOfCustomers=manager.getCountCustomerSMS();
            String datenow= DateFormat.getDateTimeInstance().format(new Date());
            if(numberOfCustomers==0)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Numero de clientes")
                        .setMessage("El numero de clientes para el envio de mensajes es cero desea cambiar el filtro para el envío?.")
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), filter.class);
                                        startActivity(intent);
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

            }else
            {
                //insertar el proceso
                //
                ProcessSMS_entity processSMS_entity=new ProcessSMS_entity();
                processSMS_entity.Active=1;
                processSMS_entity.Filtered=numberOfCustomers;
                processSMS_entity.Message=message;
                processSMS_entity.DateProcess=datenow;
                processSMS_entity.SentSMS=0;
                manager.InsertProcessSMS(processSMS_entity);

                //consulta de 1 cliente para enviar
                //meter ciclo con un hilo cada 20 segundos para enviar el mensaje
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (inCall())
                            {
                                Thread.sleep(3000);

                            }
                            // Do some stuff
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });

            }
        manager.Close(getActivity());

    }

    private boolean validateMessage() {
        boolean validate=false;
        if (txtMessage.getText().toString().trim().isEmpty()) {
            textInputLayoutMessage.setError(getString(R.string.err_msg));
        } else {
            textInputLayoutMessage.setErrorEnabled(false);
            validate= true;
        }
        return  validate;
    }

    private boolean inCall()
    {
        final boolean[] incall = new boolean[1];
        listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    incall[0] =false;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    incall[0] =true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    incall[0] =true;
                    break;
            }

        }};
        return incall[0];
    }


}
