package ru.job4j.todolist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class TaskViewFragment extends Fragment {
    private OnUpdateButtonClickListener callback;
    private Task task;

    public interface OnUpdateButtonClickListener {
        void onUpdateBtnClicked(Task task);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_reviewer, container, false);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvDesc = view.findViewById(R.id.tvDesc);
        TextView tvCreated = view.findViewById(R.id.tvCreated);
        TextView tvClosed = view.findViewById(R.id.tvClosed);
        btnUpdate.setOnClickListener(this::onClickUpdate);
        Bundle arguments = getArguments();
        if (arguments != null) {
            task = (Task) arguments.getSerializable("task");
            tvTitle.setText(String.format("Задание: %s", task.getName()));
            tvName.setText(task.getName());
            tvDesc.setText(task.getDescription());
            tvCreated.setText(task.getCreated() > 0 ? parseDateFormat(task.getCreated()) : "");
            tvClosed.setText(task.getClosed() > 0 ? parseDateFormat(task.getClosed()) : "");
        }
        return view;
    }

    private String parseDateFormat(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return String.format(Locale.getDefault(), "%02d.%02d.%d", day, month, year);
    }

    private void onClickUpdate(View view) {
        callback.onUpdateBtnClicked(task);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnUpdateButtonClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}