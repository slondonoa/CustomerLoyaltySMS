package com.lawyer.customerloyaltysms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lawyer.customerloyaltysms.R;
import com.lawyer.customerloyaltysms.data.DataBaseManager;
import com.lawyer.customerloyaltysms.entities.BirthdaySMS_entity;
import com.lawyer.customerloyaltysms.entities.Customer_entity;
import com.lawyer.customerloyaltysms.entities.ProcessSMS_entity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Birthday extends Fragment {
    private DataBaseManager manager;
    Thread SMSthreadB =null;
    TextView txtNumberOfCustomers;

    public Birthday() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_birthday, container, false);
        txtNumberOfCustomers= (TextView) view.findViewById(R.id.txtNumberOfCustomers);

        manager=new DataBaseManager(getActivity());
        manager.Open(getActivity());
        int[] sent=manager.ValidateBirthdaySMS();
        if (sent[0]==0 && sent[1]==0)
        {
            manager.getBirthdaySMS();
        }
        sent=manager.ValidateBirthdaySMS();

        if (sent[1] > 0)
        {
            Runnable  runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        manager.Open(getContext());

                        int[] sent=manager.ValidateBirthdaySMS();

                        while (sent[1] > 0)
                        {
                            TelephonyManager telephony = (TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE);

                            int state=telephony.getCallState();
                            final boolean[] incall = new boolean[1];
                            switch (state) {
                                case TelephonyManager.CALL_STATE_IDLE:
                                    incall[0] = false;
                                    break;
                                case TelephonyManager.CALL_STATE_OFFHOOK:
                                    incall[0] = true;
                                    break;
                                case TelephonyManager.CALL_STATE_RINGING:
                                    incall[0] = true;
                                    break;
                            }
                            while (incall[0]) {
                                state=telephony.getCallState();
                                switch (state) {
                                    case TelephonyManager.CALL_STATE_IDLE:
                                        incall[0] = false;
                                        break;
                                    case TelephonyManager.CALL_STATE_OFFHOOK:
                                        incall[0] = true;
                                        break;
                                    case TelephonyManager.CALL_STATE_RINGING:
                                        incall[0] = true;
                                        break;
                                }
                            }


                            //consulta de 1 cliente para enviar mensaje
                            BirthdaySMS_entity customer_entity= manager.getBirthdayToSendMessage();

                            //enviar mensaje
                            String strPhone = customer_entity.Cell1;
                            String strMessage ="Abogados pensiones medellin te desea un FELIZ CUMPLEAÑOS!!!";
                            SmsManager sms = SmsManager.getDefault();
                            ArrayList messageParts = sms.divideMessage(strMessage);

                            sms.sendMultipartTextMessage(strPhone, null, messageParts, null, null);

                            //actualizar estado de enviado del clieente
                            manager.updateSentBirthday(customer_entity.Id,1);


                            sent=manager.ValidateBirthdaySMS();


                            //TextView txtNumberOfCustomers= (TextView) getView().findViewById(R.id.txtNumberOfCustomers);
                            int numberOfCustomers=manager.getCountBirthdaySMS();
                            final int sentTmp=numberOfCustomers-sent[1];
                            final String strnumberOfCustomers=Integer.toString(numberOfCustomers);

                            //txtNumberOfCustomers.setText(Html.fromHtml(strnumberOfCustomers +" / <font color='#64DD17'> "+sentTmp+"</font>"));
                            txtNumberOfCustomers.post(new Runnable() {
                                public void run() {
                                    txtNumberOfCustomers.setText(Html.fromHtml(strnumberOfCustomers +" / <font color='#64DD17'> "+sentTmp+"</font>"));
                                }
                            });

                                /*
                                handler.post(new Runnable(){
                                    public void run() {

                                    }});
                                    */

                            //meter ciclo con un hilo cada 20 segundos para enviar el mensaje
                            Thread.sleep(20000);
                        }
                        if(SMSthreadB!=null) {
                            SMSthreadB.interrupt();
                        }
                        manager.Close(getContext());
                        //actualizar en el proceso el numero de mensajes enviados

                        // Do some stuff
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                }
            };

            SMSthreadB = new Thread(runnable);
            SMSthreadB.start();

        }else if (sent[0]>0)
        {
           txtNumberOfCustomers.setText(Html.fromHtml(sent[0] +" / <font color='#64DD17'> "+sent[0]+"</font>"));
        }

        return view;
    }

}
