package ru.job4j.workersstore;

import android.content.Context;
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

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerHolder> {
    private List<Worker> workers;
    private OnWorkerChangeListener callback;

    public WorkerAdapter(Context context) {
        try {
            callback = (OnWorkerChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format("%s must implement OnWorkerChangeListener", context.toString()));
        }
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
        notifyDataSetChanged();
    }

    public void removeWorker(Worker worker) {
        this.workers.remove(worker);
        notifyDataSetChanged();
    }

    public void addNewWorker(Worker worker) {
        this.workers.add(worker);
        notifyDataSetChanged();
    }

    public void updateWorkerList(Worker worker) {
        int index = getWorkerById(worker.getId());
        if (index != -1) {
            this.workers.set(index, worker);
            notifyItemChanged(index);
        }
    }

    private int getWorkerById(int id) {
        int index = -1;
        for (int i = 0; i < this.workers.size(); i++) {
            if (this.workers.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static class WorkerHolder extends RecyclerView.ViewHolder {
        private View view;

        public WorkerHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }

    public interface OnWorkerChangeListener {
        void onWorkerRemoveClick(Worker worker);

        void onWorkerRowClick(Worker worker);
    }

    @NonNull
    @Override
    public WorkerHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.worker_row, parent, false);
        return new WorkerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerHolder holder, int i) {
        final Worker worker = this.workers.get(i);
        TextView tvFirstLastName = holder.view.findViewById(R.id.tvFirstLastName);
        TextView tvProfession = holder.view.findViewById(R.id.tvProfession);
        TextView tvBirthDay = holder.view.findViewById(R.id.tvDate);
        Button btnRemove = holder.view.findViewById(R.id.btnRemove);
        tvFirstLastName.setText(String.format("%s %s", worker.getLastName(), worker.getFirstName()));
        tvProfession.setText(worker.getProfession() != null ? worker.getProfession().getName() : "");
        tvBirthDay.setText(parseDate(worker.getDateOfBirth()));
        btnRemove.setOnClickListener(v -> callback.onWorkerRemoveClick(worker));
        holder.view.setOnClickListener(v -> callback.onWorkerRowClick(worker));
    }

    private String parseDate(long time) {
        String result = "";
        if (time != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            result = String.format(Locale.getDefault(), "%02d.%02d.%d", day, month, year);
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return this.workers.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        callback = null;
    }
}
