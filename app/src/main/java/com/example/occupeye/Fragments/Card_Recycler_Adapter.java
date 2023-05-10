package com.example.occupeye.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.occupeye.R;

import java.util.ArrayList;

public class Card_Recycler_Adapter extends RecyclerView.Adapter<Card_Recycler_Adapter.myViewHolder> {
    Context context;
    ArrayList<CardModel> cardModelArrayList;
    public Card_Recycler_Adapter(Context context, ArrayList<CardModel> cardModelArrayList){
        this.context = context;
        this.cardModelArrayList = cardModelArrayList;
    }
    @NonNull
    @Override
    public Card_Recycler_Adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout n give look to rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new Card_Recycler_Adapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Card_Recycler_Adapter.myViewHolder holder, int position) {
        //assign values to views as they come back on the screen
        holder.locationView.setText(cardModelArrayList.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        // how many items should we display
        return cardModelArrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView locationView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            locationView = itemView.findViewById(R.id.locationView);
        }
    }
}
