package pl.lodz.p.whoborrowedthat.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.adapter.FriendsRecyclerViewAdapter;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import pl.lodz.p.whoborrowedthat.viewmodel.UserRelationViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.VALID_EMAIL_ADDRESS_REGEX;

public class AddFriendActivity extends AppCompatActivity {
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        submitBtn = findViewById(R.id.submitBtn);
        final EditText inputEmail = findViewById(R.id.inputEmail);

        Context context = getApplicationContext();
        RecyclerView recyclerView = findViewById(R.id.listOfFriends);

        final FriendsRecyclerViewAdapter friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter(getApplication());
        recyclerView.setAdapter(friendsRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final UserRelationViewModel userRelationViewModel = ViewModelProviders.of(this).get(UserRelationViewModel.class);
        userRelationViewModel.getRelations().observe(this, new Observer<List<UserRelation>>() {
            @Override
            public void onChanged(@Nullable List<UserRelation> relations) {
                friendsRecyclerViewAdapter.setFriends(relations);
            }
        });

        friendsRecyclerViewAdapter.setVM(userRelationViewModel);

        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutFriend);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = SharedPrefHelper.getUserFormSP(getApplication());
                String inputData = String.valueOf(inputEmail.getText());
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(inputData);
                if ("".equals(inputData) || !matcher.matches()){
                    Toast.makeText(AddFriendActivity.this,
                            "INCORRECT INPUT!", Toast.LENGTH_LONG).show();
                } else {
                    submitBtn.setEnabled(false);
                    ApiManager.getInstance().setUserRelation(user, inputEmail.getText().toString().toLowerCase(), new Callback<UserRelation>() {
                        @Override
                        public void onResponse(@NonNull Call<UserRelation> call, @NonNull Response<UserRelation> response) {
                            UserRelation responseUserRelation = response.body();
                            if (response.isSuccessful() && responseUserRelation != null) {
                                Toast.makeText(AddFriendActivity.this,
                                        responseUserRelation.getRelatingUser().toString(),
                                        Toast.LENGTH_LONG)
                                        .show();
                                Log.d("RESPONSE", responseUserRelation.getRelatedUser().toString() + responseUserRelation.getRelatingUser().toString());
                            } else {
                                Log.d("RESPONSE", String.format("Response is %s", String.valueOf(response.code())));
                            }
                            userRelationViewModel.refreshData(getApplication());
                            mSwipeRefreshLayout.setRefreshing(false);
                            submitBtn.setEnabled(true);
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserRelation> call, @NonNull Throwable t) {
                            Log.d("ERROR","Error is " + t.getMessage());
                            userRelationViewModel.refreshData(getApplication());
                            mSwipeRefreshLayout.setRefreshing(false);
                            submitBtn.setEnabled(true);
                        }
                    });
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userRelationViewModel.refreshData(getApplication());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
