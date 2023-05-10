package com.example.occupeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        Intent intent = new Intent(MainActivity.this, LoadingScreen.class);
        startActivity(intent);


//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this, LoadingScreen.class); // moving to leading screen
//                startActivity(intent);
//                finish();// end running of this activity
//            }
//        }, 1750);
    }


//    public void sendData(View view){
//        writeNewUser();
//    }
//
//    public void writeNewUser() {
//        String name = "ryan";
//        String email = "xx@gmail.com";
//        User user = new User(name, email);
//
//        mDatabase.child("users").child(userId).setValue(user);
//    }
}