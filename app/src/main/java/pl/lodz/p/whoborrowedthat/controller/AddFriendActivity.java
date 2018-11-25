package pl.lodz.p.whoborrowedthat.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.adapter.FriendsRecyclerViewAdapter;
import pl.lodz.p.whoborrowedthat.adapter.LentRecyclerViewAdapter;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import pl.lodz.p.whoborrowedthat.viewmodel.LentViewModel;
import pl.lodz.p.whoborrowedthat.viewmodel.UserRelationViewModel;
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

        Context context = getApplicationContext();
        RecyclerView recyclerView = findViewById(R.id.listOfFriends);

        final FriendsRecyclerViewAdapter friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter(context, getApplication());
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

        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutFriend);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = SharedPrefHelper.getUserFormSP(getApplication());
                //TODO: add validation if input is empty
                ApiManager.getInstance().setUserRelation(user, inputEmail.getText().toString().toLowerCase(), new Callback<UserRelation>() {
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
                        userRelationViewModel.refreshData(getApplication());
                        mSwipeRefreshLayout.setRefreshing(false);
                        //TODO: Add validation message
                    }

                    @Override
                    public void onFailure(Call<UserRelation> call, Throwable t) {
                        Toast.makeText(AddFriendActivity.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                        userRelationViewModel.refreshData(getApplication());
                        mSwipeRefreshLayout.setRefreshing(false);
                        //TODO: Add validation message
                    }
                });
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
