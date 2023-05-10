package com.example.occupeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.occupeye.Adapters.SliderAdapter;
import com.example.occupeye.Fragments.HomeFragment;
import com.example.occupeye.HomeScreen;
import com.example.occupeye.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomPage extends AppCompatActivity {
    SliderView sliderView;
    FirebaseFirestore fstore;
    int[] images = new int[3];
    private Map<String, List<Integer>> mContent = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_explore);

        Intent intent = getIntent();
        String title = intent.getStringExtra("roomName");
        String roomType = intent.getStringExtra("roomType");

        DatabaseReference roomTypeRef = FirebaseDatabase.getInstance("https://occupeye-dedb8-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Locations");
        if (roomType.equals("lib")) {
            roomTypeRef = roomTypeRef.child("Library").child("Level 3").child(title);
        } else if (roomType.equals("hostel")) {
            roomTypeRef = roomTypeRef.child("Hostel").child("Block 55").child(title);
        } else if (roomType.equals("college")) {
            roomTypeRef = roomTypeRef.child("College").child("Building 2").child(title);
        }

        roomTypeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String roomColor = dataSnapshot.child("Colour Grading").getValue(String.class);
                ImageView imageView = findViewById(R.id.crowdindicator);
                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(RoomPage.this, R.drawable.circle).mutate();
                int newFillColor = Color.parseColor(roomColor);
                drawable.setColor(newFillColor);
                imageView.setImageDrawable(drawable);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        List<Integer> contentStudyRoom = new ArrayList<>();
        contentStudyRoom.add(R.drawable.studyroom);
        contentStudyRoom.add(R.string.studyrooms);
        contentStudyRoom.add(R.drawable.studyroom1);
        contentStudyRoom.add(R.drawable.studyroom2);
        contentStudyRoom.add(R.drawable.studyroom3);
        mContent.put("Study Room 2", contentStudyRoom);
        mContent.put("Study Room 4", contentStudyRoom);
        mContent.put("Study Room 6", contentStudyRoom);
        mContent.put("Study Room 9", contentStudyRoom);
        mContent.put("Test Room", contentStudyRoom);

        List<Integer> contentLibrary = new ArrayList<>();
        contentLibrary.add(R.drawable.library);
        contentLibrary.add(R.string.library);
        contentLibrary.add(R.drawable.library1);
        contentLibrary.add(R.drawable.library2);
        contentLibrary.add(R.drawable.library3);
        mContent.put("Discussion Room 1", contentLibrary);
        mContent.put("Discussion Room 2", contentLibrary);
        mContent.put("Discussion Room 6", contentLibrary);
        mContent.put("Discussion Room 3", contentLibrary);

        List<Integer> contentCollege = new ArrayList<>();
        contentCollege.add(R.drawable.college1);
        contentCollege.add(R.string.college);
        contentCollege.add(R.drawable.college1);
        contentCollege.add(R.drawable.college2);
        contentCollege.add(R.drawable.college3);
        mContent.put("Cohort Class 6", contentCollege);
        mContent.put("Think Tank 11", contentCollege);
        mContent.put("Think Tank 15", contentCollege);


        List<Integer> contentList = mContent.get(title);

        TextView mainText = findViewById(R.id.maintext);
        mainText.setText(title);

        ImageView mainImage = findViewById(R.id.topimage);
        mainImage.setImageResource(contentList.get(0));

        TextView descText = findViewById(R.id.desc1);
        descText.setText(contentList.get(1));

        images[0] = contentList.get(2);
        images[1] = contentList.get(3);
        images[2] = contentList.get(4);


        ImageButton hostel = findViewById(R.id.imageButton1);
        hostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomPage.this, HomeScreen.class);
                intent.putExtra("hostelselbtn",true);
                startActivity(intent);
            }
        });

        ImageButton lib = findViewById(R.id.imageButton2);
        lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomPage.this, HomeScreen.class);
                intent.putExtra("libselbtn",true);
                startActivity(intent);
            }
        });

        ImageView imageView = findViewById(R.id.crowdindicator);
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.circle).mutate();
        int newFillColor = ContextCompat.getColor(this, R.color.iconhover);
        drawable.setColor(newFillColor);
        imageView.setImageDrawable(drawable);

        sliderView = findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(images, 200);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
    }
}