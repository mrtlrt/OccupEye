package com.example.occupeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.occupeye.Fragments.HomeFragment;
import com.example.occupeye.Fragments.SearchFragment;
import com.example.occupeye.Fragments.UserFragment;
import com.example.occupeye.databinding.ActivityHomeScreenBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeScreen extends AppCompatActivity{

    ActivityHomeScreenBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case(R.id.home):
                        replaceFragment(new HomeFragment());
                        break;
                    case(R.id.search):
                        replaceFragment(new SearchFragment());
                        break;
                    case(R.id.user):
                        replaceFragment(new UserFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}