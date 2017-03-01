package com.lawyer.customerloyaltysms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lawyer.customerloyaltysms.R;
import com.lawyer.customerloyaltysms.data.DataBaseManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Birthday extends Fragment {
    private DataBaseManager manager;

    public Birthday() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_birthday, container, false);

        manager=new DataBaseManager(getActivity());
        manager.Open(getActivity());
        manager.getBirthdaySMS();

        return view;
    }

}
