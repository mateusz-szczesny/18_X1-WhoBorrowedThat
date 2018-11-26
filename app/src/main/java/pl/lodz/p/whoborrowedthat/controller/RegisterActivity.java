package pl.lodz.p.whoborrowedthat.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.MINIMUM_PASSWORD_LENGTH;
import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.VALID_EMAIL_ADDRESS_REGEX;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText getEditTextPasswordConfirmation;
    private EditText editTextUsername;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        getEditTextPasswordConfirmation = findViewById(R.id.editTextPasswordConfirmation);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordConfirmation = getEditTextPasswordConfirmation.getText().toString();
                String username = editTextUsername.getText().toString();

                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
                if (password.equals(passwordConfirmation) && matcher.matches() && password.length() >= MINIMUM_PASSWORD_LENGTH) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ApiManager.getInstance().registerUser(email, password, passwordConfirmation, username, new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User responseUser = response.body();
                            if (response.isSuccessful() && responseUser != null) {
                                Toast.makeText(RegisterActivity.this,
                                        "REGISTERED SUCCESSFULLY!",
                                        Toast.LENGTH_LONG)
                                        .show();

                                //Save registered user to SP
                                SharedPrefHelper.storeUserInSharedPrefs(responseUser, getApplication());
                                onRegisterSuccess();

                            } else {
                                Log.d("ERROR", response.message());
                            }
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this,
                                    "Cannot register!\nPlease try again later."
                                    , Toast.LENGTH_LONG).show();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Incorrect input data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onRegisterSuccess() {
        Intent intent = new Intent(RegisterActivity.this, BorrowLentListActivity.class);
        RegisterActivity.this.startActivity(intent);
    }
}
