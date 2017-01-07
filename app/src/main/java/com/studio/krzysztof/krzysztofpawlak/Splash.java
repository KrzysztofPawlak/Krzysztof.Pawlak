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

    private Handler handler;
    private boolean isMinimazed = false;
    private boolean splashIsCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_screen);

        final SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean isLogged = getPrefs.getBoolean("checkbox", false);

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
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        boolean isLogged = getPrefs.getBoolean("checkbox", false);

        if(isLogged == false) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }

    int backButtonCount;

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
        if (isMinimazed && (splashIsCanceled == false)) {
            Intent i = new Intent(Splash.this, LoginActivity.class);
            startActivity(i);
            isMinimazed = false;
            finish();
        } else if (isMinimazed && (splashIsCanceled == true)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, TIME);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}