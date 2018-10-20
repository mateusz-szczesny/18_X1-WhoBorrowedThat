package pl.lodz.p.whoborrowedthat.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.ConstHelper;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        final EditText emailText = findViewById(R.id.editTextEmail);
        final EditText passowrdText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonSignIn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passowrdText.getText().toString();
                ApiManager.getInstance().loginUser(email, password, new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
                            Toast.makeText(LoginActivity.this,
                                    String.format(responseUser.getToken()),
                                    Toast.LENGTH_LONG)
                                    .show();

                            storeUserInSharedPrefs(responseUser);
                            onLogInSuccess();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    String.format("Response is %s", String.valueOf(response.code()))
                                    , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                    }
                });
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


    private void storeUserInSharedPrefs(User user) {
        SharedPreferences sharedPref = getSharedPreferences(ConstHelper.USER__SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ConstHelper.USER_LOGIN_STATUS__SP, true);
        editor.putString(ConstHelper.USER_DATA__SP, new Gson().toJson(user));
        editor.apply();
    }

    private void onLogInSuccess() {
        Intent intent = new Intent(LoginActivity.this, BorrowLentListActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}
