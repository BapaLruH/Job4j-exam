package ru.job4j.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ExamsActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener {
    private RecyclerView recycler;
    private ExamAdapter examAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams);
        this.recycler = findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
    }


    private void updateUI() {
        List<Exam> exams = new ArrayList<>();
        for (int index = 1; index != 100; index++) {
            exams.add(new Exam(
                            index,
                            String.format("Exam %s", index),
                            System.currentTimeMillis(),
                            index
                    )
            );
        }
        examAdapter = new ExamAdapter(exams);
        this.recycler.setAdapter(examAdapter);
    }

    public void onClickDateTimeFragment(View view) {
        startActivity(new Intent(this, DialogsActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exams_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                addNewElement();
                return true;
            case R.id.delete_item:
                removeAllElements();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNewElement() {
        int index = examAdapter.getSizeAdapter() + 1;
        Exam exam = new Exam(
                index,
                String.format("Exam %s", index),
                System.currentTimeMillis(),
                index
        );
        examAdapter.addNewElement(exam);
    }

    public void removeAllElements() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        Bundle bundle = new Bundle();
        bundle.putString("dialog_text", "Удалить всё?");
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "removeAllExams");
    }

    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        examAdapter.removeAllExams();
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
    }
}