package ru.job4j.job4jexam;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class ExamActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                picker = new DatePickerDialog(ExamActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvSelectedDate.setText(
                                new StringBuilder()
                                        .append(dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth)
                                        .append(".")
                                        .append(month + 1 < 10 ? "0" + (month + 1) : month + 1)
                                        .append(".")
                                        .append(year)
                                        .toString());
                    }
                }, year, month, day);
                picker.show();
            }
        });


    }
}
