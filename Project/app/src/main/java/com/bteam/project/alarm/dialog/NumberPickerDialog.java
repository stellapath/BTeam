package com.bteam.project.alarm.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NumberPickerDialog extends DialogFragment {

    String title;    // dialog 제목
    String message;  // dialog 설명
    int minValue;    // 최소값
    int maxValue;    // 최대값
    int nowValue;    // 현재값

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final NumberPicker numberPicker = new NumberPicker(getActivity());

        Bundle bundle = getArguments();

        title = bundle.getString("title");
        message = bundle.getString("message");
        minValue = bundle.getInt("minValue");
        maxValue = bundle.getInt("maxValue");
        nowValue = bundle.getInt("nowValue");

        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setValue(nowValue);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);

        return super.onCreateDialog(savedInstanceState);
    }
}
