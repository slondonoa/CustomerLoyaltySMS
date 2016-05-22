package com.lawyer.customerloyaltysms.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.costumers.lawyer.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SendingSms extends Fragment {


    public SendingSms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sending_sms, container, false);
    }

}
