package ru.job4j.todolist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class TaskChangeFragment extends Fragment {
    private OnSaveButtonClickListener callback;
    private EditText etName, etDesc;
    private TextView tvCreated, tvClosed;

    public interface OnSaveButtonClickListener {
        void onSaveBtnClicked(Task task);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_updater, container, false);
        Button btnSave = view.findViewById(R.id.btnSave);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        etName = view.findViewById(R.id.etName);
        etDesc = view.findViewById(R.id.etDesc);
        tvCreated = view.findViewById(R.id.tvCreated);
        tvClosed = view.findViewById(R.id.tvClosed);
        CheckBox cbCreated = view.findViewById(R.id.cbCreatedDate);
        CheckBox cbClosed = view.findViewById(R.id.cBClosedDate);
        btnSave.setOnClickListener(this::onClickSave);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Task task = (Task) arguments.getSerializable("task");
            tvTitle.setText(String.format("Задание: %s", task.getName()));
            etName.setText(task.getName());
            etDesc.setText(task.getDescription());
            tvCreated.setText(task.getCreated() > 0 ? parseTextFromDate(task.getCreated()) : "");
            cbCreated.setChecked(task.getCreated() > 0);
            cbCreated.setEnabled(task.getCreated() == 0);
            tvCreated.setOnClickListener(this::onSelectDate);
            tvClosed.setText(task.getClosed() > 0 ? parseTextFromDate(task.getClosed()) : "");
            cbClosed.setChecked(task.getClosed() > 0);
            tvClosed.setOnClickListener(this::onSelectDate);
        }
        cbCreated.setOnCheckedChangeListener(this::onCheckedChangedBox);
        cbClosed.setOnCheckedChangeListener(this::onCheckedChangedBox);
        return view;
    }

    private void onClickSave(View view) {
        long dCreated = parseDateFromText(tvCreated.getText().toString());
        long dClosed = parseDateFromText(tvClosed.getText().toString());
        callback.onSaveBtnClicked(
                new Task(
                        etName.getText().toString(),
                        etDesc.getText().toString(),
                        dCreated,
                        dClosed
                )
        );
    }

    private void onCheckedChangedBox(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cbCreatedDate:
                setDateOnTextView((CheckBox) buttonView, tvCreated);
                break;
            case R.id.cBClosedDate:
                setDateOnTextView((CheckBox) buttonView, tvClosed);
                break;
        }
    }

    private void setDateOnTextView(CheckBox view, TextView tv) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (view.isChecked()) {
            tv.setText(
                    String.format(Locale.getDefault(), "%02d.%02d.%d", day, month + 1, year)
            );
        } else {
            tv.setText("");
        }
    }

    private void onSelectDate(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        TextView tvView = (TextView) view;
        DatePickerDialog picker = new DatePickerDialog(tvView.getContext(), (dPView, year1, month1, dayOfMonth) -> tvView.setText(
                String.format(Locale.getDefault(), "%02d.%02d.%d", dayOfMonth, month1 + 1, year1)), year, month, day);
        picker.show();
    }

    private long parseDateFromText(String text) {
        String[] strings = text.split("\\.");
        long time = 0;
        if (strings.length > 2) {
            Calendar cal = Calendar.getInstance();
            cal.set(
                    Integer.parseInt(strings[2]),
                    Integer.parseInt(strings[1]) - 1,
                    Integer.parseInt(strings[0])
            );
            time = cal.getTime().getTime();
        }
        return time;
    }

    private String parseTextFromDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return String.format(Locale.getDefault(), "%02d.%02d.%d", day, month, year);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnSaveButtonClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}