package ru.job4j.todolist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> tasks;
    private OnTaskRemoveClickListener onTaskRemoveClickListener;

    public TaskAdapter(OnTaskRemoveClickListener onTaskRemoveClickListener) {
        this.onTaskRemoveClickListener = onTaskRemoveClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_to_do_list, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int i) {
        final Task task = this.tasks.get(i);
        TextView tvName = holder.view.findViewById(R.id.tvName);
        TextView tvDesc = holder.view.findViewById(R.id.tvDesc);
        TextView tvCreated = holder.view.findViewById(R.id.tvCreated);
        TextView tvClosed = holder.view.findViewById(R.id.tvClosed);
        Button btnDelete = holder.view.findViewById(R.id.delete);
        btnDelete.setOnClickListener(v -> onTaskRemoveClickListener.onTaskRemoveClick(task));
        String name = task.getName();
        String desc = task.getDescription();
        tvName.setText(name.length() > 15 ? name.substring(0, 15) + "..." : name);
        tvDesc.setText(desc.length() > 15 ? desc.substring(0, 15) + "..." : desc);
        tvCreated.setText(task.getCreated() > 0 ? parseDateFormat(task.getCreated()) : "");
        tvClosed.setText(task.getClosed() > 0 ? parseDateFormat(task.getClosed()) : "");
        tvName.setOnClickListener(v -> {
            Intent intent = new Intent(holder.view.getContext(), TaskViewActivity.class);
            intent.putExtra("task", task);
            holder.view.getContext().startActivity(intent);
        });
    }

    private String parseDateFormat(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return String.format(Locale.getDefault(), "%02d.%02d.%d", day, month, year);
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {
        private View view;

        public TaskHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }

    public interface OnTaskRemoveClickListener {
        void onTaskRemoveClick(Task task);
    }
}