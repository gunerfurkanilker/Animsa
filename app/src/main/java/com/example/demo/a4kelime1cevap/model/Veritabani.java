package com.example.demo.a4kelime1cevap.model;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Veritabani extends SQLiteAssetHelper {

    private static final String VERITABANI_ISMI = "1Cevap4KelimeDBv2.db";
    private static final int VERSION_ID=1;


    public Veritabani(Context context){
        super(context,VERITABANI_ISMI,null,VERSION_ID);
    }


}
