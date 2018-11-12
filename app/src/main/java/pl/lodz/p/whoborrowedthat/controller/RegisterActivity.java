package pl.lodz.p.whoborrowedthat.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText getEditTextPasswordConfirmation;
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
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordConfirmation = getEditTextPasswordConfirmation.getText().toString();

                if (password.equals(passwordConfirmation)) {
                    ApiManager.getInstance().registerUser(email, password, passwordConfirmation, new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User responseUser = response.body();
                            if (response.isSuccessful() && responseUser != null) {
                                Toast.makeText(RegisterActivity.this,
                                        responseUser.getToken(),
                                        Toast.LENGTH_LONG)
                                        .show();
                                SharedPrefHelper.storeUserInSharedPrefs(responseUser, getApplication());

                                Intent intent = new Intent(RegisterActivity.this, BorrowLentListActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        String.format("Response is %s", String.valueOf(response.code()))
                                        , Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this,
                                    "Error is " + t.getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
