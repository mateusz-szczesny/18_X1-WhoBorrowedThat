package pl.lodz.p.whoborrowedthat.features;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import pl.lodz.p.whoborrowedthat.model.Stuff;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private TextView dateTextView;
    private Date date;
    private SimpleDateFormat format;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        format = new SimpleDateFormat("dd.MM.YYYY", getResources().getConfiguration().locale);
        date = new Date();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d("YEAR", String.valueOf(year));
        date.setYear(year - 1900);
        date.setMonth(month);
        date.setDate(day);

        if(dateTextView != null) {
            dateTextView.setText(format.format(date));
        }
    }

    public void setFragmentSetup(TextView dateTextView, Date date) {
        this.dateTextView = dateTextView;
        this.date = date;
    }
}
