package com.studio.krzysztof.krzysztofpawlak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mLogOffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogOffButton = (Button) findViewById(R.id.logout_btn);

        final SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean isLogged = getPrefs.getBoolean("checkbox", false);

        if(isLogged == false) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        mLogOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrefs.edit().putBoolean("checkbox", false).commit();

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

                finish();
            }
        });
    }
}
