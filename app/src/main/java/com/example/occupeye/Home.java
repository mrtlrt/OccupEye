//package com.example.occupeye;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//
////TODO : Add bookmark checker
////TODO : Display the bookmark recycler view
////TODO : Display the crowd based on a percent
////TODO : Connect to the database
////TODO : Add all this to github
//public class Home extends AppCompatActivity {
//    Button loginTester;
//
//    Button registerTester;
//    ArrayList<CategoryCreatorModel> categoryModel=new ArrayList<>();
//    View hostelselbtn;
//    View allselbtn;
//    View collegeselbtn;
//    View libselbtn;
//    DatabaseReference myRef;
//    Bookmark bookmark=Bookmark.getBookmark();
//
//    HashMap<String,String> obj=new HashMap<>();
//
//
//    @SuppressLint("WrongViewCast")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_home);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("Location");
//
//
//        //INITIALISING DOM ELEMENTS
//        hostelselbtn=findViewById(R.id.category_sel_hostel);
//        allselbtn=findViewById(R.id.category_sel_all);
//        libselbtn=findViewById(R.id.category_sel_lib);
//        collegeselbtn=findViewById(R.id.category_sel_college);
//
//        //SETTING UP DEFAULT DISPLAY ITEMS
//        setUpCategoryModel("all");
//        setUpRecyclerView();
//
//        //CATEGORY SELECTORS
//        hostelselbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.d("HOSTELSEL", "HOSTEL ROOMS ONLY");
//                setUpCategoryModel("hostel");
//                setUpRecyclerView();
//            }
//        });
//        collegeselbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("COLLEGESEL", "COLLEGE ROOMS ONLY");
//                setUpCategoryModel("college");
//                setUpRecyclerView();
//            }
//        });
//        allselbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("ALLSEL", "ALL ROOMS");
//                setUpCategoryModel("all");
//                setUpRecyclerView();
//            }
//        });
//        libselbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("LIBSEL", "LIB ROOMS ONLY");
//                setUpCategoryModel("lib");
//                setUpRecyclerView();
//            }
//
//        });
//
//
//    }
//    private void setUpRecyclerView(){
//        //Setting up the recycler view
//        RecyclerView recyclerView=findViewById(R.id.myRecyclerView);
//        AA_RecyclerviewAdapter adapter=new AA_RecyclerviewAdapter(this,categoryModel);
//        recyclerView.setAdapter(adapter);
//
//
//        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(
//                Home.this,
//                LinearLayoutManager.HORIZONTAL,
//                false);
//        recyclerView.setLayoutManager(HorizontalLayout);
//    }
//
//    private void setUpCategoryModel(String buttonName){
//
//
//        if (buttonName=="all"){
//            ArrayList<String> roomName = new ArrayList<>();
//            ArrayList<String> colorGrading = new ArrayList<>();
//            ArrayList<Integer> imageno = new ArrayList<>();
//
//
//            categoryModel.clear();
//
//            for(int i=0;i<roomName.size();i++){
//                categoryModel.add(new CategoryCreatorModel(roomName.get(i),imageno.get(i),colorGrading.get(i)));
//            }
//            System.out.println(Bookmark.getBookmarkedLocs());
//        } else if (buttonName=="hostel") {
//            categoryModel.clear();
//            String[] roomName={"SUTD HOSTEL"};
//            int[] imageno={R.drawable.hostel_img};
//            for(int i=0;i<roomName.length;i++){
//                categoryModel.add(new CategoryCreatorModel(roomName[i],imageno[i], colo));
//            }
//
//        }else if (buttonName=="lib"){
//            categoryModel.clear();
//            String[] roomName={"SUTD LIBRARY"};
//            int[] imageno={R.drawable.lib_img};
//            for(int i=0;i<roomName.length;i++){
//                categoryModel.add(new CategoryCreatorModel(roomName[i],imageno[i]));
//            }
//        } else if (buttonName=="college") {
//            categoryModel.clear();
//            String[] roomName={"SUTD LIBRARY","SUTD COLLEGE"};
//            int[] imageno={R.drawable.lib_img,R.drawable.college};
//
//            for(int i=0;i<roomName.length;i++){
//
//                categoryModel.add(new CategoryCreatorModel(roomName[i],imageno[i]));
//            }
//
//        }
//
//    }
//
//}