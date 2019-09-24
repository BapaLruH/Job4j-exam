package ru.job4j.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener {

    private final QuestionsStorage storage = QuestionsStorage.getInstance();
    private int[] selectedAnswers = new int[storage.size()];
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fillForm();
    }

    private void fillForm() {
        findViewById(R.id.prev).setEnabled(position != 0);
        Button next = findViewById(R.id.next);
        if (position == storage.size() || position == storage.size() - 1) {
            next.setText(R.string.complete);
        } else {
            next.setText(R.string.next);
        }
        final TextView text = findViewById(R.id.question);
        RadioGroup variants = findViewById(R.id.variants);
        if (position != storage.size()) {
            Question question = this.storage.getQuestion(this.position);
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
            Intent resultActivity = new Intent(this, ResultActivity.class);
            resultActivity.putExtra("questions", (Serializable) storage.getAllQuestions());
            resultActivity.putExtra("answers", selectedAnswers);
            startActivity(resultActivity);
            position--;
        }
    }

    private boolean showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        boolean executionResult = true;
        selectedAnswers[position] = id;
        if (id == 0) {
            executionResult = false;
            Toast.makeText(this, "Please select one of the following options", Toast.LENGTH_SHORT).show();
        }
        return executionResult;
    }

    public void btnNext(View view) {
        if (showAnswer()) {
            position++;
            fillForm();
        }
    }

    public void btnPrev(View view) {
        position--;
        fillForm();
    }

    public void btnHint(View view) {
        DialogFragment confDialog = new ConfirmationDialog();
        confDialog.show(getSupportFragmentManager(), "ConfirmationDialog");
    }

    public void btnBack(View view) {
        onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", this.position);
        outState.putIntArray("selectedAnswers", this.selectedAnswers);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.position = savedInstanceState.getInt("position");
        this.selectedAnswers = savedInstanceState.getIntArray("selectedAnswers");
        fillForm();
    }

    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        int answer = storage.getQuestion(this.position).getAnswer();
        String question = storage.getQuestion(this.position).getText();
        Intent hintIntent = new Intent(this, HintActivity.class);
        hintIntent.putExtra("HINT_FOR", question);
        hintIntent.putExtra("answer", answer);
        startActivity(hintIntent);
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
        Toast.makeText(this, "Молодец!", Toast.LENGTH_SHORT).show();
    }
}
