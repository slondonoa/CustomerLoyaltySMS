package com.lawyer.customerloyaltysms.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lawyer.customerloyaltysms.R;

public class filter extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Gestión de filtros");
        setSupportActionBar(toolbar);

    }
}
