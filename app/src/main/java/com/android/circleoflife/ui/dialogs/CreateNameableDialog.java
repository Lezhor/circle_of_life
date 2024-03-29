package com.android.circleoflife.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.circleoflife.R;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.other.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputLayout;

public class CreateNameableDialog extends AppCompatDialogFragment {

    private TextInputLayout nameInput;
    @StringRes
    private final int nameableType;
    private final OnResultSubmitListener submit;

    public CreateNameableDialog(@StringRes int nameableType, OnResultSubmitListener listener) {
        super();
        this.nameableType = nameableType;
        this.submit = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_name_dialog, null);

        builder.setView(view)
                .setTitle(getString(R.string.dialog_create_new, getString(nameableType)))
                .setNegativeButton(R.string.dialog_cancel, (dialog, which) -> {})
                .setPositiveButton(R.string.dialog_ok, null);



        nameInput = view.findViewById(R.id.edit_name_dialog_input);
        nameInput.getEditText().requestFocus();

        Dialog result = builder.create();
        result.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String nameString = nameInput.getEditText().getText().toString().trim();
                if (TextInputLayoutValidator.validate(nameInput, StringValidator::validateString, getString(nameableType))) {
                    submit.trigger(nameString);
                    dialog.dismiss();
                }
            });
        }
    }


    @FunctionalInterface
    public interface OnResultSubmitListener {
        void trigger(String name);
    }
}
