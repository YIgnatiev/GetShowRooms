package com.team.noty.getshowrooms;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.team.noty.getshowrooms.customTextView.TextViewPlus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView hanger, beside, top, liked, search;
    LinearLayout lineContent;

    TextViewPlus conditions;

    public static void showNetDisabledAlertToUser(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("На Вашем телефоне - нет соеденения с интернетом. Хотите его включить?")
                .setTitle("Нет соеденения!!!")
                .setCancelable(false)
                .setPositiveButton(" Настройки ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent dialogIntent = new
                                        Intent(android.provider.Settings.ACTION_SETTINGS);

                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(dialogIntent);
                            }
                        });

        alertDialogBuilder.setNegativeButton("Выход", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        alert.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
        alert.getButton(Dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));

    }

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hanger = (ImageView) findViewById(R.id.hanger);
        beside = (ImageView) findViewById(R.id.beside);
        top = (ImageView) findViewById(R.id.top);
        liked = (ImageView) findViewById(R.id.liked);
        search = (ImageView) findViewById(R.id.search);

        conditions = (TextViewPlus) findViewById(R.id.conditions);

        beside.setOnClickListener(this);
        top.setOnClickListener(this);
        liked.setOnClickListener(this);
        search.setOnClickListener(this);
        conditions.setOnClickListener(this);

        lineContent = (LinearLayout) findViewById(R.id.lineContent);

        hanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
                intent.putExtra("tab", 0);
                startActivity(intent);
            }
        });

        if (!hasConnection(MainActivity.this)) {
            showNetDisabledAlertToUser(this);
        }


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.beside:
                intent = new Intent(MainActivity.this, FragmentActivity.class);
                intent.putExtra("tab", 0);
                break;
            case R.id.top:
                intent = new Intent(MainActivity.this, FragmentActivity.class);
                intent.putExtra("tab", 1);
                break;
            case R.id.liked:
                intent = new Intent(MainActivity.this, FragmentActivity.class);
                intent.putExtra("tab", 2);
                break;
            case R.id.search:
                intent = new Intent(MainActivity.this, FragmentActivity.class);
                intent.putExtra("tab", 3);
                break;
            case R.id.conditions:
                intent = new Intent(MainActivity.this, RoolsToUseApp.class);
                break;
        }
        startActivity(intent);
    }
}
