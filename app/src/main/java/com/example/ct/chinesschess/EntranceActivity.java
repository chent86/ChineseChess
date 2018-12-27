package com.example.ct.chinesschess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EntranceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance);
    }

    void click1(View view) {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("mode", 4);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void click2(View vide) {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("mode", 5);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
