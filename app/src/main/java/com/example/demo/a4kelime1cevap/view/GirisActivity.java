package com.example.demo.a4kelime1cevap.view;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo.a4kelime1cevap.R;
import com.example.demo.a4kelime1cevap.controller.AnaMenuButtonlari;

public class GirisActivity extends AppCompatActivity {

    private Button btnOyna,btnCikis,btnAyarlar;
    private TextView txtSeviye,txtGenelPuan,txt4Kelime1Cevap;
    private AnaMenuButtonlari anaMenuButtonlari;
    private SharedPreferences sPreferances;
    private SharedPreferences.Editor editor;
    private int GENEL_PUAN;
    public static final int ACEMI_SINIR = 50000;
    public static final int DENEYIMLI_SINIR = 80000;
    public static final int CIRAK_SINIR = 100000;
    public static final int KALFA_SINIR = 120000;
    public static final int USTA_SINIR = 250000;
    private String OYUNCU_SEVIYE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        init();
        registerHandlers();
    }

    private void init(){

        btnOyna = (Button) findViewById(R.id.btnOyna);
        btnCikis = (Button) findViewById(R.id.btnCikisYap);
        btnAyarlar =(Button) findViewById(R.id.btnAyarlar);

        txtGenelPuan = (TextView) findViewById(R.id.txtGenelPuan);
        txtSeviye = (TextView) findViewById(R.id.txtSeviye);


        anaMenuButtonlari = new AnaMenuButtonlari(this);
        sPreferances =getApplicationContext().getSharedPreferences("oyuncuseviyesi",MODE_PRIVATE);
        puanBelirle();
        seviyeBelirle();

    }


    private void registerHandlers(){

        btnOyna.setOnClickListener(anaMenuButtonlari);
        btnAyarlar.setOnClickListener(anaMenuButtonlari);
        btnCikis.setOnClickListener(anaMenuButtonlari);


    }


    @Override
    protected void onResume() {
        puanBelirle();
        seviyeBelirle();
        super.onResume();
    }


    private void puanBelirle(){

        GENEL_PUAN = sPreferances.getInt("genelpuan",0);
        txtGenelPuan.setText(""+GENEL_PUAN);
    }

    private void seviyeBelirle(){
        int i = GENEL_PUAN;
        if(i <= ACEMI_SINIR)
            txtSeviye.setText("Acemi");
        else if(i > ACEMI_SINIR && i <=DENEYIMLI_SINIR )
            txtSeviye.setText("Deneyimli");
        else if(i > DENEYIMLI_SINIR && i <=CIRAK_SINIR )
            txtSeviye.setText("Çırak");
        else if(i > CIRAK_SINIR && i <=KALFA_SINIR )
            txtSeviye.setText("Kalfa");
        else if(i > KALFA_SINIR && i <=USTA_SINIR )
            txtSeviye.setText("Usta");
        else
            txtSeviye.setText("Üstat");
    }



}
