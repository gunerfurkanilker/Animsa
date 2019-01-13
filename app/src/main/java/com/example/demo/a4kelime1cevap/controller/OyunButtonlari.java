package com.example.demo.a4kelime1cevap.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.a4kelime1cevap.R;
import com.example.demo.a4kelime1cevap.model.SoruListesi;
import com.example.demo.a4kelime1cevap.model.Sorular;
import com.example.demo.a4kelime1cevap.model.VeritabaniErisim;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class OyunButtonlari implements View.OnClickListener {

    private Context context;
    private Dialog dialog;
    private MediaPlayer mpDogruCevap;
    private MediaPlayer mpYanlisCevap;
    LinearLayout layoutIpuclari;

    private String ZORLUK_SEVİYESİ;
    private int MAX_PUAN;
    private int PUAN_AZALT;
    private int YANLIS_SAYISI=0;
    private int SORU_INDEXI = 0;
    private int GENEL_PUAN;

    private static final int KOLAY_SEVIYE_MAX = 2000;
    private static final int ORTA_SEVIYE_MAX = 4000;
    private static final int ZOR_SEVIYE_MAX = 6000;

    private static final int KOLAY_SEVIYE_AZALT = 250;
    private static final int ORTA_SEVİYE_AZALT = 500;
    private static final int ZOR_SEVİYE_AZALT = 1200;

    private static final int KOLAY_ORTA_YANLIS = 8;
    private static final int ZOR_SEVIYE_YANLIS = 5;
    private int MAX_YANLIS_SAYISI;

    private VeritabaniErisim vtErisim;
    private SoruListesi soruListesi;
    private ArrayList<Sorular> gelenSorular;
    private Toast dogruCevapToast,yanlisCevapToast;
    private SharedPreferences sPreferances,sPreferances2;
    private SharedPreferences.Editor editor;
    private GradientDrawable ipuclariBackgroundDrawable;
    int zorlukSeviyesi;


    Activity activity;
    private InterstitialAd reklamOyunsonu;
    private AdView bannerReklami;


    private Button scnk1,scnk2,scnk3,scnk4;
    private TextView  kategori,ipucu1,ipucu2,ipucu3,ipucu4,txtSuankiSoru,txtYanlisCevap,dtxtYanlisCevap,dtxtZorlukseviyesi
            ,dtxtAlinanPuan;



    public OyunButtonlari(Context context, Dialog dialog){
        this.context = context;
        this.dialog = dialog;
        sharedPrefsAyarlari();
        vtErisim = VeritabaniErisim.getInstance(this.context);
        vtErisim.openDatabase();
        soruListesiHazirla();
        vtErisim.closeDatabase();
        initViewElements();
        soruHazirla(SORU_INDEXI);
    }

    private void soruListesiHazirla(){
        soruListesi = new SoruListesi(zorlukSeviyesiBelirle(),vtErisim);
        gelenSorular = soruListesi.getSoruListesi();
    }


    private void sharedPrefsAyarlari(){
        sPreferances = context.getSharedPreferences("zorlukseviyesi", Context.MODE_PRIVATE);
        sPreferances2 = context.getSharedPreferences("oyuncuseviyesi",Context.MODE_PRIVATE);
        GENEL_PUAN = sPreferances2.getInt("genelpuan",0);
        editor = sPreferances2.edit();
    }

    private void initViewElements(){
        activity = (Activity)this.context;


        bannerReklami = activity.findViewById(R.id.adView);
        bannerReklami.loadAd(new AdRequest.Builder().build());


        reklamOyunsonu = new InterstitialAd(activity);
        reklamOyunsonu.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        reklamOyunsonu.loadAd(new AdRequest.Builder().build());
        reklamDinleyicisi();


        mpDogruCevap = MediaPlayer.create(activity,R.raw.dogru);
        mpYanlisCevap = MediaPlayer.create(activity,R.raw.yanlis);

        LinearLayout ipuclariLayout =  activity.findViewById(R.id.ipuclariLayout);
        ipuclariBackgroundDrawable = (GradientDrawable) ipuclariLayout.getBackground().mutate();

        kategori = (TextView) activity.findViewById(R.id.txtKategori);

        scnk1 = (Button) activity.findViewById(R.id.btnSecenek1);
        scnk2 = (Button) activity.findViewById(R.id.btnSecenek2);
        scnk3 = (Button) activity.findViewById(R.id.btnSecenek3);
        scnk4 = (Button) activity.findViewById(R.id.btnSecenek4);

        ipucu1 = (TextView) activity.findViewById(R.id.txtIpucu1);
        ipucu2 = (TextView) activity.findViewById(R.id.txtIpucu2);
        ipucu3 = (TextView) activity.findViewById(R.id.txtIpucu3);
        ipucu4 = (TextView) activity.findViewById(R.id.txtIpucu4);

        //Oyun Ekranında sol ve sağ üstte görünen viewlar
        txtYanlisCevap = (TextView) activity.findViewById(R.id.txtYanlisCevap);
        txtSuankiSoru = (TextView) activity.findViewById(R.id.txtSuankiSoru);

        //Dialogtaki View Elemanları
        dtxtAlinanPuan = (TextView) dialog.findViewById(R.id.txtAlinanPuan);
        dtxtZorlukseviyesi = (TextView) dialog.findViewById(R.id.txtZorlukSeviyesi);
        dtxtYanlisCevap = (TextView) dialog.findViewById(R.id.txtYanlisCevap);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.layout_dogru_cevap_toast,null);
        View view2 = layoutInflater.inflate(R.layout.layout_yanlis_cevap_toast,null);

        layoutIpuclari =activity.findViewById(R.id.ipuclariLayout);



        dogruCevapToast = new Toast(context);
        dogruCevapToast.setView(view1);
        dogruCevapToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,0,0);
        dogruCevapToast.setDuration(Toast.LENGTH_SHORT);

        yanlisCevapToast = new Toast(context);
        yanlisCevapToast.setView(view2);
        yanlisCevapToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,0,0);
        yanlisCevapToast.setDuration(Toast.LENGTH_SHORT);



    }

    private void soruHazirla(int soruIndex){

        kategori.setText(gelenSorular.get(SORU_INDEXI).getKategori());

        ipuclariArkaPlanDegis(gelenSorular.get(SORU_INDEXI).getKategori());

        ipucu1.setText(gelenSorular.get(SORU_INDEXI).getIpucu1());
        ipucu2.setText(gelenSorular.get(SORU_INDEXI).getIpucu2());
        ipucu3.setText(gelenSorular.get(SORU_INDEXI).getIpucu3());
        ipucu4.setText(gelenSorular.get(SORU_INDEXI).getIpucu4());

        scnk1.setText(gelenSorular.get(SORU_INDEXI).getSecenek1());
        scnk2.setText(gelenSorular.get(SORU_INDEXI).getSecenek2());
        scnk3.setText(gelenSorular.get(SORU_INDEXI).getSecenek3());
        scnk4.setText(gelenSorular.get(SORU_INDEXI).getSecenek4());

        txtSuankiSoru.setText((SORU_INDEXI+1) +"/" + SoruListesi.soruSayisi);
        txtYanlisCevap.setText(YANLIS_SAYISI + "/" + MAX_YANLIS_SAYISI);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnSecenek1:
                cevapKontrol(scnk1.getText().toString());
                break;
            case R.id.btnSecenek2:
                cevapKontrol(scnk2.getText().toString());
                break;
            case R.id.btnSecenek3:
                cevapKontrol(scnk3.getText().toString());
                break;
            case R.id.btnSecenek4:
                cevapKontrol(scnk4.getText().toString());
                break;
            case R.id.btnYeniOyun:
                dialog.dismiss();
                activity.recreate();
                reklamOyunsonu.show();
                break;
            case R.id.btnAnaMenu:
                dialog.dismiss();
                activity.finish();
                reklamOyunsonu.show();
                break;

        }


    }


    private void cevapKontrol(String s){

        if(gelenSorular.get(SORU_INDEXI).getDogruCevap().equals(s)){
            SORU_INDEXI++;
            mpDogruCevap.start();
            dogruCevapToast.show();
            try{
                soruHazirla(SORU_INDEXI);
            }catch (IndexOutOfBoundsException ioob){
                oyunuTamamla();
            }
        }

        else{
            mpYanlisCevap.start();
            if (!puanAzalt()){
               oyunuTamamla();
            }
            SORU_INDEXI++;
            yanlisCevapToast.show();
            try{
                soruHazirla(SORU_INDEXI);
            }catch (IndexOutOfBoundsException ioob){
               oyunuTamamla();
            }
        }


    }


    private int zorlukSeviyesiBelirle(){
        zorlukSeviyesi = sPreferances.getInt("zorlukseviyesi",0);
        switch (zorlukSeviyesi){
            case 0:
                MAX_PUAN = KOLAY_SEVIYE_MAX;
                PUAN_AZALT = KOLAY_SEVIYE_AZALT;
                MAX_YANLIS_SAYISI = KOLAY_ORTA_YANLIS;
                ZORLUK_SEVİYESİ = "KOLAY";
                break;
            case 1:
                MAX_PUAN = ORTA_SEVIYE_MAX;//25 Soru 4000 puan soru başına 160 puan
                PUAN_AZALT = ORTA_SEVİYE_AZALT;//Max hata 8;
                MAX_YANLIS_SAYISI = KOLAY_ORTA_YANLIS;
                ZORLUK_SEVİYESİ = "ORTA";
                break;
            case 2:
                MAX_PUAN = ZOR_SEVIYE_MAX;
                PUAN_AZALT = ZOR_SEVİYE_AZALT;
                MAX_YANLIS_SAYISI = ZOR_SEVIYE_YANLIS;
                ZORLUK_SEVİYESİ = "ZOR";
                break;
        }
        return zorlukSeviyesi;
    }


    private boolean puanAzalt(){
        MAX_PUAN-=PUAN_AZALT;
        YANLIS_SAYISI++;
        if(MAX_PUAN == 0)
            return false;
        else
            return true;

    }


    private void oyunuTamamla(){
        dtxtYanlisCevap.setText(""+YANLIS_SAYISI);
        dtxtZorlukseviyesi.setText(""+ZORLUK_SEVİYESİ);
        dtxtAlinanPuan.setText(""+MAX_PUAN);
        GENEL_PUAN+= MAX_PUAN;
        editor.putInt("genelpuan",GENEL_PUAN);
        editor.commit();
        dialog.show();
    }

    private void ipuclariArkaPlanDegis(String s){

       if(s.equals("Spor"))
           ipuclariBackgroundDrawable.setColor(ContextCompat.getColor(context,R.color.ktgrisporbgcolor));
       else if(s.equals("Tarih"))
           ipuclariBackgroundDrawable.setColor(ContextCompat.getColor(context,R.color.ktgritarihbgcolor));
       else if(s.equals("Kültür/Sanat"))
           ipuclariBackgroundDrawable.setColor(ContextCompat.getColor(context,R.color.ktgrigenelkultur));
       else if(s.equals("Coğrafya"))
           ipuclariBackgroundDrawable.setColor(ContextCompat.getColor(context,R.color.ktgricografya));
       else if(s.equals("Dizi/Sinema"))
           ipuclariBackgroundDrawable.setColor(ContextCompat.getColor(context,R.color.ktgrisinema));
       else if(s.equals("Bilim"))
           ipuclariBackgroundDrawable.setColor(ContextCompat.getColor(context,R.color.ktgribilim));

    }


    private void reklamDinleyicisi(){
        reklamOyunsonu.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                reklamOyunsonu.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

        bannerReklami.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                bannerReklami.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });



    }



}
