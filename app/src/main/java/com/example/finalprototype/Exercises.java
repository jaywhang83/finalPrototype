package com.example.finalprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Exercises extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        Button exerciseListBtn = findViewById(R.id.exerciseList);
        Button favoriteBtn = findViewById(R.id.favorite);

        exerciseListBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);

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
                case R.id.settings:
                    Intent settingsIntent = new Intent(this, Settings.class);
                    startActivity(settingsIntent);
                    break;
                case R.id.setTimes:
                    Intent setTimeIntent = new Intent(this, SetTime.class);
                    startActivity(setTimeIntent);
                    break;
            }
            return true;
        });
    }

    @Override
    public void onClick(View v)  {
        switch (v.getId()) {
            case R.id.exerciseList:
                Intent exerciseListIntent = new Intent(this, ExerciseList.class);
                startActivity(exerciseListIntent);
                break;
            case R.id.favorite:
                Intent favoriteIntent = new Intent(this, Favorites.class);
                startActivity(favoriteIntent);
                break;
        }
    }
}