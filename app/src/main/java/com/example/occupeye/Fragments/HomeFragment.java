package com.example.occupeye.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.occupeye.AA_RecyclerviewAdapter;
import com.example.occupeye.Adapters.myRvAdapter;
import com.example.occupeye.Bookmark;
import com.example.occupeye.CategoryCreatorModel;
import com.example.occupeye.R;
import com.example.occupeye.RecyclerItemClickListener;
import com.example.occupeye.RoomPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.protobuf.Value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    View rootView;
    Bookmark bookmark;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    ArrayList<CategoryCreatorModel> categoryModel=new ArrayList<>();
    View hostelselbtn;
    View allselbtn;
    View collegeselbtn;
    ArrayList<String> bookmarks = new ArrayList<>();
    View libselbtn;
    RecyclerView recyclerView;
    ArrayList<String> roomName;

    RecyclerView bookmarkrv;
    myRvAdapter bookmarkRvAdapter;
    ArrayList<String> dataSource;
    LinearLayoutManager linearLayoutManager; // every recycler view needs this
    DatabaseReference myRef;

    HashMap<String,String> obj=new HashMap<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        bookmark = Bookmark.getBookmark();
        bookmarks = Bookmark.getBookmarkedLocs();
        DocumentReference documentReference = fStore.collection("Users").document(userID);




        //INITIALISING DOM ELEMENTS
        hostelselbtn=rootView.findViewById(R.id.category_sel_hostel);
        allselbtn=rootView.findViewById(R.id.category_sel_all);
        libselbtn=rootView.findViewById(R.id.category_sel_lib);
        collegeselbtn=rootView.findViewById(R.id.category_sel_college);
        recyclerView = rootView.findViewById(R.id.myRecyclerView);
        bookmarkrv = rootView.findViewById(R.id.bookmarksRv);
        linearLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        //SETTING UP DEFAULT DISPLAY ITEMS
        setUpCategoryModel("all");
        setUpRecyclerView();

        //CATEGORY SELECTORS
        hostelselbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("HOSTELSEL", "HOSTEL ROOMS ONLY");
                setUpCategoryModel("hostel");


                Log.d("label2","msg2");

            }
        });
        collegeselbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("COLLEGESEL", "COLLEGE ROOMS ONLY");
                setUpCategoryModel("college");
                setUpRecyclerView();
            }
        });
        allselbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ALLSEL", "ALL ROOMS");
                setUpCategoryModel("all");
                setUpRecyclerView();
            }
        });
        libselbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LIBSEL", "LIB ROOMS ONLY");
                setUpCategoryModel("lib");
                setUpRecyclerView();
            }

        });
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    if(value.exists()){
                        Bookmark.setBookmarkedLocs((ArrayList<String>) value.get("bookmark"));
                        bookmarkRvAdapter = new myRvAdapter(rootView.getContext(), categoryModel, Bookmark.getBookmarkedLocs());
                        bookmarkrv.setLayoutManager(linearLayoutManager);
                        bookmarkrv.setAdapter(bookmarkRvAdapter);
                    }
                }
            }
        });

//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.framelayouthome);
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.detach(currentFragment);
//                fragmentTransaction.attach(currentFragment);
//                fragmentTransaction.commit();
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
        return rootView;
    }

    public void setUpRecyclerView(){
        //Setting up the recycler view
        recyclerView=rootView.findViewById(R.id.myRecyclerView);
        AA_RecyclerviewAdapter adapter=new AA_RecyclerviewAdapter(rootView.getContext(),categoryModel, Bookmark.getBookmarkedLocs());
        recyclerView.setAdapter(adapter);


        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);
    }

    private void setUpCategoryModel(String buttonName){
        Log.d("settingup","settingupmodels");
        if (buttonName=="all"){
            fetchData("all",true);
        } else if (buttonName=="hostel") {
            fetchData("hostel", true);
        }else if (buttonName=="lib"){
            fetchData("lib", true);
        } else if (buttonName=="college") {
            fetchData("college", true);
        }
    }

    public void fetchData(String type, Boolean clear){
        if (clear){
            categoryModel.clear();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://occupeye-dedb8-default-rtdb.asia-southeast1.firebasedatabase.app");
        myRef = database.getReference("Locations");
        if (type=="lib"){
            myRef = myRef.child("Library").child("Level 3");
        } else if (type=="hostel") {
            myRef = myRef.child("Hostel").child("Block 55");
        } else if (type=="college"){
            myRef = myRef.child("College").child("Building 2");
        } else if (type=="all"){
            fetchData("hostel", false);
            fetchData("college", false);
            fetchData("lib", false);
        }

        ArrayList<String> roomName = new ArrayList<>();
        ArrayList<String> colours = new ArrayList<>();
        int[] imageno={R.drawable.hostel_img};


        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {Toast.makeText(getContext(),"Unable to get data",Toast.LENGTH_SHORT).show();}
                if (task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        HashMap<String,HashMap<String,String>>data= (HashMap<String, HashMap<String,String>>) task.getResult().getValue();
                        for ( String key : data.keySet() ) {
                            Log.d("LOOK HERE", String.valueOf(key));
                            roomName.add(String.valueOf(key));
                        }
                        for (HashMap value : data.values()) {
                            colours.add((String) value.get("Colour Grading"));
                        }
                        for(int i=0;i<roomName.size();i++){
                            categoryModel.add(new CategoryCreatorModel(roomName.get(i),imageno[0],colours.get(i), type));
                        }
                        setUpRecyclerView();

                    }
                }else{
                    Log.d("label6","myRef");
                }
            }
        });
    }

    public static String getHexColor(String input) {
        String hexColor = null;
        Pattern pattern = Pattern.compile("#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            hexColor = matcher.group();
        }
        return hexColor;
    }

}