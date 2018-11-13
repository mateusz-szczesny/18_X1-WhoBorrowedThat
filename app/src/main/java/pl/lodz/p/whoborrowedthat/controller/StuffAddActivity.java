package pl.lodz.p.whoborrowedthat.controller;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import pl.lodz.p.whoborrowedthat.service.ApiManager;
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
        /* STATIC DATA */
        List<Integer> list = new ArrayList<Integer>();
        list.add(5);
        list.add(7);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendsSpinner.setAdapter(dataAdapter);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuff.setName(nameEditText.getText().toString());
                stuff.getBorrower().setId((Integer)friendsSpinner.getSelectedItem());

                ApiManager.getInstance().addBorrows(SharedPrefHelper.getUserFormSP(getApplication()), stuff, new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Toast.makeText(StuffAddActivity.this,
                                String.format("Response is %s", String.valueOf(response.code()))
                                , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(StuffAddActivity.this,
                                "error"
                                , Toast.LENGTH_LONG).show();
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
}
