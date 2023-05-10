package com.example.occupeye.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.occupeye.AA_RecyclerviewAdapter;
import com.example.occupeye.Adapters.myRvAdapter;
import com.example.occupeye.Bookmark;
import com.example.occupeye.CategoryCreatorModel;
import com.example.occupeye.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private SearchView searchView;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    myRvAdapter bookmarkRvAdapter;
    ArrayList<String> dataSource;
    ArrayList<CategoryCreatorModel> categoryModel=new ArrayList<>();

    View rootView;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<CardModel> cardModels = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void setUpCardsModels(){
        String[] cardLocations = getResources().getStringArray(R.array.location_full_text);

        for (int i = 0; i< cardLocations.length; i++){
            cardModels.add(new CardModel(cardLocations[i]));
        }

        System.out.println(cardLocations.length);

    }
    public void setUpRecyclerView(){
        //Setting up the recycler view
        recyclerView=rootView.findViewById(R.id.mRecyclerView);

        AA_RecyclerviewAdapter adapter=new AA_RecyclerviewAdapter(rootView.getContext(),categoryModel, Bookmark.getBookmarkedLocs());
        recyclerView.setAdapter(adapter);


        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);
    }
    public void fetchData(String type, Boolean clear){

        categoryModel.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://occupeye-dedb8-default-rtdb.asia-southeast1.firebasedatabase.app");
        myRef = database.getReference("Locations");

        try {
            if ("library".contains(type.toLowerCase(Locale.ROOT))){
                System.out.println("Library");
                myRef = myRef.child("Library").child("Level 3");


            } else if ("hostel".contains(type.toLowerCase())) {
                System.out.println("Hostel");
                myRef = myRef.child("Hostel").child("Block 55");
            }
            else if ("college".contains(type.toLowerCase(Locale.ROOT))){
                System.out.println("college");
                myRef = myRef.child("College").child("Building 2");
            }else {throw  new Exception();}

            ArrayList<String> roomName = new ArrayList<>();
            ArrayList<String> colours = new ArrayList<>();
            int[] imageno={R.drawable.hostel_img};
            System.out.println(myRef);

            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {Toast.makeText(getContext(),"Unable to get data",Toast.LENGTH_SHORT).show();}
                    if (task.isSuccessful()){
                        System.out.println("works??>");
                        if(task.getResult().exists()){
                            DataSnapshot dataSnapshot = task.getResult();
                            HashMap<String,HashMap<String,String>>data= (HashMap<String, HashMap<String,String>>) task.getResult().getValue();
                            System.out.println(data);
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
                            System.out.println(colours);
                            setUpRecyclerView();

                        }
                    }else{
                        Log.d("label6","myRef");
                    }
                }
            });
        }
       catch (Exception e){
            Toast.makeText(getContext(),"Invalid Search Entry",Toast.LENGTH_SHORT).show();
        }
        ArrayList<String> roomName = new ArrayList<>();
        ArrayList<String> colours = new ArrayList<>();
        int[] imageno={R.drawable.hostel_img};
        System.out.println(myRef);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        linearLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        // Inflate the layout for this fragment

        RecyclerView recyclerView = rootView.findViewById(R.id.mRecyclerView);




        searchView = rootView.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

              fetchData(s,true);
              return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String newText = "";
                filterList(newText);
                return false;
            }
        });

        return rootView;
    }


    private void filterList(String text){
        ArrayList<CardModel> filteredList = new ArrayList<>();
    }
}