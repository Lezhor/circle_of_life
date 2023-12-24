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
import com.android.circleoflife.database.models.additional.Nameable;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.other.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.function.Consumer;

public class EditNameDialog<E extends Nameable> extends AppCompatDialogFragment {

    private TextInputLayout nameInput;
    private final Consumer<E> submit;
    private final E entity;
    private final int entityTypeNameResId;

    /**
     * Constructor for editNameDialog<br>
     * @param submit submit method. e.g. update in database
     * @param entity this entity should be a copy of the original, because it gets changed here!
     */
    public EditNameDialog(Consumer<E> submit, @NonNull E entity, int entityTypeNameResId) {
        super();
        this.submit = submit;
        this.entity = entity;
        this.entityTypeNameResId = entityTypeNameResId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_name_dialog, null);

        builder.setView(view)
                .setTitle(getString(R.string.dialog_edit_title, getString(entityTypeNameResId)))
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.dialog_ok, null);



        nameInput = view.findViewById(R.id.edit_name_dialog_input);
        nameInput.setHint(R.string.dialog_edit_name_hint);
        EditText editText = nameInput.getEditText();
        editText.setText(entity.getName());
        editText.setSelectAllOnFocus(true);
        editText.requestFocus();
        
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
                if (TextInputLayoutValidator.validate(nameInput, StringValidator::validateString, getString(entityTypeNameResId))) {
                    String nameString = nameInput.getEditText().getText().toString().trim();
                    entity.setName(nameString);
                    submit.accept(entity);
                    dialog.dismiss();
                }
            });
        }
    }
}
