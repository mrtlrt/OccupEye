package com.example.occupeye;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;


import com.example.occupeye.Fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    Button block55;
    Button block57;
    Button block59;
    Button register;
    Button back;
    boolean block55_sel;
    boolean block57_sel;
    boolean block59_sel;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    SwitchCompat hostel_toggle;
    EditText username;
    EditText email;
    EditText password;
    DatabaseReference myRef;
    String userID;

    private boolean block_checker(){
        if(block57_sel ||block59_sel ||block55_sel)return true;
        else if (!hostel_toggle.isChecked()) {return true;

        }
        return false;
    }

    private String getActiveBlock(){
        if (block55_sel){return "55";} else if (block59_sel) {return "59";}else if(block57_sel){return "57";}else {return "-";}

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://occupeye-dedb8-default-rtdb.asia-southeast1.firebasedatabase.app");
        db=FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        block55_sel=false;
        block59_sel=false;
        block57_sel=false;

        block57=findViewById(R.id.button57);
        block55=findViewById(R.id.button55);
        block59=findViewById(R.id.button59);

        register=findViewById(R.id.register_submit);
        back=findViewById(R.id.back);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        hostel_toggle=findViewById(R.id.toggleHostel);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user=new User(username.getText().toString().trim(),password.getText().toString(),email.getText().toString());
                //username checker
                //password checker
                //email checker
                //block select
                if(user.validate()&&block_checker()){
                    ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                    progressDialog.setTitle("Loading...");
                    //TODO send data to firebase collection
                    user.setBlock(getActiveBlock());
                    fAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign up success, update UI with the signed-in user's information
                                        Log.d("firebaseAuth", "onComplete done ");
                                        userID = fAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = db.collection("Users").document(userID);
                                        documentReference.set(user.database_obj()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                documentReference.update("bookmark", new ArrayList<String>());
                                            }
                                        });
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign up fails, display a message to the user.
                                        Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
//                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                Toast.makeText(Register.this,"Username Already In Use",Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                myRef.setValue(user.database_obj());
//                                db.collection("Users").document(username.getText().toString()).set(user.database_obj());
//                                //Log database stored
//
//                                myRef.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        String value = String.valueOf(snapshot.getValue());
//                                        Log.d("FirebaseElement", "Value is: " + value);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//                                        Log.w("FirebaseElement", "Failed to read value.", error.toException());
//                                    }
//                                });
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });


                }else {
                    if(!user.username_checker()){

                        Toast.makeText(Register.this,"Username has to be atleast 7 characters long",Toast.LENGTH_SHORT).show();
                    }if (!user.password_checker()) {
                        Toast.makeText(Register.this,"Password Cannot Have Space",Toast.LENGTH_SHORT).show();
                    }
                    if(!block_checker()){
                        Toast.makeText(Register.this,"Block Not Entered",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
        //SLECTOR FOR BUTTONS
        block55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(block55_sel){
                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    block55.setBackgroundTintList(colorStateList);
                    block55.setTextColor(Color.parseColor("#E3655B"));
                    block55_sel=false;
                    return;
                }
                if(block59_sel || block57_sel){
                    return;
                }
                block55_sel=true;
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iconhover));
                block55.setBackgroundTintList(colorStateList);
                block55.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
        });
        block57.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(block57_sel){
                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    block57.setBackgroundTintList(colorStateList);
                    block57.setTextColor(Color.parseColor("#E3655B"));
                    block57_sel=false;
                    return;
                }
                if(block59_sel || block55_sel){
                    return;
                }
                block57_sel=true;
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iconhover));
                block57.setBackgroundTintList(colorStateList);
                block57.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
        });
        block59.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(block59_sel){
                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    block59.setBackgroundTintList(colorStateList);
                    block59.setTextColor(Color.parseColor("#E3655B"));
                    block59_sel=false;
                    return;
                }
                if(block55_sel || block57_sel){
                    return;
                }
                block59_sel=true;
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iconhover));
                block59.setBackgroundTintList(colorStateList);
                block59.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
        });


        
    }
}
