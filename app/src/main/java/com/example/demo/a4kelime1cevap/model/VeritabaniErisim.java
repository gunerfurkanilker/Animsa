package com.example.demo.a4kelime1cevap.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeritabaniErisim  {

    private static VeritabaniErisim instance;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private Cursor cursorDB;

    private VeritabaniErisim(Context context){

        openHelper = new Veritabani(context);

    }


    public static VeritabaniErisim getInstance(Context context){

        if(instance == null){
            instance = new VeritabaniErisim(context);
        }
        return instance;
    }


    public void openDatabase(){
        this.db = openHelper.getWritableDatabase();
    }

    public void closeDatabase(){
        if(db != null)
            this.db.close();
    }

    public Cursor soruGetir(int soruID){
        cursorDB = db.rawQuery("select * from sorular where soruID = " + soruID,null);
        return cursorDB;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
