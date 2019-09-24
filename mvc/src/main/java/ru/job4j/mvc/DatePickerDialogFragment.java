package ru.job4j.mvc;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment {
    private DatePickerDialogListener callback;

    public interface DatePickerDialogListener {
        void onDateSet(DatePicker view, int year, int month, int dayOfMonth);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth) -> callback.onDateSet(view, year1, month1, dayOfMonth),
                year,
                month,
                day
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (DatePickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format("%s must implement TimePickerDialogListener", context.toString()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
