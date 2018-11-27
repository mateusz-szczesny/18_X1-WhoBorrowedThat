package pl.lodz.p.whoborrowedthat.controller;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.lodz.p.whoborrowedthat.R;
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
    private Button returnItemBtn;
    private TextView returnDateTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lent_stuff_detail);

        itemName = findViewById(R.id.itemName);
        borrowDate = findViewById(R.id.borrowDate);
        returnDate = findViewById(R.id.returnDate);
        borrowerName = findViewById(R.id.borrowerName);
        changeDateBtn = findViewById(R.id.changeDateBtn);
        returnItemBtn = findViewById(R.id.returnItemBtn);
        returnDateTitle = findViewById(R.id.returnDateTitle);

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
                showDatePickerDialogForReturnDate();
            }
        });

        returnItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                User currentUser = SharedPrefHelper.getUserFormSP(getApplication());
                ApiManager.getInstance().returnItem(currentUser, stuff.getId(), date, new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        markItemAsReturned();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LentStuffDetailActivity.this,
                                "Cannot return the item :("
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

            if (stuff.getReturnDate() != null) {
                markItemAsReturned();
            }
        }
    }

    public void showDatePickerDialogForReturnDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog newFragment = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String date = day + "-" + month + "-" + year;
                        try {
                            callForNewDate(formatter.parse(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, dayOfMonth);
        newFragment.show();
    }

    public void callForNewDate(final Date date) {
        User currentUser = SharedPrefHelper.getUserFormSP(getApplication());
        ApiManager.getInstance().changeEstimatedReturnDate(currentUser, stuff.getId(), date, new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Toast.makeText(LentStuffDetailActivity.this,
                        "Date changed!"
                        , Toast.LENGTH_LONG).show();
                returnDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LentStuffDetailActivity.this,
                        "Cannot change the date :("
                        , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void markItemAsReturned() {
        returnItemBtn.setEnabled(false);
        returnItemBtn.setBackgroundColor(Color.RED);
        returnItemBtn.setText("Already returned :)");
        returnDateTitle.setText("Returned on");
        returnDate.setText(dateFormat.format(stuff.getReturnDate()));
    }
}
