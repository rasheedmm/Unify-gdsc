package com.nexus.unify.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.MainChatsActivity;
import com.nexus.unify.ModelClasses.Ads;
import com.nexus.unify.R;
import com.nexus.unify.ResultActivity;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.SliderViewHolder> {

    private List<Ads> sliderItems;
    private ViewPager2 viewPager2;
    Context context;

    public AdsAdapter(List<Ads> sliderItems, ViewPager2 viewPager2, Context context) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.ads_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull SliderViewHolder holder, int position) {
        setAnimation(holder.itemView, position);
        holder.setImage(sliderItems.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliderItems.get(position).getType().equals("web")) {


                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(sliderItems.get(position).getWeburl()));
                    context.startActivity(i);

                } else if (sliderItems.get(position).getType().equals("chat")) {
                    context.startActivity(new Intent(context, MainChatsActivity.class));

                } else if (sliderItems.get(position).getType().equals("post")) {
                    context.startActivity(new Intent(context, ResultActivity.class));


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


        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);


        }

        void setImage(Ads sliderItem) {
            try {
                if (context == null) {

                } else {
                    Glide.with(context)
                            .load(sliderItem.getUrl())
                            .fitCenter()
                            .into(imageView);
                }

            } catch (Exception e) {

            }


        }
    }

    private void setAnimation(View viewanimate, int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        viewanimate.setAnimation(animation);
    }
}
