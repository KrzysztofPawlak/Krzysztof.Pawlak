package com.studio.krzysztof.krzysztofpawlak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    private static final int TIME = 5000;

    private static Handler handler;
    private boolean isMinimazed = false;
    private boolean splashIsCanceled = false;
    private int backButtonCount;

    private SharedPreferences getPrefs;
    private boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_screen);

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        isLogged = getPrefs.getBoolean("checkbox", false);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chooseNextAcivity();
                finish();
            }
        }, TIME);
    }

    public void chooseNextAcivity() {

        if (isLogged == false) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(getApplicationContext(), PhotoActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            super.onBackPressed();
            finish();
        } else {
            handler.removeCallbacksAndMessages(null);
            splashIsCanceled = true;
            Toast.makeText(this, "Przyciśnij przycisk jeszcze raz, aby wyjść z aplikacji.", Toast.LENGTH_SHORT).show();
        }
        backButtonCount++;
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
        isMinimazed = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        backButtonCount = 0;
        if (splashIsCanceled || isMinimazed) {
            chooseNextAcivity();
            isMinimazed = false;
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}