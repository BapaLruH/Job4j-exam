package ru.job4j.uifragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;
    private Fragment firstFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        firstFragment = fm.findFragmentById(R.id.fragment_container);
        if (firstFragment == null) {
            firstFragment = new FirstFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, firstFragment)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {return;}
        String message = data.getStringExtra("message");
        TextView tvMessage = firstFragment.getView().findViewById(R.id.tvMessage);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, NextActivity.class);
        intent.putExtra("message", "Next button clicked");
        startActivityForResult(intent, 1);
    }
}
