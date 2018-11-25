package pl.lodz.p.whoborrowedthat.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.ConstHelper;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.VALID_EMAIL_ADDRESS_REGEX;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        final EditText emailText = findViewById(R.id.editTextEmail);
        final EditText passowrdText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonSignIn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passowrdText.getText().toString();
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
                if (password.length() < 6  || !matcher.matches()) {
                    Toast.makeText(LoginActivity.this,
                            "Incorrect input!",
                            Toast.LENGTH_LONG).show();
                } else {
                    ApiManager.getInstance().loginUser(email, password, new Callback<User>() {
                        @Override
                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                            User responseUser = response.body();
                            if (response.isSuccessful() && responseUser != null) {
                                Toast.makeText(LoginActivity.this,
                                        "WELCOME!",
                                        Toast.LENGTH_LONG).show();

                                //Save logged user to SP
                                SharedPrefHelper.storeUserInSharedPrefs(responseUser, getApplication());
                                onLogInSuccess();
                            } else {
                                Log.d("ERROR", response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                            Toast.makeText(LoginActivity.this,
                                    "Cannot login!\nPlease try again later."
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                }
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
        Intent intent = new Intent(LoginActivity.this, BorrowLentListActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}
