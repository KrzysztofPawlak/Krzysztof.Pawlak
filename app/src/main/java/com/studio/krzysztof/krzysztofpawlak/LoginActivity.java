package com.studio.krzysztof.krzysztofpawlak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private ImageView image;
    private Button buttonRotate;
    private Button mSignUpButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private String email;
    private String password;
    private boolean hasErrors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        image = (ImageView) findViewById(R.id.workImage);
        buttonRotate = (Button) findViewById(R.id.buttonRotate);
        mSignUpButton = (Button) findViewById(R.id.sign_up_btn);
        mEmailEditText = (EditText) findViewById(R.id.email_et);
        mPasswordEditText = (EditText) findViewById(R.id.password_et);

        buttonRotate.setOnClickListener(btnRotate);
        mSignUpButton.setOnClickListener(btnSign);

        rotate();
    }

    View.OnClickListener btnRotate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rotate();
        }
    };

    View.OnClickListener btnSign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tryToSignUp();
        }
    };

    public void rotate() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim);
        image.startAnimation(animation);
    }

    private void tryToSignUp() {
        email = mEmailEditText.getText().toString();
        password = mPasswordEditText.getText().toString();

        hasErrors = false;

        if (TextUtils.isEmpty(email)) {
            hasErrors = true;
            mEmailEditText.setError("Pole nie może być puste!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setError("Nieprawidlowy adres email!");
        }

        if (TextUtils.isEmpty(password)) {
            hasErrors = true;
            mPasswordEditText.setError("Pole hasło nie może być puste!");
        } else if (!password.matches("(?=.*[A-Z])(?=.*\\d).*[a-z].*") || (password.length() < 8)) {
            hasErrors = true;
            mPasswordEditText.setError("Hasło powinno zawierać 8 znaków, przynajmniej jedną dużą, jedną małą literę oraz jedną cyfrę!");
        }

        if (!hasErrors) {
            signUp();
        }
    }

    private void signUp() {
        savePreferences("checkbox", true);

        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        startActivity(intent);

        finish();
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor checkbox = sharedPreferences.edit();
        checkbox.putBoolean(key, value);
        checkbox.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
