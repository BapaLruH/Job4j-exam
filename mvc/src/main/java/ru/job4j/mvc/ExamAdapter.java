package ru.job4j.mvc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamHolder> {
    private final List<Exam> exams;

    public ExamAdapter(List<Exam> exams) {
        this.exams = exams;
    }

    @NonNull
    @Override
    public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.info_exam, parent, false);
        return new ExamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHolder holder, int i) {
        final Exam exam = this.exams.get(i);
        TextView text = holder.view.findViewById(R.id.tvInfo);
        TextView result = holder.view.findViewById(R.id.tvResult);
        TextView date = holder.view.findViewById(R.id.tvDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        result.setText(String.valueOf(exam.getResult()));
        date.setText(sdf.format(new Date(exam.getTime())));
        text.setText(exam.getName());
        text.setOnClickListener(v -> {
            Toast.makeText(holder.view.getContext(), "You select " + exam, Toast.LENGTH_SHORT).show();
            holder.view.getContext().startActivity(new Intent(holder.view.getContext(), MainActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return this.exams.size();
    }

    public static class ExamHolder extends RecyclerView.ViewHolder {
        private View view;

        public ExamHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }
}