package ru.job4j.workersstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class WorkersActivity extends AppCompatActivity implements WorkerAdapter.OnWorkerChangeListener {
    private RecyclerView recWorkers;
    private WorkerAdapter workerAdapter;
    private DBStore store;
    private Profession profession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_workers_activity);
        this.store = DBStore.getInstance(getApplicationContext(), MainActivity.VERSION);
        this.recWorkers = findViewById(R.id.recWork);
        this.recWorkers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Profession profession = (Profession) getIntent().getSerializableExtra("profession");
        if (profession != null) {
            initWorkerAdapter();
            updateAdapterList(profession);
            this.profession = profession;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_workers_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_worker) {
            addNewWorker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewWorker() {
        Intent intent = new Intent(getApplicationContext(), WorkerUpdaterActivity.class);
        intent.putExtra("profession", profession);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        }
        Worker worker = (Worker) data.getSerializableExtra("worker");
        if (worker != null) {
            if (worker.getId() != 0) {
                if (store.updateWorker(worker.getId(), worker) != null) {
                    this.workerAdapter.updateWorkerList(worker);
                }
            } else {
                worker = store.createWorker(worker);
                if (worker.getId() != 0) {
                    this.workerAdapter.addNewWorker(worker);
                }
            }
        }
    }

    private void updateAdapterList(Profession profession) {
        this.workerAdapter.setWorkers(store.findWorkersByProfession(profession));
    }

    private void initWorkerAdapter() {
        this.workerAdapter = new WorkerAdapter(this);
        this.recWorkers.setAdapter(this.workerAdapter);
    }

    @Override
    public void onWorkerRemoveClick(Worker worker) {
        if (store.deleteWorker(worker.getId())) {
            this.workerAdapter.removeWorker(worker);
        }
    }

    @Override
    public void onWorkerRowClick(Worker worker) {
        Intent intent = new Intent(getApplicationContext(), WorkerUpdaterActivity.class);
        intent.putExtra("worker", worker);
        intent.putExtra("profession", profession);
        startActivityForResult(intent, 1);
    }
}
