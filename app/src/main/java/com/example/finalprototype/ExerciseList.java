package com.example.finalprototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExerciseList extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {
    private RecyclerViewAdapter adapter;
    private static final String MY_PREF = "MY_APP";
    private static final String EXERCISES_KEY = "exercises";
    private static final String YOUTUBE_ID = "youTubeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        ArrayList<Data> exercises = getData(EXERCISES_KEY);

        RecyclerView recyclerView = findViewById(R.id.exerRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(this, exercises);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

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
                case R.id.exercises:
                    Intent exercisesIntent = new Intent(this, Exercises.class);
                    startActivity(exercisesIntent);
                    break;
            }
            return true;
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent videoIntent = new Intent(this, Video.class);
        videoIntent.putExtra(YOUTUBE_ID, adapter.getItem(position).getUrl());
        startActivity(videoIntent);
    }

    private ArrayList<Data> getData(String name) {
        SharedPreferences mPrefs = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        ArrayList<Data> data = new ArrayList<>();
        Gson gson = new Gson();
        String json = mPrefs.getString(name, "");
        if (json.isEmpty()) {
            Toast.makeText(ExerciseList.this,"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Data>>(){}.getType();
            data = gson.fromJson(json, type);
        }
        return data;
    }
}