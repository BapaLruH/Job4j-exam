package ru.job4j.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ExamsActivity extends AppCompatActivity {
    private RecyclerView recycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams);
        this.recycler = findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
    }


    private void updateUI() {
        List<Exam> exams =new ArrayList<>();
        for (int index = 0; index != 100; index++) {
            exams.add(new Exam(
                    index,
                    String.format("Exam %s", index),
                    System.currentTimeMillis(),
                    index
                    )
            );
        }
        this.recycler.setAdapter(new ExamAdapter(exams));
    }

    public void onClickDateTimeFragment(View view) {
        startActivity(new Intent(this, DialogsActivity.class));
    }
}