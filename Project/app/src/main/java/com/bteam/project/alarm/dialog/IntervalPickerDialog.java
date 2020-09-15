package com.bteam.project.alarm.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class IntervalPickerDialog extends DialogFragment {

    private NumberPicker.OnValueChangeListener valueChangeListener;
    private NumberPicker numberPicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        numberPicker = new NumberPicker(getActivity());

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("반복 간격 설정");
        builder.setMessage("알람 반복 간격을 설정하세요.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public void setNumberPickerValue(int i) {
        numberPicker.setValue(i);
    }
}