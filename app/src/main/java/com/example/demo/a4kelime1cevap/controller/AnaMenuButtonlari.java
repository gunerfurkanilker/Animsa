package com.example.demo.a4kelime1cevap.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.demo.a4kelime1cevap.R;
import com.example.demo.a4kelime1cevap.view.AyarlarActivity;
import com.example.demo.a4kelime1cevap.view.GirisActivity;
import com.example.demo.a4kelime1cevap.view.OyunActivity;

public class AnaMenuButtonlari implements View.OnClickListener {

    private Context context;
    private Intent intent;



    public AnaMenuButtonlari(Context context)
    {
        this.context = context;
    }




    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnOyna:
                intent = new Intent(context, OyunActivity.class);
                context.startActivity(intent);
                break;
            case R.id.btnAyarlar:
                intent = new Intent(context,AyarlarActivity.class);
                context.startActivity(intent);
                break;
            case R.id.btnCikisYap:
                ((Activity)context).finish();
                break;

        }




    }
}
