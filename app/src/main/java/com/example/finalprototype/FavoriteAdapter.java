package com.example.finalprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private static final String TITLES_KEY = "titles";
    private static final String MY_PREF = "MY_APP";
    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private FavoriteAdapter.ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    FavoriteAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.favorite_list_row, parent, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String exercise = mData.get(position);
        holder.myTextView.setText(exercise);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myTextView;
        public CardView cardView;
        public ImageButton deleteBtn;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.favoriteListCardView);
            myTextView = view.findViewById(R.id.favoriteListName);
            deleteBtn = view.findViewById(R.id.delete);
            view.setOnClickListener(this);

            deleteBtn.setOnClickListener(v -> {
                showAlertDialog(v, getAdapterPosition());
            });
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
            System.out.println("position" + String.valueOf(getAdapterPosition()));
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(FavoriteAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void remove(int position) {
        mData.remove(position);
        updateSharedPreference();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    private void showAlertDialog(View v, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogStyle);

        builder.setMessage(String.format("Do you want to delete %s?", getItem(position)));
        builder.setTitle("CONFIRMATION");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) ->
                remove(position)
        );

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateSharedPreference() {
        SharedPreferences mPrefs = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        Gson gson = new Gson();
        String newJson = gson.toJson(mData);
        editor.putString(TITLES_KEY, newJson);
        editor.apply();

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
