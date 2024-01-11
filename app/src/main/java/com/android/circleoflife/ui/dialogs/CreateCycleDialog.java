package com.android.circleoflife.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.circleoflife.R;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.additional.CycleFrequency;
import com.android.circleoflife.database.validators.StringValidator;
import com.android.circleoflife.ui.other.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputLayout;

public class CreateCycleDialog extends AppCompatDialogFragment {

    private TextInputLayout nameInput;
    private final SubmitAction submit;
    private final Cycle passedCycle;
    private final CycleFrequency frequency;

    /**
     * Constructor for cycle dialog.
     * @param cycle passed cycle. if null this is a create dialog, if not null, an edit dialog
     * @param callback what happens after dialog-ok is pressed. e.g. upload to database
     */
    public CreateCycleDialog(@Nullable Cycle cycle, @NonNull SubmitAction callback) {
        super();
        this.submit = callback;
        this.passedCycle = cycle;
        frequency = passedCycle == null ? new CycleFrequency() : new CycleFrequency(passedCycle.getFrequency());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_cycle_dialog, null);

        String title;
        if (passedCycle == null) {
            title = getString(R.string.dialog_create_new) + " " + getString(R.string.cycle);
        } else {
            title = getString(R.string.dialog_edit_title, getString(R.string.cycle));
        }

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton(R.string.dialog_cancel, (dialog, which) -> {})
                .setPositiveButton(R.string.dialog_ok, null);



        nameInput = view.findViewById(R.id.edit_name_dialog_input);
        EditText editText = nameInput.getEditText();
        if (editText != null) {
            if (passedCycle != null) {
                editText.setText(passedCycle.getName());
                editText.setSelectAllOnFocus(true);
            }
            editText.requestFocus();
        }

        setUpCheckBoxes(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String nameString = nameInput.getEditText().getText().toString().trim();
                if (TextInputLayoutValidator.validate(nameInput, StringValidator::validateString, getString(R.string.cycle))) {
                    submit.trigger(nameString, new CycleFrequency(frequency));
                    dialog.dismiss();
                }
            });
        }
    }

    private void setUpCheckBoxes(View view) {
        setUpCheckbox(
                view.findViewById(R.id.checkbox_monday),
                view.findViewById(R.id.option_monday),
                CycleFrequency.MASK_MONDAY
        );
        setUpCheckbox(
                view.findViewById(R.id.checkbox_tuesday),
                view.findViewById(R.id.option_tuesday),
                CycleFrequency.MASK_TUESDAY
        );
        setUpCheckbox(
                view.findViewById(R.id.checkbox_wednesday),
                view.findViewById(R.id.option_wednesday),
                CycleFrequency.MASK_WEDNESDAY
        );
        setUpCheckbox(
                view.findViewById(R.id.checkbox_thursday),
                view.findViewById(R.id.option_thursday),
                CycleFrequency.MASK_THURSDAY
        );
        setUpCheckbox(
                view.findViewById(R.id.checkbox_friday),
                view.findViewById(R.id.option_friday),
                CycleFrequency.MASK_FRIDAY
        );
        setUpCheckbox(
                view.findViewById(R.id.checkbox_saturday),
                view.findViewById(R.id.option_saturday),
                CycleFrequency.MASK_SATURDAY
        );
        setUpCheckbox(
                view.findViewById(R.id.checkbox_sunday),
                view.findViewById(R.id.option_sunday),
                CycleFrequency.MASK_SUNDAY
        );
    }

    private void setUpCheckbox(ImageView checkbox, View button, int dayMask) {
        checkbox.setImageResource(getCheckboxImage(frequency.maskIsSet(dayMask)));
        button.setOnClickListener(v -> {
            boolean isChecked;
            synchronized (frequency) {
                frequency.toggleMask(dayMask);
                isChecked = frequency.maskIsSet(dayMask);
            }
            checkbox.setImageResource(getCheckboxImage(isChecked));
        });
    }

    @DrawableRes
    private int getCheckboxImage(boolean checked) {
        return checked ? R.drawable.ic_checkbox : R.drawable.ic_checkbox_outline;
    }

    @FunctionalInterface
    public interface SubmitAction {
        void trigger(String name, CycleFrequency frequency);
    }
}
