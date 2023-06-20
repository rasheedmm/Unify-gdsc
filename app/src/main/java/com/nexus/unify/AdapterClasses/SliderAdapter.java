package com.nexus.unify.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.ModelClasses.SliderItem;
import com.nexus.unify.R;
import com.nexus.unify.SharedProfileActivity;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    Context context;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2, Context context) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull SliderViewHolder holder, int position) {
setAnimation(holder.click,position);
        holder.setImage(sliderItems.get(position));
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliderItems.get(position).getUsername().equals("null")) {
                    String url = sliderItems.get(position).getWeburl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                } else {




                    Intent intent = new Intent(context, SharedProfileActivity.class);
                    intent.putExtra("uid", sliderItems.get(position).getUsername());
                   context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;
        AppCompatButton click;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            click = itemView.findViewById(R.id.getStarted);

        }

        void setImage(SliderItem sliderItem) {
         try {
             if (context==null){

             }else {
                 Glide.with(context)
                         .load(sliderItem.getUrl())
                         .fitCenter()
                         .into(imageView);
             }

         }catch (Exception e){

         }
            if (sliderItem.getUsername().equals("null")) {

                click.setText("Featured Store");
            } else {
                click.setText("View Store");
            }

        }
    }
    private void setAnimation(View viewanimate, int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        viewanimate.setAnimation(animation);
    }
}
