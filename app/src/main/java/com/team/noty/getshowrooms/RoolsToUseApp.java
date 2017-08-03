package com.team.noty.getshowrooms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class RoolsToUseApp extends AppCompatActivity implements View.OnClickListener {

    AppCompatImageView back, getshowroom, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rools_to_use_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        back = (AppCompatImageView) findViewById(R.id.back);
        getshowroom = (AppCompatImageView) findViewById(R.id.getShowroom);

        back.setOnClickListener(this);
        getshowroom.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.getShowroom:
                finish();
                break;
        }
    }

}
