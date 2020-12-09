package com.example.finalprototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    private CardView setWorkTime;
    private CardView setBreakTime;
    private CardView viewExercises;
    private CardView myFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setWorkTime = findViewById(R.id.workTimeCardView);
        setBreakTime = findViewById(R.id.breakTimeCardView);
        viewExercises = findViewById(R.id.viewExerciseCardView);
        myFavorite = findViewById(R.id.myFavoritesCardView);

        setWorkTime.setOnClickListener(this);
        setBreakTime.setOnClickListener(this);
        viewExercises.setOnClickListener(this);
        myFavorite.setOnClickListener(this);

       displayBottomNavigation();
    }

    private void displayBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.setTimes:
                    Intent setTimeIntent = new Intent(this, SetTime.class);
                    startActivity(setTimeIntent);
                    break;
                case R.id.exercises:
                    Intent exerciseIntent = new Intent(this, Exercises.class);
                    startActivity(exerciseIntent);
                    break;
            }
            return true;
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.workTimeCardView:
                SetTimeDialog workTimeDialog = new SetTimeDialog(Settings.this);
                workTimeDialog.showWorkTimePickerDialog(v);
                break;
            case R.id.breakTimeCardView:
                SetTimeDialog breakTimeDialog = new SetTimeDialog(Settings.this);
                breakTimeDialog.showBreakTimePickerDialog(v);
                break;
            case R.id.viewExerciseCardView:
                Intent exerciseListIntent = new Intent(this, ExerciseList.class);
                startActivity(exerciseListIntent);
                break;
            case R.id.myFavoritesCardView:
                Intent favoriteIntent  = new Intent(this, Favorites.class);
                startActivity(favoriteIntent);
                break;
        }
    }
}