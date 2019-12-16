package ru.job4j.retrofitexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        int code = getIntent().getIntExtra("code", -1);
        if (code != -1) {
            ((TextView)findViewById(R.id.error_message)).setText(String.format("Error, code response: %s", code));
        }
    }

    public void back(View view) {
        onBackPressed();
    }
}
