package ru.job4j.mvc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment {

    private ConfirmationDialogListener callback;

    public interface ConfirmationDialogListener {
        void onPositiveDialogClick(DialogFragment dialog);

        void onNegativeDialogClick(DialogFragment dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Вопрос")
                .setPositiveButton(R.string.yes, (dialog, which) -> callback.onPositiveDialogClick(ConfirmationDialog.this))
                .setNegativeButton(R.string.no, (dialog, which) -> callback.onNegativeDialogClick(ConfirmationDialog.this))
                .setMessage("Вы уверены, что хотите воспользоваться подсказкой?");
        return adb.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ConfirmationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format("%s must implement ConfirmationDialogListener", context.toString()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
