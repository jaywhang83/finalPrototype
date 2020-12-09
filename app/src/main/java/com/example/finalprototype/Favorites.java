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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity implements FavoriteAdapter.ItemClickListener{
    private FavoriteAdapter adapter;
    private static final String TITLES_KEY = "titles";
    private static final String MY_PREF = "MY_APP";
    private static final String SINGLE_TITLE_KEY = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ArrayList<String> titles = getTitleData(TITLES_KEY);
        System.out.println(titles);

        RecyclerView recyclerView = findViewById(R.id.favRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteAdapter(this, titles);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.addNewFav);
        fab.setOnClickListener(v -> {
            Intent createNewFavIntent = new Intent(Favorites.this, CreateNewFav.class);
            startActivity(createNewFavIntent);
        });

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
                    Intent exerciseIntent = new Intent(this, Exercises.class);
                    startActivity(exerciseIntent);
                    break;
            }
            return true;
        });
    }

    private ArrayList<String> getTitleData(String name) {
        SharedPreferences mPrefs = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        ArrayList<String> data = new ArrayList<>();
        Gson gson = new Gson();
        String json = mPrefs.getString(name, "");
        if (json.isEmpty()) {
            Toast.makeText(Favorites.this,"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<String>>(){}.getType();
            data = gson.fromJson(json, type);
        }
        return data;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent favoriteIntent = new Intent(this, FavoriteList.class);
        favoriteIntent.putExtra(SINGLE_TITLE_KEY, adapter.getItem(position));
        startActivity(favoriteIntent);
    }
}