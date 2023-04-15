package com.nexus.unify.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.nexus.unify.ModelClasses.SliderItem;
import com.nexus.unify.R;
import com.nexus.unify.SharedProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private final Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapterExample(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        return new SliderAdapterVH(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);
setAnimation(viewHolder.itemView,position);
        if (sliderItem.getUsername().equals("null")) {

            viewHolder.button.setText("View More");
        }else {
            viewHolder.button.setText("View Profile");
        }
        viewHolder.textViewDescription.setText(sliderItem.getDesc());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sliderItem.getUsername().equals("null")){
                    String url = sliderItem.getWeburl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                }else {

                    Intent intent=new Intent(context, SharedProfileActivity.class);
                    intent.putExtra("profileid",sliderItem.getUsername());
                    context.startActivity(intent);
                }
            }
        });
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sliderItem.getUsername().equals("")){
                    String url = sliderItem.getWeburl();
                    viewHolder.button.setText("View More");
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                }else {

                    Intent intent=new Intent(context, SharedProfileActivity.class);
                    intent.putExtra("profileid",sliderItem.getUsername());
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;
        Button button;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
           button= itemView.findViewById(R.id.button);
            this.itemView = itemView;
        }
    }
    private void setAnimation(View viewanimate, int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.left_right);
        viewanimate.setAnimation(animation);
    }

}