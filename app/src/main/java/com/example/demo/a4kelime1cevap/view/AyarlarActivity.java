package com.example.demo.a4kelime1cevap.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.demo.a4kelime1cevap.R;

public class AyarlarActivity extends AppCompatActivity {

    private RadioGroup rdoGroup;
    private SharedPreferences sPreferances;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        init();
        registerHandlers();
    }



    private void init(){
        rdoGroup = (RadioGroup) findViewById(R.id.radioGroup);
        sPreferances = getSharedPreferences("zorlukseviyesi",MODE_PRIVATE);
        editor = sPreferances.edit();

    }

    private void registerHandlers(){
        rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID){
                    case R.id.rdoKolay :
                        editor.putInt("zorlukseviyesi",0);
                        editor.commit();
                        break;
                    case R.id.rdoOrta :
                        editor.putInt("zorlukseviyesi",1);
                        editor.commit();
                        break;
                    case R.id.rdoZor :
                        editor.putInt("zorlukseviyesi",2);
                        editor.commit();
                        break;
                }
            }
        });

    }

}
