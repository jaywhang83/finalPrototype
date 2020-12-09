package com.example.finalprototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import java.util.logging.LogRecord;

import static android.content.Context.MODE_PRIVATE;

public class SetTimeDialog extends Dialog implements  NumberPicker.OnValueChangeListener {
    private static final String WORK_TIME_KEY = "workTime";
    private static final String BREAK_TIME_KEY = "breakTime";
    private static final String MY_PREF = "MY_APP";
    private final Context context;

    public SetTimeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void showWorkTimePickerDialog(View v) {
        setContentView(R.layout.work_time_picker);
        Button cancelBtn = (Button) findViewById(R.id.cancelDialog);
        Button setBtn = (Button) findViewById(R.id.set);
        final NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        final NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        hourPicker.setMaxValue(12);
        hourPicker.setMinValue(0);
        hourPicker.setWrapSelectorWheel(false);
        hourPicker.setOnValueChangedListener(this);

        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(1);
        minutePicker.setWrapSelectorWheel(false);
        minutePicker.setOnValueChangedListener(this);
        setBtn.setOnClickListener(v12 -> {
            int hourInMinutes = hourPicker.getValue() * 60;
            int totalMinutes = hourInMinutes + minutePicker.getValue();
            int workTimeInMillis = totalMinutes * 60 * 1000;
            saveData(WORK_TIME_KEY, workTimeInMillis);
            showAlertDialog("Work Time");
            dismiss();
        });

        cancelBtn.setOnClickListener(v1 -> dismiss());
        show();
    }

    public void showBreakTimePickerDialog(View v) {
        setContentView(R.layout.break_time_picker);
        Button cancelBtn = (Button) findViewById(R.id.cancelDialog);
        Button setBtn = (Button) findViewById(R.id.set);

        final NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);

        minutePicker.setMaxValue(60);
        minutePicker.setMinValue(1);
        minutePicker.setWrapSelectorWheel(false);
        minutePicker.setOnValueChangedListener(this);
        setBtn.setOnClickListener(v1 -> {
            int breakTimeInMillis = minutePicker.getValue() * 60 * 1000;
            saveData(BREAK_TIME_KEY, breakTimeInMillis);
            showAlertDialog("Break Time");
            dismiss();
        });

        cancelBtn.setOnClickListener(v12 -> dismiss());
        show();
    }

    private void showAlertDialog(String displayName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);

        builder.setMessage(String.format("%s has been set", displayName));
        builder.setTitle("CONFIRMATION");

        builder.setCancelable(false);

        builder.setPositiveButton("OK", (dialog, which) -> {
            openHome();
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    private void openHome() {
        Intent homeIntent = new Intent(context, MainActivity.class);
        context.startActivity(homeIntent);
    }

    private void saveData(String name, int time) {
        SharedPreferences mPrefs = context.getSharedPreferences(MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(name, time);
        editor.apply();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}
