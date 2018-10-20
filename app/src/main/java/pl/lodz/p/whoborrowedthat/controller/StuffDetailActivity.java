package pl.lodz.p.whoborrowedthat.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.model.Stuff;

import static pl.lodz.p.whoborrowedthat.helper.ConstHelper.STUFF_BUNDLE__KEY;

public class StuffDetailActivity extends AppCompatActivity {

    private Stuff stuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_detail);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getSerializable(STUFF_BUNDLE__KEY) != null)
            stuff = (Stuff)bundle.getSerializable(STUFF_BUNDLE__KEY);

        if (stuff != null) {
            //TODO: set UI
        }
    }
}
