package com.example.finalprototype;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CreateFavAdapter extends RecyclerView.Adapter<CreateFavAdapter.ViewHolder> {

    private final OnItemCheckListener onItemClick;
    private ArrayList<Data> mData;
    private LayoutInflater mInflater;

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    interface OnItemCheckListener {
        void onItemCheck(Data data);
        void onItemUncheck(Data data);
    }

    CreateFavAdapter(Context context, ArrayList<Data> data, @NonNull OnItemCheckListener onItemCheckListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.onItemClick = onItemCheckListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.create_fav_rows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String exercise = mData.get(position).getTitle();
        Data currentData = mData.get(position);
        holder.myTextView.setText(exercise);

        holder.checkBox.setOnClickListener(v -> {
            final boolean isChecked = holder.checkBox.isChecked();
            if(isChecked) {
                onItemClick.onItemCheck(currentData);
            } else {
                onItemClick.onItemUncheck(currentData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView myTextView;
        public CardView cardView;
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            this.setIsRecyclable(false);
            cardView = view.findViewById(R.id.favoriteCardView);
            myTextView = view.findViewById(R.id.favoriteListName);
            checkBox = view.findViewById(R.id.checkBox);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            checkBox.setOnClickListener(onClickListener);
        }

    }

    public Data getItem(int id) {
        return mData.get(id);
    }

}