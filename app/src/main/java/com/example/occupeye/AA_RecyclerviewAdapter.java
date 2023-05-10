package com.example.occupeye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.occupeye.Fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AA_RecyclerviewAdapter extends RecyclerView.Adapter<AA_RecyclerviewAdapter.MyViewHolder> {
    Context context;
    ArrayList<CategoryCreatorModel> creatorModel;
    ArrayList<String> bookmarks = new ArrayList<>();
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;


    public AA_RecyclerviewAdapter(Context context, ArrayList<CategoryCreatorModel> creatorModel, ArrayList<String> bookmarks) {
        this.context = context;
        this.creatorModel = creatorModel;
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
        this.userID = fAuth.getCurrentUser().getUid();
        if(bookmarks == null){
            this.bookmarks = new ArrayList<String>();
            fStore.collection("Users").document(userID).update("bookmark", this.bookmarks).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }else{
            this.bookmarks = bookmarks;
        }
    }

    @NonNull
    @Override
    public AA_RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.text, parent, false);

        return new AA_RecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerviewAdapter.MyViewHolder holder, int position) {
        holder.background.setImageResource(creatorModel.get(position).image);
        holder.roomName.setText(creatorModel.get(position).roomName);
        if(this.bookmarks.contains(holder.roomName.getText().toString())){
            holder.bookmarkButton.setText("BOOKMARKED");
        }
        holder.cardView.setCardBackgroundColor(Color.parseColor(creatorModel.get(position).colour));
        DocumentReference documentReference = fStore.collection("Users").document(userID);
        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.bookmarkButton.getText().toString() == "BOOKMARKED") {
                    holder.bookmarkButton.setText("ADD TO BOOKMARKS");
                    bookmarks.remove(holder.roomName.getText().toString());
                } else{
                    if(!bookmarks.contains(holder.roomName.getText().toString())){
                        holder.bookmarkButton.setText("BOOKMARKED");
                        bookmarks.add(holder.roomName.getText().toString());
                    }
                }
                documentReference.update("bookmark", bookmarks).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            }
        });
        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RoomPage.class);
                intent.putExtra("roomName", creatorModel.get(holder.getAdapterPosition()).roomName);
                intent.putExtra("roomType", creatorModel.get(holder.getAdapterPosition()).getRoomType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return creatorModel.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView background;
        TextView roomName;
        Button bookmarkButton;
        CardView cardView;
        CardView parentCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomname);
            background = itemView.findViewById(R.id.background_layout);
            bookmarkButton = itemView.findViewById(R.id.buttonbookmark);
            cardView = itemView.findViewById(R.id.crowdColour);
            parentCard = itemView.findViewById(R.id.cardholder);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        public void onLongItemClick(View view, int position);
    }
}
