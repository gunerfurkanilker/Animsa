package com.example.demo.a4kelime1cevap.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo.a4kelime1cevap.R;
import com.example.demo.a4kelime1cevap.controller.OyunButtonlari;
import com.example.demo.a4kelime1cevap.model.VeritabaniErisim;
import com.google.android.gms.ads.MobileAds;

public class OyunActivity extends AppCompatActivity {

    private Button btnSecenek1,btnSecenek2,btnSecenek3,btnSecenek4;
    private Button btnYenidenOyna,btnAnaMenu;

    private OyunButtonlari oyunButtonListener;
    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyun);
        init();
        registerHandlers();
        MobileAds.initialize(this,"ca-app-pub-8569643829436656~8449432324");
    }

    private void init(){

        dialog = new Dialog(this,R.style.Theme_Dialog);
        dialog.setContentView(R.layout.oyun_sonu);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        btnSecenek1 = (Button) findViewById(R.id.btnSecenek1);
        btnSecenek2 = (Button) findViewById(R.id.btnSecenek2);
        btnSecenek3 = (Button) findViewById(R.id.btnSecenek3);
        btnSecenek4 = (Button) findViewById(R.id.btnSecenek4);
        btnYenidenOyna = (Button) dialog.findViewById(R.id.btnYeniOyun);
        btnAnaMenu = (Button) dialog.findViewById(R.id.btnAnaMenu);



        oyunButtonListener = new OyunButtonlari(this,dialog);





    }

    private void registerHandlers(){

        btnSecenek1.setOnClickListener(oyunButtonListener);
        btnSecenek2.setOnClickListener(oyunButtonListener);
        btnSecenek3.setOnClickListener(oyunButtonListener);
        btnSecenek4.setOnClickListener(oyunButtonListener);
        btnYenidenOyna.setOnClickListener(oyunButtonListener);
        btnAnaMenu.setOnClickListener(oyunButtonListener);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Çıkmak istediğinizden emin misiniz ? Kazandığınız puanlar kaybolacaktır. ")
                .setCancelable(false)
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OyunActivity.this.finish();
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
