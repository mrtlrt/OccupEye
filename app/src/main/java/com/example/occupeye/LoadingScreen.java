package com.example.occupeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.occupeye.Adapters.SliderAdapter;
import com.example.occupeye.Fragments.SearchFragment;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class LoadingScreen extends AppCompatActivity {
    SliderView sliderView;
    int[] images = {R.drawable.loadscreenimg1, R.drawable.loadscreenimg2, R.drawable.loadscreenimg3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        Button button = findViewById(R.id.getstartedbutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadingScreen.this, Login.class);
                startActivity(intent);
            }
        });

        sliderView = findViewById(R.id.imageSlider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
    }
}