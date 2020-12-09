package com.example.finalprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SetTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        displayBottomNavigation();

        Button setWorkTimeBtn = findViewById(R.id.setWorkTime);
        setWorkTimeBtn.setOnClickListener(this::showWorkTimePickerDialog);

        Button setBreakTimeBtn = findViewById(R.id.setBreakTime);
        setBreakTimeBtn.setOnClickListener(this::showBreakTimePickerDialog);
    }

    private void displayBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.settings:
                    Intent settingsIntent = new Intent(this, Settings.class);
                    startActivity(settingsIntent);
                    break;
                case R.id.exercises:
                    Intent exerciseIntent = new Intent(this, Exercises.class);
                    startActivity(exerciseIntent);
                    break;
            }
            return true;
        });
    }

    private void showBreakTimePickerDialog(View view) {
        SetTimeDialog dialog = new SetTimeDialog(SetTime.this);
        dialog.showBreakTimePickerDialog(view);
    }

    private void showWorkTimePickerDialog(View view) {
        SetTimeDialog dialog = new SetTimeDialog(SetTime.this);
        dialog.showWorkTimePickerDialog(view);
    }

}