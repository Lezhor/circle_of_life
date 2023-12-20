package com.android.circleoflife.ui.activities.categories;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.circleoflife.R;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.other.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputLayout;

public class CreateCategoryDialog extends AppCompatDialogFragment {

    private TextInputLayout nameInput;
    private final OnResultSubmitListener submit;

    public CreateCategoryDialog(OnResultSubmitListener listener) {
        super();
        this.submit = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_create_dialog, null);

        builder.setView(view)
                .setTitle(R.string.category_dialog_title)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.dialog_ok, null);



        nameInput = view.findViewById(R.id.category_create_dialog_name);
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
                if (TextInputLayoutValidator.validate(nameInput, StringValidator::validateString, "Category")) {
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
