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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ProfessionAdapter.OnProfessionChangeListener {
    private RecyclerView recyclerView;
    private ProfessionAdapter profAdapter;
    private DBStore store;
    static final int VERSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_activity);
        this.store = DBStore.getInstance(this, VERSION);
        this.recyclerView = findViewById(R.id.recProf);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initProfessionsAdapter();
        updateProfAdapterList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_profession:
                addNewProfession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewProfession() {
        Intent intent = new Intent(getApplicationContext(), ProfessionUpdaterActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        }
        Profession profession = (Profession) data.getSerializableExtra("profession");
        if (profession != null) {
            if (profession.getId() > 0) {
                if (store.updateProfession(profession.getId(), profession) != null) {
                    this.profAdapter.updateProfessionList(profession);
                }
            } else {
                profession = store.createProfession(profession);
                if (profession.getId() > 0) {
                    this.profAdapter.addNewProfession(profession);
                }
            }
        }
    }

    private void initProfessionsAdapter() {
        this.profAdapter = new ProfessionAdapter(this);
        this.recyclerView.setAdapter(this.profAdapter);
    }

    private void updateProfAdapterList() {
        this.profAdapter.setProfList(store.findAllProfessions());
    }

    @Override
    public void onProfessionRemoveClick(Profession prof) {
        if (store.deleteProfession(prof.getId())) {
            profAdapter.removeProfession(prof);
        } else {
            Toast.makeText(this, "Не удалось удалить запись. Проверьте, нет ли пользователей, зависимых от данной профессии!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProfessionIdSelectClickListener(Profession prof) {
        Intent intent = new Intent(getApplicationContext(), ProfessionUpdaterActivity.class);
        intent.putExtra("profession", prof);
        startActivityForResult(intent, 1);
    }
}
