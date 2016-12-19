package com.studio.krzysztof.krzysztofpawlak;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private Button buttonRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.workImage);
        buttonRotate = (Button) findViewById(R.id.buttonRotate);

        buttonRotate.setOnClickListener(btnRotate);

        rotate();
    }

    public void rotate() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim);
        image.startAnimation(animation);
    }

    View.OnClickListener btnRotate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rotate();
        }
    };
}
