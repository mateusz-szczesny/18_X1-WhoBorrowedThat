package pl.lodz.p.whoborrowedthat.controller;

import android.app.SharedElementCallback;
import android.os.Bundle;
import android.util.Log;
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

public class SettingsActivity extends AppBaseActivity {
    private Button updateButton;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        updateButton = findViewById(R.id.updateButton);
        usernameEditText = findViewById(R.id.usernameEditText);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameEditText.getText().toString();
                if(username == null && username.equals("")) {
                    Toast.makeText(SettingsActivity.this,
                            "Username can not be empty"
                            , Toast.LENGTH_LONG).show();
                } else {
                    updateButton.setEnabled(false);
                    ApiManager.getInstance().updateUsername(SharedPrefHelper.getUserFormSP(getApplication()), username, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(SettingsActivity.this,
                                    "Username updated"
                                    , Toast.LENGTH_LONG).show();
                            User user = SharedPrefHelper.getUserFormSP(getApplication());
                            user.setUsername(username);
                            SharedPrefHelper.storeUserInSharedPrefs(user,getApplication());
                            Log.d("username", SharedPrefHelper.getUserFormSP(getApplication()).getUsername());
                            updateUserDetails();
                            updateButton.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(SettingsActivity.this,
                                    "Something went wrong"
                                    , Toast.LENGTH_LONG).show();
                            updateButton.setEnabled(true);
                        }
                    });
                }

            }
        });
    }
}
