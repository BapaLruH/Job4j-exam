package ru.job4j.workersstore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ProfessionAdapter extends RecyclerView.Adapter<ProfessionAdapter.ProfHolder> {
    private List<Profession> profList;
    private OnProfessionChangeListener callback;

    public ProfessionAdapter(Context context) {
        try {
            callback = (OnProfessionChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format("%s must implement OnProfessionChangeListener", context.toString()));
        }
    }

    public void setProfList(List<Profession> profList) {
        this.profList = profList;
        notifyDataSetChanged();
    }

    public void removeProfession(Profession profession) {
        this.profList.remove(profession);
        notifyDataSetChanged();
    }

    public void addNewProfession(Profession profession) {
        this.profList.add(profession);
        notifyDataSetChanged();
    }

    public void updateProfessionList(Profession profession) {
        int index = getProfById(profession.getId());
        if (index != -1) {
            this.profList.set(index, profession);
            notifyItemChanged(index);
        }
    }

    private int getProfById(int id) {
        int index = -1;
        for (int i = 0; i < this.profList.size(); i++) {
            if (this.profList.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static class ProfHolder extends RecyclerView.ViewHolder {
        private View view;

        public ProfHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }

    public interface OnProfessionChangeListener {
        void onProfessionRemoveClick(Profession prof);
        void onProfessionIdSelectClickListener(Profession prof);
    }

    @NonNull
    @Override
    public ProfHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.profession_row, parent, false);
        return new ProfHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfHolder profHolder, int i) {
        final Profession prof = this.profList.get(i);
        TextView tvCode = profHolder.view.findViewById(R.id.tvProfCode);
        TextView tvName = profHolder.view.findViewById(R.id.tvProfName);
        Button btnRemove = profHolder.view.findViewById(R.id.btnRemoveProf);
        tvCode.setText(String.valueOf(prof.getSpecialtyCode()));
        tvName.setText(prof.getName());
        btnRemove.setOnClickListener(v -> callback.onProfessionRemoveClick(prof));
        tvCode.setOnClickListener(v -> callback.onProfessionIdSelectClickListener(prof));
        tvName.setOnClickListener(v -> {
            Intent intent = new Intent(profHolder.view.getContext(), WorkersActivity.class);
            intent.putExtra("profession", prof);
            profHolder.view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.profList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        callback = null;
    }
}
