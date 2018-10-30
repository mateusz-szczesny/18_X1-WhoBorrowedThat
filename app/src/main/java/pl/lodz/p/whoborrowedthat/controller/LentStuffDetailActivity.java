package pl.lodz.p.whoborrowedthat.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.model.Stuff;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class LentStuffDetailActivity extends AppBaseActivity {
    private Stuff stuff;
    private TextView itemName;
    private TextView borrowDate;
    private TextView returnDate;
    private TextView borrowerName;
    private SimpleDateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lent_stuff_detail);

        itemName = findViewById(R.id.itemName);
        borrowDate = findViewById(R.id.borrowDate);
        returnDate = findViewById(R.id.returnDate);
        borrowerName = findViewById(R.id.borrowerName);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getSerializable(STUFF_BUNDLE__KEY) != null) {
            stuff = (Stuff) bundle.getSerializable(STUFF_BUNDLE__KEY);
            itemName.setText(stuff.getName());
            borrowDate.setText(dateFormat.format(stuff.getRentalDate()));
            returnDate.setText(dateFormat.format(stuff.getEstimatedReturnDate()));
            StringBuilder ownerNameStringBuilder = new StringBuilder();
            ownerNameStringBuilder.append(stuff.getBorrower().getFirstName());
            ownerNameStringBuilder.append(" ");
            ownerNameStringBuilder.append(stuff.getBorrower().getLastName());
            borrowerName.setText(ownerNameStringBuilder.toString());
        }
    }
}
