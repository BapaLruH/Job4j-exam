package ru.job4j.mvc;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment {

    private TimePickerDialogListener callback;

    public interface TimePickerDialogListener {
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(
                getActivity(),
                (view, hourOfDay, minuteOfHour) -> callback.onTimeSet(view, hourOfDay, minuteOfHour),
                hour,
                minute,
                DateFormat.is24HourFormat(getActivity())
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (TimePickerDialogListener) context;
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
