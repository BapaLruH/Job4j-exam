package ru.job4j.mvc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int[] answers = getIntent().getIntArrayExtra("answers");
        ArrayList<Question> questions = (ArrayList<Question>) getIntent().getSerializableExtra("questions");
        ArrayList<Map<String, String>> list = new ArrayList<>();
        if (answers != null && questions != null) {
            for (int i = 0; i < answers.length; i++) {
                Map<String, String> map = new HashMap<>();
                map.put("Вопрос", questions.get(i).getText());
                map.put("Ответ", String.valueOf(answers[i] == questions.get(i).getAnswer()));
                list.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    list, android.R.layout.simple_list_item_2,
                    new String[]{"Вопрос", "Ответ"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            ListView lvResult = findViewById(R.id.lvResult);
            lvResult.setAdapter(adapter);
        }

    }

    public void btnBack(View view) {
        onBackPressed();
    }
}
