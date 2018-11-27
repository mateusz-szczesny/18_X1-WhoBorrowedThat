package pl.lodz.p.whoborrowedthat.controller;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.features.DatePickerFragment;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class LentStuffDetailActivity extends AppBaseActivity {
    private Stuff stuff;
    private TextView itemName;
    private TextView borrowDate;
    private TextView returnDate;
    private TextView borrowerName;
    private SimpleDateFormat dateFormat;
    private Button sendRemainder;
    private Button changeDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lent_stuff_detail);

        itemName = findViewById(R.id.itemName);
        borrowDate = findViewById(R.id.borrowDate);
        returnDate = findViewById(R.id.returnDate);
        borrowerName = findViewById(R.id.borrowerName);
        changeDateBtn = findViewById(R.id.changeDateBtn);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Bundle bundle = getIntent().getExtras();

        sendRemainder = findViewById(R.id.sendRemainder);

        sendRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User currentUser = SharedPrefHelper.getUserFormSP(getApplication());
                ApiManager.getInstance().notifyBorrow(currentUser, stuff.getId(), new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                        Toast.makeText(LentStuffDetailActivity.this,
                                "Remainder sent!"
                                , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                        Toast.makeText(LentStuffDetailActivity.this,
                                "Cannot send a remainder :("
                                , Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        changeDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogForReturnDate(v);
                User currentUser = SharedPrefHelper.getUserFormSP(getApplication());
                ApiManager.getInstance().changeEstimatedReturnDate(currentUser, stuff.getId(), stuff.getEstimatedReturnDate(), new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Toast.makeText(LentStuffDetailActivity.this,
                                "Date will be changed!"
                                , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LentStuffDetailActivity.this,
                                "Cannot change the date :("
                                , Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        if (bundle != null && bundle.getSerializable(STUFF_BUNDLE__KEY) != null) {
            stuff = (Stuff) bundle.getSerializable(STUFF_BUNDLE__KEY);
            itemName.setText(stuff.getName());
            borrowDate.setText(dateFormat.format(stuff.getRentalDate()));
            returnDate.setText(dateFormat.format(stuff.getEstimatedReturnDate()));
            String ownerNameStringBuilder = stuff.getBorrower().getFirstName() +
                    " " +
                    stuff.getBorrower().getLastName();
            borrowerName.setText(ownerNameStringBuilder);
        }
    }

    public void showDatePickerDialogForReturnDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment)newFragment).setFragmentSetup(null, stuff.getEstimatedReturnDate());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
