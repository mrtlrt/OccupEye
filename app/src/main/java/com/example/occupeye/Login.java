package com.example.occupeye;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button login;
    Button signin;
    EditText username;
    EditText password;
    FirebaseAuth fAuth;

    Button forgotpass;
    boolean password_status;
    boolean username_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://occupeye-dedb8-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference mRef=database.getReference().child("Users");
        fAuth = FirebaseAuth.getInstance();
        //INITIALLIZING THE VARIABLES
        login=findViewById(R.id.loggin);
        signin=findViewById(R.id.signup);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        forgotpass=findViewById(R.id.forgot_pass);
        password_status=false;
        username_status=false;


        //
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                String username_tag=username.getText().toString();
                if(!b&&username_tag.length()<6){
                    Toast.makeText(Login.this,"Invalid Username",Toast.LENGTH_SHORT).show();
                    username_status=false;
                } else if (!b&&username_tag.contains(" ")) {

                    Toast.makeText(Login.this,"Username Cannot Have Space",Toast.LENGTH_SHORT).show();
                    username_status=false;
                }else if(!b){
                    username_status=true;
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                String password_tag=password.getText().toString();
                if(!b && password_tag.length()<6){
                    Toast.makeText(Login.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                    password_status=false;
                } else if (!b&& password_tag.contains(" ")) {
                    Toast.makeText(Login.this,"Password Cannot Have Space",Toast.LENGTH_SHORT).show();
                    password_status=false;
                } else if (!b) {
                    password_status=true;
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User(username.getText().toString(), password.getText().toString());

                if (user.validate_login()) {
                                try {fAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        ProgressDialog progressDialog = new ProgressDialog(Login.this);
                                                        progressDialog.setTitle("Loading....");
                                                        progressDialog.show();
                                                        if (task.isSuccessful()) {
                                                            // Update the password in Firestore
                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                            DocumentReference userRef = db.collection("Users").document(fAuth.getCurrentUser().getUid());
                                                            Intent intent = new Intent(Login.this, HomeScreen.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Login.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                                                        }
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                } catch (Exception e) {
                                    Toast.makeText(Login.this, "User not registered", Toast.LENGTH_SHORT).show();
                                }



                } else {
                    Toast.makeText(Login.this, "Invalid Entries", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPasswordPage.class));
            }
        });
    }
}