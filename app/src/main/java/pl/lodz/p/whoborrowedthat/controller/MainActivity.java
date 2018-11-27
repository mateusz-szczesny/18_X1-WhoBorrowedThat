package pl.lodz.p.whoborrowedthat.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.ConstHelper;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        isUserAlreadyLoggedIn();
    }

    private void isUserAlreadyLoggedIn() {
        SharedPreferences sharedPref = getSharedPreferences(ConstHelper.USER__SP, Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(ConstHelper.USER_LOGIN_STATUS__SP, false)){
            onLogInSuccess();
        }
    }

    private void onLogInSuccess() {
        Intent intent = new Intent(MainActivity.this, BorrowLentListActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
