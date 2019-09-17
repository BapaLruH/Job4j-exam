package ru.job4j.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TaskViewActivity extends AppCompatActivity implements TaskViewFragment.OnUpdateButtonClickListener {
    private Fragment taskViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        Task task = (Task) getIntent().getSerializableExtra("task");
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (task != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("task", task);
            if (taskViewFragment == null) {
                taskViewFragment = new TaskViewFragment();
            }
            taskViewFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, taskViewFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onUpdateBtnClicked(Task task) {
        Intent updateTaskIntent = new Intent(this, TaskChangeActivity.class);
        updateTaskIntent.putExtra("task", task);
        startActivity(updateTaskIntent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}