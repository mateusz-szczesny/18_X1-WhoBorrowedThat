package pl.lodz.p.whoborrowedthat.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.features.DatePickerFragment;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import pl.lodz.p.whoborrowedthat.viewmodel.BorrowViewModel;
import pl.lodz.p.whoborrowedthat.viewmodel.UserRelationViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StuffAddActivity extends AppBaseActivity {
    private TextView rentalDateTextView;
    private TextView estimatedReturnDateTextView;
    private EditText nameEditText;
    private Stuff stuff;
    private SimpleDateFormat format;
    private Spinner friendsSpinner;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_add);

        rentalDateTextView = findViewById(R.id.rentalDateTextView);
        estimatedReturnDateTextView = findViewById(R.id.returnDateTextView);
        format = new SimpleDateFormat("dd.MM.YYYY", getResources().getConfiguration().locale);
        stuff = new Stuff();
        rentalDateTextView.setText(format.format(stuff.getRentalDate()));
        estimatedReturnDateTextView.setText(format.format(stuff.getEstimatedReturnDate()));

        nameEditText = findViewById(R.id.nameEditText);
        friendsSpinner = findViewById(R.id.friendsSpinner);

        final List<UserSelection> list = new ArrayList<UserSelection>();

        UserRelationViewModel userRelationViewModel = ViewModelProviders.of(this).get(UserRelationViewModel.class);
        userRelationViewModel.getRelations().observe(this, new Observer<List<UserRelation>>() {
            @Override
            public void onChanged(@Nullable List<UserRelation> relations) {
                User user = SharedPrefHelper.getUserFormSP(getApplication());
                for(UserRelation ur : relations) {
                    User relatedUser;
                    relatedUser = ur.getRelatedUser();
                    if (user.getId() == relatedUser.getId()) {
                        relatedUser = ur.getRelatingUser();
                    }
                    list.add(new UserSelection((int)relatedUser.getId(), relatedUser.getUsername()));
                }
                ArrayAdapter<UserSelection> dataAdapter = new ArrayAdapter<UserSelection>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                friendsSpinner.setAdapter(dataAdapter);
            }
        });



        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuff.setName(nameEditText.getText().toString());
                UserSelection selectedUser = (UserSelection) friendsSpinner.getSelectedItem();
                stuff.getBorrower().setId(selectedUser.getId());

                ApiManager.getInstance().addBorrows(SharedPrefHelper.getUserFormSP(getApplication()), stuff, new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Toast.makeText(StuffAddActivity.this,
                                "Borrow added!"
                                , Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.d("ERROR", t.getMessage());
                    }
                });
            }
        });
    }

    public void showDatePickerDialogForRentalDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment)newFragment).setFragmentSetup(rentalDateTextView, stuff.getRentalDate());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogForReturnDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment)newFragment).setFragmentSetup(estimatedReturnDateTextView, stuff.getRentalDate());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private class UserSelection {
        int id;
        String displayedName;

        public UserSelection(int id, String displayedName) {
            this.id = id;
            this.displayedName = displayedName;
        }

        @Override
        public String toString() {
            return displayedName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDisplayedName() {
            return displayedName;
        }

        public void setDisplayedName(String displayedName) {
            this.displayedName = displayedName;
        }
    }
}
