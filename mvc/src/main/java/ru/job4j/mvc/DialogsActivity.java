package ru.job4j.mvc;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;

public class DialogsActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogListener, TimePickerDialogFragment.TimePickerDialogListener {

    private String selectedTime = "";
    private String selectedDate = "";
    private TextView tvDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        tvDateTime = findViewById(R.id.tvDateTime);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedDate = String.format(Locale.getDefault(), "%02d.%02d.%d", dayOfMonth, month, year);
        setDateTimeOnTV();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        selectedTime = String.format(Locale.getDefault(), " %02d:%02d", hourOfDay, minute);
        setDateTimeOnTV();
    }

    private void setDateTimeOnTV() {
        tvDateTime.setText(String.format("%s%s", selectedDate, selectedTime));
    }

    public void OnClickDate(View view) {
        DialogFragment dateFragment = new DatePickerDialogFragment();
        dateFragment.show(getSupportFragmentManager(), "dateFragment");
    }

    public void OnClickTime(View view) {
        DialogFragment timeFragment = new TimePickerDialogFragment();
        timeFragment.show(getSupportFragmentManager(), "timeFragment");
    }
}
