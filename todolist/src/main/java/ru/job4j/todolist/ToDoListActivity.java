package ru.job4j.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private TaskAdapter taskAdapter;
    private ToDoStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks);
        store = ToDoStoreImpl.getInstance(this, 1);
        this.recycler = findViewById(R.id.tasks);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initTaskAdapter();
        updateAdapterList();
    }

    private void updateAdapterList() {
        List<Task> tasks = store.findAll();
        taskAdapter.setTasks(tasks);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateAdapterList();
    }

    private void initTaskAdapter() {
        TaskAdapter.OnTaskRemoveClickListener taskRemoveClickListener = task -> {
            if (store.delete(task.getId()) > 0) {
                taskAdapter.removeTask(task);
            }
        };
        this.taskAdapter = new TaskAdapter(taskRemoveClickListener);
        this.recycler.setAdapter(taskAdapter);
    }

    public void onClickBtnCreate(View view) {
        startActivity(new Intent(this, TaskChangeActivity.class));
    }
}