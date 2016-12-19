package com.studio.krzysztof.krzysztofpawlak;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    private static final int TIME = 5000;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_screen);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                        startActivity(i);
                        finish();
            }
        }, TIME);
    }

    int backButtonCount;

    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1) {
            super.onBackPressed();
        } else {
            handler.removeCallbacksAndMessages(null);
            Toast.makeText(this, "Przyciśnij przycisk jeszcze raz, aby wyjść z aplikacji.", Toast.LENGTH_SHORT).show();
        }

        backButtonCount++;
    }
}