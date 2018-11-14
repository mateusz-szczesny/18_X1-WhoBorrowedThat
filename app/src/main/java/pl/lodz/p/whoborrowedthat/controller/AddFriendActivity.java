package pl.lodz.p.whoborrowedthat.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Button btn = findViewById(R.id.submitBtn);
        final EditText inputEmail = findViewById(R.id.inputEmail);

        TextView title = findViewById(R.id.title);
        title.setText(SharedPrefHelper.getUserFormSP(getApplication()).getUsername() + "     " + SharedPrefHelper.getUserFormSP(getApplication()).getEmail());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = SharedPrefHelper.getUserFormSP(getApplication());
                ApiManager.getInstance().setUserRelation(user, inputEmail.getText().toString(), new Callback<UserRelation>() {
                    @Override
                    public void onResponse(Call<UserRelation> call, Response<UserRelation> response) {
                        UserRelation responseUserRelation = response.body();
                        if (response.isSuccessful() && responseUserRelation != null) {
                            Toast.makeText(AddFriendActivity.this,
                                    responseUserRelation.getRelatingUser().toString(),
                                    Toast.LENGTH_LONG)
                                    .show();
                            Log.d("RESPONSE", responseUserRelation.getRelatedUser().toString() + responseUserRelation.getRelatingUser().toString());
                        } else {
                            Toast.makeText(AddFriendActivity.this,
                                    String.format("Response is %s", String.valueOf(response.code()))
                                    , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRelation> call, Throwable t) {
                        Toast.makeText(AddFriendActivity.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
