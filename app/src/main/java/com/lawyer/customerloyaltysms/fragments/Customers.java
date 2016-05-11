package com.lawyer.customerloyaltysms.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lawyer.customerloyaltysms.R;
import com.lawyer.customerloyaltysms.adapters.Customer_RecyclerView_adapter;
import com.lawyer.customerloyaltysms.adapters.Customer_adapter;
import com.lawyer.customerloyaltysms.data.DataBaseManager;
import com.lawyer.customerloyaltysms.entities.Customer_entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Customers extends Fragment {

    public static Customers newInstance() {
        return new Customers();
    }

    private RecyclerView mRecyclerView;
    private List<Customer_adapter> mCustomers;
    private Customer_RecyclerView_adapter mAdapter;
    private DataBaseManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_customers, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCustomers = new ArrayList<>();
        manager = new DataBaseManager(getActivity());
        List<Customer_entity> lstcustomers = manager.getCustomerSMS("");
        manager.Close(getActivity());

        for(Customer_entity customer:lstcustomers) {
            String allname=customer.Name + " " + customer.LastName;

            if(allname.length() > 27) {
                allname = allname.substring(0, 26) + "...";
            }

            String cells="";
            if (customer.Cell1.length()>0)
            {
                cells=cells + customer.Cell1;
            }
            if (customer.Cell2.length()>0)
            {
                cells=cells +"-"+ customer.Cell2;
            }
            if (customer.Cell3.length()>0)
            {
                cells=cells +"-"+ customer.Cell3;
            }

            if(cells.length() > 27) {
                cells = cells.substring(0, 26) + "...";
            }

            Customer_adapter pa=new Customer_adapter(customer.IdPerson,allname.toUpperCase(),cells,customer.Document,customer.Sent);
            mCustomers.add(pa);

        }
        mAdapter = new Customer_RecyclerView_adapter(getActivity(), mCustomers);
        mRecyclerView.setAdapter(mAdapter);

        /*
        mAdapter.setOnItemClickListener(new Customer_RecyclerView_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String Idperson, int position) {
                String message = Idperson;
                Intent intent;
                intent = new Intent(getActivity(), DetailCostumer.class);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
        */


    }

}
