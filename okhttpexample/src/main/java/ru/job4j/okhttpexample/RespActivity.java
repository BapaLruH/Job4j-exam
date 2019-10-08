package ru.job4j.okhttpexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

public class RespActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resp);
        TextView tvResponseText = findViewById(R.id.tvResponseText);
        String[] authResult = getIntent().getStringArrayExtra("authResult");
        if (authResult != null) {
            tvResponseText.setText(Arrays.toString(authResult));
        }
    }
}
