package com.example.finalprototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CreateNewFav extends AppCompatActivity {
    private CreateFavAdapter adapter;
    private ArrayList<Data> selectedExercises = new ArrayList<>();
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private  Gson gson;
    private static final String TITLES_KEY = "titles";
    private static final String MY_PREF = "MY_APP";
    private static final String EXERCISES_KEY = "exercises";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_fav);

        mPrefs = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        gson = new Gson();
        ArrayList<Data> exercises = getData(EXERCISES_KEY);

        RecyclerView recyclerView = findViewById(R.id.exerRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new CreateFavAdapter(this, exercises, new CreateFavAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Data data) {
                selectedExercises.add(data);
            }

            @Override
            public void onItemUncheck(Data data) {
                selectedExercises.remove(data);
            }
        });
        recyclerView.setAdapter(adapter);

       displayBottomNavigation();
    }

    private void displayBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.cancel:
                    Intent favoriteIntent = new Intent(this, Favorites.class);
                    startActivity(favoriteIntent);
                    break;
                case R.id.save:
                    saveData();
                    break;
            }
            return true;
        });
    }

    private void saveFavoriteTitles(String title, ArrayList<String> titles, String json) {
        if(!json.isEmpty()) {
            Type type = new TypeToken<List<String>>(){}.getType();
            titles = gson.fromJson(json, type);
        }
        saveTitleHelper(titles, title);
    }

    private void saveTitleHelper(ArrayList<String> titles, String title) {
        titles.add(title);
        editor = mPrefs.edit();
        String newJson = gson.toJson(titles);
        editor.putString(TITLES_KEY, newJson);
        editor.apply();
    }

    private void saveSelectedData(String name, ArrayList<Data> list) {
        String json = gson.toJson(list);
        editor = mPrefs.edit();
        editor.putString(name, json);
        editor.apply();
    }

    private ArrayList<Data> getData(String name) {
        ArrayList<Data> data = new ArrayList<>();
        String json = mPrefs.getString(name, "");
        if (json.isEmpty()) {
            Toast.makeText(CreateNewFav.this,"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Data>>(){}.getType();
            data = gson.fromJson(json, type);
        }
        return data;
    }

    private boolean isTitleEmpty(String title) {
        if (title.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean doesTitleExist(String title, ArrayList<String> titles) {
        return titles.contains(title);
    }

    private void saveData() {
        EditText titleEditText = findViewById(R.id.enterTitle);
        String title = titleEditText.getText().toString();
        ArrayList<String> titles = new ArrayList<>();
        String json = mPrefs.getString(TITLES_KEY, "");

        String missingTitle = "MISSING TITLE ERROR!";
        String missingTitleMsg = "Title is missing. Please enter title";

        String titleExistTile = "TITLE ALREADY EXIST ERROR!";
        String titleExistMsg = "Title already exist. Enter different title";

        String successTitle = "SUCCESS!";
        String successMsg = String.format("%s has been created", title);

        if (!json.isEmpty()) {
            Type type = new TypeToken<List<String>>(){}.getType();
            titles = gson.fromJson(json, type);
            boolean isTitleEmpty = isTitleEmpty(title);
            boolean doesTitleExist = doesTitleExist(title, titles);

            if (!isTitleEmpty) {
                showAlertDialog(missingTitle, missingTitleMsg);
            } else if(doesTitleExist){
                showAlertDialog(titleExistTile, titleExistMsg);
            } else {
                saveFavoriteTitles(title, titles, json);
                saveSelectedData(title, selectedExercises);
                showAlertDialog(successTitle, successMsg);
            }
        } else {
            saveFavoriteTitles(title, titles, json);
            saveSelectedData(title, selectedExercises);
            showAlertDialog(successTitle, successMsg);
        }
    }

    private void showAlertDialog(String alertTitle, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewFav.this, R.style.AlertDialogStyle);

        builder.setMessage(message);
        builder.setTitle(alertTitle);

        builder.setCancelable(false);

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (alertTitle.equalsIgnoreCase("SUCCESS!")) {
                Intent favoriteListIntent = new Intent(this, Favorites.class);
                startActivity(favoriteListIntent);
            }
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

}