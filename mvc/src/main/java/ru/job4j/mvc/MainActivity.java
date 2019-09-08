package ru.job4j.mvc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Question> questions = Arrays.asList(
            new Question(
                    1, "How many primitive variables does Java have?",
                    Arrays.asList(
                            new Option(1, "1.1"), new Option(2, "1.2"),
                            new Option(3, "1.3"), new Option(4, "1.4")
                    ), 4
            ),
            new Question(
                    2, "What is Java Virtual Machine?",
                    Arrays.asList(
                            new Option(1, "2.1"), new Option(2, "2.2"),
                            new Option(3, "2.3"), new Option(4, "2.4")
                    ), 4
            ),
            new Question(
                    3, "What is happen if we try unboxing null?",
                    Arrays.asList(
                            new Option(1, "3.1"), new Option(2, "3.2"),
                            new Option(3, "3.3"), new Option(4, "3.4")
                    ), 4
            )
    );
    private int[] selectedAnswers = new int[questions.size()];
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fillForm();
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showAnswer()) {
                    position++;
                    fillForm();
                }

            }
        });
        Button previous = findViewById(R.id.prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                fillForm();
            }
        });
    }

    private void fillForm() {
        findViewById(R.id.prev).setEnabled(position != 0);
        Button next = findViewById(R.id.next);
        next.setEnabled(position != questions.size());
        if (position == questions.size() - 1) {
            next.setText(R.string.complete);
        } else {
            next.setText(R.string.next);
        }
        final TextView text = findViewById(R.id.question);
        RadioGroup variants = findViewById(R.id.variants);
        if (position != questions.size()){
            Question question = this.questions.get(this.position);
            text.setText(question.getText());
            variants.clearCheck();
            for (int i = 0; i != variants.getChildCount(); i++) {
                RadioButton button = (RadioButton) variants.getChildAt(i);
                button.setVisibility(View.VISIBLE);
                Option option = question.getOptions().get(i);
                button.setId(option.getId());
                button.setText(option.getText());
            }
            variants.check(selectedAnswers[position]);
        } else {
            text.setText(String.format("Selected answers: %s", Arrays.toString(selectedAnswers)));
            for (int i = 0; i != variants.getChildCount(); i++) {
                variants.getChildAt(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        String text = "Please select one of the following options";
        boolean executionResult = false;
        if (id != -1) {
            Question question = this.questions.get(position);
            text = "Your answer is " + id + ", correct is " + question.getAnswer();
            selectedAnswers[position] = id;
            executionResult = true;
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return executionResult;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.position = savedInstanceState.getInt("position");
        this.selectedAnswers = savedInstanceState.getIntArray("selectedAnswers");
        fillForm();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", this.position);
        outState.putIntArray("selectedAnswers", this.selectedAnswers);
    }
}
