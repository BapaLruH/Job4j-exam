package ru.job4j.todolist;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TaskChangeActivity extends AppCompatActivity implements TaskChangeFragment.OnSaveButtonClickListener {
    private ToDoStore store;
    private Task task;
    private Fragment taskChangeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creator);
        store = ToDoStoreImpl.getInstance(getApplicationContext(), 1);
        task = (Task) getIntent().getSerializableExtra("task");
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (taskChangeFragment == null) {
            taskChangeFragment = new TaskChangeFragment();
        }
        if (task != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("task", task);
            taskChangeFragment.setArguments(bundle);
        }
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, taskChangeFragment)
                .addToBackStack(null)
                .commit();
    }

    public int updateTask(Task task) {
        return store.update(task.getId(), task);
    }

    @Override
    public void onSaveBtnClicked(Task rslTask) {
        if (rslTask.getName().isEmpty()) {
            DialogFragment dialog = new EmptyNameErrorDialog();
            dialog.show(getSupportFragmentManager(), "EmptyNameError");
        } else {
            String action = "";
            if (task != null) {
                rslTask.setId(task.getId());
                if (!rslTask.equals(this.task)) {
                    int updatedTasks = updateTask(rslTask);
                    if (updatedTasks > 0) {
                        action = "update";
                    }
                }
            } else {
                rslTask = createTask(rslTask);
                if (rslTask.getId() > 0) {
                    action = "add";
                }
            }
            Intent mainIntent = new Intent(getApplicationContext(), ToDoListActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.putExtra("action", action);
            mainIntent.putExtra("task", rslTask);
            startActivity(mainIntent);
        }
    }

    public Task createTask(Task task) {
        return store.create(task);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}
