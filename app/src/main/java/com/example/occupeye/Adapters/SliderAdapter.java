package com.example.occupeye.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.occupeye.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {
    int[] images;
    int imageHeight;

    public SliderAdapter(int[] images){
        this.images = images;
    }

    public SliderAdapter(int[] images, int imageHeight) {
        this.images = images;
        this.imageHeight = imageHeight;
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);

        if (imageHeight > 0) {
            ViewGroup.LayoutParams layoutParams = viewHolder.imageView.getLayoutParams();
            float density = viewHolder.imageView.getResources().getDisplayMetrics().density;
            int newHeightInPixels = (int) (imageHeight * density + 0.5f); // Convert dp to pixels
            layoutParams.height = newHeightInPixels;
            viewHolder.imageView.setLayoutParams(layoutParams);
        }

    }


    @Override
    public int getCount() {
        return this.images.length;
    }
}
