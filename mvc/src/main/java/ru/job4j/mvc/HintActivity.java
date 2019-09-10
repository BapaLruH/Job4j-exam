package ru.job4j.mvc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class HintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        TextView tvHint = findViewById(R.id.hint);
        tvHint.setText(getIntent().getStringExtra("HINT_FOR"));
        TextView tvAnswer = findViewById(R.id.tvAnswer);
        tvAnswer.setText(String.format("Correct answer: %d", getIntent().getIntExtra("answer", 0)));
    }

    public void btnBack(View view) {
        onBackPressed();
    }
}
