package ru.job4j.mvc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Вопрос")
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .setMessage("Вы уверены, что хотите воспользоваться подсказкой?");
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            Intent hintIntent = new Intent(getActivity(), HintActivity.class);
            Bundle bundle = this.getArguments();
            int answer = 0;
            String question = "";
            if (bundle != null) {
                question = bundle.getString("question");
                answer = bundle.getInt("answer");

            }
            hintIntent.putExtra("HINT_FOR", question);
            hintIntent.putExtra("answer", answer);
            startActivity(hintIntent);
        }
    }
}
