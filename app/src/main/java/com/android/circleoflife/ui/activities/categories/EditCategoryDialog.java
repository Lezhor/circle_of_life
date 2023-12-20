package com.android.circleoflife.ui.activities.categories;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.circleoflife.R;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.other.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.function.Consumer;

public class EditCategoryDialog extends AppCompatDialogFragment {

    private TextInputLayout nameInput;
    private TextInputLayout parentInput;
    private final Consumer<Category> submit;
    private final Category category;

    public EditCategoryDialog(Consumer<Category> submit, @NonNull Category category) {
        super();
        this.submit = submit;
        this.category = new Category(category);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_create_dialog, null);

        builder.setView(view)
                .setTitle(R.string.category_dialog_edit_title)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.dialog_ok, null);



        nameInput = view.findViewById(R.id.category_create_dialog_name);
        nameInput.setHint(R.string.category_dialog_edit_name_hint);
        EditText editText = nameInput.getEditText();
        editText.setText(category.getName());
        editText.setSelectAllOnFocus(true);
        editText.requestFocus();

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        parentInput = view.findViewById(R.id.category_create_dialog_parent);
        if (category.getParentID() != null) {
            // parentInput.getEditText().setText("Not Null"); // TODO: 20.12.2023 set to old parent
        }

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
                String parentString = parentInput.getEditText().getText().toString().trim();
                if (parentString.isEmpty() || parentString.equalsIgnoreCase("null")) {
                    parentString = null;
                }
                if (TextInputLayoutValidator.validate(nameInput, StringValidator::validateString, "Category")) {
                    String nameString = nameInput.getEditText().getText().toString().trim();
                    category.setName(nameString);
                    submit.accept(category);
                    dialog.dismiss();
                }
            });
        }
    }
}
