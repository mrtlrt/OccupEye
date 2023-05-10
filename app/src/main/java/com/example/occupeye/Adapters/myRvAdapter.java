package com.example.occupeye.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.occupeye.Bookmark;
import com.example.occupeye.CategoryCreatorModel;
import com.example.occupeye.Fragments.UserFragment;
import com.example.occupeye.R;
import com.example.occupeye.RoomPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class myRvAdapter extends RecyclerView.Adapter<myRvAdapter.myHolder> {
    Context context;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ArrayList<String> bookmarks = new ArrayList<>();
    ArrayList<CategoryCreatorModel> creatorModel;

    public myRvAdapter(Context context, ArrayList<CategoryCreatorModel> creatorModel, ArrayList<String> bookmarks){
        this.bookmarks = bookmarks;
        this.context = context;
        this.creatorModel = creatorModel;
        this.fAuth = FirebaseAuth.getInstance();
        this.fStore = FirebaseFirestore.getInstance();
        this.userID = fAuth.getCurrentUser().getUid();
    }
    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.rv_item, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.tvTitle.setText(bookmarks.get(position));
        holder.background.setImageResource(R.drawable.studyroom);
        DocumentReference documentReference = fStore.collection("Users").document(userID);
        holder.bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getScrollBarSize() == 0){
                    Toast.makeText(context, "No more bookmarks left!", Toast.LENGTH_SHORT).show();
                }
                bookmarks.remove(holder.getAdapterPosition());
                documentReference.update("bookmark", bookmarks).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RoomPage.class);
                String roomname = bookmarks.get(holder.getAdapterPosition());
                String roomType = "hostel";
                intent.putExtra("roomName",roomname);
                for(int i = 0; i < creatorModel.size(); i++){
                    if(creatorModel.get(i).getRoomName().equals(roomname)){
                        roomType = creatorModel.get(i).getRoomType();
                        break;
                    }
                }
                intent.putExtra("roomType", roomType);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(bookmarks == null){
            DocumentReference documentReference = fStore.collection("Users").document(userID);
            documentReference.update("bookmark", new ArrayList<String>());
            return 0;
        }
        return bookmarks.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView background;
        Button bookmarkbtn;
        CardView cardView;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.roomnameRv);
            bookmarkbtn = itemView.findViewById(R.id.bookmarkbtnRv);
            background = itemView.findViewById(R.id.roompicRv);
            cardView = itemView.findViewById(R.id.parentCard);

        }
    }

}
