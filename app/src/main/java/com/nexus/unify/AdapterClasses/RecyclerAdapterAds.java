package com.nexus.unify.AdapterClasses;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nexus.unify.ModelClasses.Ads;
import com.nexus.unify.PlayerActivity;
import com.nexus.unify.R;

import java.util.List;

public class RecyclerAdapterAds extends RecyclerView.Adapter {
    Context context;
    private static final String TAG = "RecyclerAdapter";
    List<Ads> moviesList;
    private final int limit = 10;
    public RecyclerAdapterAds(List<Ads> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position % 2 == 0) {
//            return 0;
//        }
//        return 1;


        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.row_item, parent, false);
            return new ViewHolderOne(view);
        }

        view = layoutInflater.inflate(R.layout.another_row_item, parent, false);
        return new ViewHolderTwo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

//        if (position % 2 == 0) {
//            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
//            viewHolderOne.textView.setText(moviesList.get(position));
//            viewHolderOne.rowCountTextView.setText(String.valueOf(position));
//        } else {
//            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
//            viewHolderTwo.textView.setText(moviesList.get(position));
//        }


        if (moviesList.get(position).getType().equals("image")) {


            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            Glide.with(context).load(moviesList.get(position).getUrl())
                    .placeholder(R.drawable.post_place)
                    .into(viewHolderOne.imageView);


            viewHolderOne.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context==null){
                        return;
                    }else {
                        try {
                            Intent i = new Intent(context, PlayerActivity.class);
                            SharedPreferences.Editor editor = context.getSharedPreferences("MY", MODE_PRIVATE).edit();
                            editor.putInt("pos", position+1);

                            editor.apply();
                            i.putExtra("pos", position+1);
                            context.startActivity(i);
                        }catch (Exception e){


                        }


                    }

                }
            });
            if (moviesList.get(position).getUrl().equals("null")) {
                viewHolderOne.imageView.setVisibility(View.GONE);
                viewHolderOne.textView.setVisibility(View.VISIBLE);
               // viewHolderOne.textView.setText(moviesList.get(position).getText());
            }else {

            }

        } else {
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            Glide.with(context).load(moviesList.get(position).getUrl())
                    .placeholder(R.drawable.avatar)
                    .into(viewHolderTwo.imageView1);
            viewHolderTwo.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context==null){
                        return;
                    }else {

                        try {
                            Intent i = new Intent(context, PlayerActivity.class);
                            SharedPreferences.Editor editor = context.getSharedPreferences("MY", MODE_PRIVATE).edit();
                            editor.putInt("pos", position+1);

                            editor.apply();
                            i.putExtra("pos", position+1);
                            context.startActivity(i);
                        }catch (Exception e){


                        }
                    }

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if(moviesList.size() > limit){
            return limit;
        }
        else
        {
            return moviesList.size();
        }
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.text_desc);
            cardView = itemView.findViewById(R.id.view2);

        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {

        ImageView imageView1;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);

            imageView1 = itemView.findViewById(R.id.imageView1);

        }
    }
}
