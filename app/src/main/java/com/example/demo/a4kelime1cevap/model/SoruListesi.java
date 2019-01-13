package com.example.demo.a4kelime1cevap.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SoruListesi {

    private int zorlukSeviyesi;
    private int vTabanindakiSoruSayisi = 0;
    private ArrayList<Sorular> sorularArrayList;
    private ArrayList<Integer> uretilenIDListesi;
    private Random rnd;
    int uretilenSayi;
    public static int soruSayisi=0;
    private VeritabaniErisim vtErisim;
    ArrayList<String> seceneklerListesi = new ArrayList<String>();
    ArrayList<String> ipuclariListesi = new ArrayList<String>();





    public SoruListesi(int zorlukSeviyesi,VeritabaniErisim vtErisim){
        this.zorlukSeviyesi = zorlukSeviyesi;
        this.vtErisim = vtErisim;
        sorularArrayList = new ArrayList<Sorular>();
        uretilenIDListesi = new ArrayList<Integer>();
        init();
    }


    private void init(){
        rnd = new Random();

        if(vTabanindakiSoruSayisi != 0)
            vTabanindakiSoruSayisi = 0;

        if(!uretilenIDListesi.isEmpty())
            uretilenIDListesi.clear();
            toplamSoruSayisi();
            soruIDUret();
            soruEkle();

    }

    //Soru Dizisini geriye döndürüyoruz
    public ArrayList<Sorular> getSoruListesi(){
        return sorularArrayList;
    }

    //Soru Dizisine eleman ekliyoruz
    private void soruEkle(){

        for (Integer soruID:uretilenIDListesi) {

            Cursor geciciCursor = vtErisim.soruGetir(soruID.intValue());
                while(geciciCursor.moveToNext()){

                    Sorular tempSoru = new Sorular();
                    setKategori(tempSoru,soruID.intValue());
                    setIpuclari(tempSoru,soruID.intValue());
                    setSecenekler(tempSoru,soruID.intValue());
                    setDogruCevap(tempSoru,soruID.intValue());
                    sorularArrayList.add(tempSoru);

                }
        }



    }

    //Sorunun ipuçları belirleniyor
    private void setIpuclari(Sorular tempSoru,int soruID){

        Cursor tempCursor = vtErisim.getDb().rawQuery("select ipucuAdi from ipuclari inner join sorular on\n" +
                "sorular.ipucu1 = ipuclari.ipucuId where sorular.soruId = "+ soruID,null);
        while (tempCursor.moveToNext())
            ipuclariListesi.add(tempCursor.getString(0));

        tempCursor.close();

        tempCursor = vtErisim.getDb().rawQuery("select ipucuAdi from ipuclari inner join sorular on\n" +
                "sorular.ipucu2 = ipuclari.ipucuId where sorular.soruId = "+ soruID,null);

        while (tempCursor.moveToNext())
            ipuclariListesi.add(tempCursor.getString(0));

        tempCursor.close();

        tempCursor = vtErisim.getDb().rawQuery("select ipucuAdi from ipuclari inner join sorular on\n" +
                "sorular.ipucu3 = ipuclari.ipucuId where sorular.soruId = "+ soruID,null);

        while (tempCursor.moveToNext())
            ipuclariListesi.add(tempCursor.getString(0));

        tempCursor.close();

        tempCursor = vtErisim.getDb().rawQuery("select ipucuAdi from ipuclari inner join sorular on\n" +
                "sorular.ipucu4 = ipuclari.ipucuId where sorular.soruId = "+ soruID,null);

        while (tempCursor.moveToNext())
            ipuclariListesi.add(tempCursor.getString(0));

        tempCursor.close();

        Collections.shuffle(ipuclariListesi);

        tempSoru.setIpucu1(ipuclariListesi.get(0));
        tempSoru.setIpucu2(ipuclariListesi.get(1));
        tempSoru.setIpucu3(ipuclariListesi.get(2));
        tempSoru.setIpucu4(ipuclariListesi.get(3));

        ipuclariListesi.clear();


    }


    //Sorunun şıkları belirleniyor
    private void setSecenekler(Sorular tempSoru,int soruID){

        Cursor tempCursor = vtErisim.getDb().rawQuery("select scnkAdi from secenekler inner join sorular on\n"+
                "sorular.secenek1 = secenekler.scnkId where sorular.soruId = " + soruID,null);
        while (tempCursor.moveToNext())
            seceneklerListesi.add(tempCursor.getString(0));



        tempCursor.close();

        tempCursor = vtErisim.getDb().rawQuery("select scnkAdi from secenekler inner join sorular on\n"+
                "sorular.secenek2 = secenekler.scnkId where sorular.soruId = " + soruID,null);
        while (tempCursor.moveToNext())
            seceneklerListesi.add(tempCursor.getString(0));



        tempCursor.close();

        tempCursor = vtErisim.getDb().rawQuery("select scnkAdi from secenekler inner join sorular on\n"+
                "sorular.secenek3 = secenekler.scnkId where sorular.soruId = " + soruID,null);
        while (tempCursor.moveToNext())
            seceneklerListesi.add(tempCursor.getString(0));



        tempCursor.close();

        tempCursor = vtErisim.getDb().rawQuery("select scnkAdi from secenekler inner join sorular on\n"+
                "sorular.secenek4 = secenekler.scnkId where sorular.soruId = " + soruID,null);
        while (tempCursor.moveToNext())
            seceneklerListesi.add(tempCursor.getString(0));

        tempCursor.close();

        Collections.shuffle(seceneklerListesi);

        tempSoru.setSecenek1(seceneklerListesi.get(0));
        tempSoru.setSecenek2(seceneklerListesi.get(1));
        tempSoru.setSecenek3(seceneklerListesi.get(2));
        tempSoru.setSecenek4(seceneklerListesi.get(3));

        seceneklerListesi.clear();

    }

    //Sorunun kategorisi belirleniyor
    private void setKategori(Sorular tempSoru,int soruID){
        Cursor tempCursor = vtErisim.getDb().rawQuery("select kategoriAdi from kategoriler inner join sorular on\n" +
                "sorular.kategoriId = kategoriler.kategoriId where sorular.soruId = " + soruID,null);
        while (tempCursor.moveToNext())
            tempSoru.setKategori(tempCursor.getString(0));

        tempCursor.close();
    }


    //Sorunun dogru cevabı belirleniyor.
    private void setDogruCevap(Sorular tempSoru,int soruID){
        int dogruCevapID = 0;

        Cursor tempCursor = vtErisim.getDb().rawQuery("select scnkId from dogrucevaplar inner join sorular on\n" +
                "sorular.dgrcvpId = dogrucevaplar.dgrcvpId where sorular.soruId = " +soruID,null);
        while (tempCursor.moveToNext())
            dogruCevapID = tempCursor.getInt(0);

        tempCursor.close();

        tempCursor = vtErisim.getDb().rawQuery("select scnkAdi from secenekler where scnkId= " +dogruCevapID,null );
        while(tempCursor.moveToNext())
            tempSoru.setDogruCevap(tempCursor.getString(0));

        tempCursor.close();


    }

    //Burada veritabaninda bulunan toplam soru sayısını belirliyoruz.
    private void toplamSoruSayisi(){

        Cursor tempCursor = vtErisim.getDb().rawQuery("select * from sorular",null);
        while(tempCursor.moveToNext())
            vTabanindakiSoruSayisi++;
    }





    //Burada ID üreticez bunu ise şu sebepten ==> veritabanından ilgili ID no suna sahip satırı çekicez
    private void soruIDUret(){

        //Burada shared preferances dosyasından gelen zorlukseviyesi degerine göre toplam soru sayısı belirliyoruz.
        switch (zorlukSeviyesi){
            case 0:
                soruSayisi = 20;//ŞİMDİLİK BÖYLE KALICAK
                break;
            case 1:
                soruSayisi = 25;//ŞİMDİLİK BÖYLE KALICAK
                break;
            case 2:
                soruSayisi = 30;//ŞİMDİLİK BÖYLE KALICAK
                break;
        }


        for (int i = 0 ; i < soruSayisi ; i++){
            uretilenSayi = rnd.nextInt(vTabanindakiSoruSayisi) + 1;
            if(ayniDegerMi(uretilenSayi)){
                i--;
            }
            else{
                uretilenIDListesi.add(uretilenSayi);
            }




        }

    }
    //Aynı sayının yani aynı soruların üretilmediğini garantiliyoruz
    private boolean ayniDegerMi(int uretilenSayi){

        for (Integer integer:uretilenIDListesi) {
            if(uretilenSayi == integer.intValue())
                return true;
        }

        return false;
    }

}
