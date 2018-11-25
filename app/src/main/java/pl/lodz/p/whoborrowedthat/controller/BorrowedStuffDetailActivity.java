package pl.lodz.p.whoborrowedthat.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class BorrowedStuffDetailActivity extends AppBaseActivity {

    private Stuff stuff;
    private TextView itemName;
    private TextView borrowDate;
    private TextView returnDate;
    private TextView ownerName;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_stuff_detail);

        itemName = findViewById(R.id.itemName);
        borrowDate = findViewById(R.id.borrowDate);
        returnDate = findViewById(R.id.returnDate);
        ownerName = findViewById(R.id.ownerName);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getSerializable(STUFF_BUNDLE__KEY) != null) {
            stuff = (Stuff) bundle.getSerializable(STUFF_BUNDLE__KEY);
        }

        if (stuff != null) { //UI here
            itemName.setText(stuff.getName());
            borrowDate.setText(dateFormat.format(stuff.getRentalDate()));
            returnDate.setText(dateFormat.format(stuff.getEstimatedReturnDate()));
            StringBuilder ownerNameStringBuilder = new StringBuilder();
            ownerNameStringBuilder.append(stuff.getOwner().getFirstName());
            ownerNameStringBuilder.append(" ");
            ownerNameStringBuilder.append(stuff.getOwner().getLastName());
            ownerName.setText(ownerNameStringBuilder.toString());
        }
    }
}
