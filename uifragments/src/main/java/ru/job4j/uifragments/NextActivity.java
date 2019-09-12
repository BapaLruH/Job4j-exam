package ru.job4j.uifragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NextActivity extends AppCompatActivity {

    private FragmentManager fm;
    private Fragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        fm = getSupportFragmentManager();
        secondFragment = fm.findFragmentById(R.id.fragment_container_next);
        String message =getIntent().getStringExtra("message");
        if (secondFragment == null) {
            secondFragment = new SecondFragment();
            if (message != null){
                Bundle bundle = new Bundle();
                bundle.putString("message", message);
                secondFragment.setArguments(bundle);
            }
            fm.beginTransaction()
                    .add(R.id.fragment_container_next, secondFragment)
                    .commit();
        }
    }

    public void onClickBack(View view) {
        Intent data = new Intent();
        data.putExtra("message", "Back button clicked");
        setResult(RESULT_OK, data);
        finish();
    }
}
