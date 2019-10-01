package ru.job4j.workersstore;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ProfessionUpdaterActivity extends AppCompatActivity {
    private Profession profession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_profession_updater_activity);

        FragmentManager fm = getSupportFragmentManager();
        Fragment updaterFragment = fm.findFragmentById(R.id.prof_updater_fragment_container);
        Profession profession = (Profession) getIntent().getSerializableExtra("profession");
        if (updaterFragment == null) {
            updaterFragment = new ProfessionUpdaterFragment();
            if (profession != null) {
                Bundle bundle = new Bundle();
                this.profession = profession;
                bundle.putSerializable("profession", profession);
                updaterFragment.setArguments(bundle);
            }
            fm.beginTransaction()
                    .add(R.id.prof_updater_fragment_container, updaterFragment)
                    .commit();
        }
    }

    public void onClickSaveProf(View view) {
        Intent data = new Intent();
        setResult(RESULT_CANCELED);
        String name = ((EditText)findViewById(R.id.e_prof_name)).getText().toString();
        int specialtyCode = Integer.parseInt(((EditText)findViewById(R.id.e_prof_specialty_code)).getText().toString());
        if (profession != null) {
            if (!profession.getName().equals(name) || profession.getSpecialtyCode() != specialtyCode) {
                profession.setName(name);
                profession.setSpecialtyCode(specialtyCode);
                data.putExtra("profession", profession);
                setResult(RESULT_OK, data);
            }
        } else {
              if (!name.isEmpty() && specialtyCode != 0) {
                  Profession resProf = new Profession(name, specialtyCode);
                  data.putExtra("profession", resProf);
                  setResult(RESULT_OK, data);
              }
        }
        finish();
    }

    public void onClickCancelProf(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
