package com.nexus.unify.AdapterClasses;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.ModelClasses.Avatars;
import com.nexus.unify.R;


import java.util.ArrayList;

public class AvatarsAdapter extends RecyclerView.Adapter<AvatarsAdapter.MultiViewHolder> {
    public String url="";
    FirebaseUser firebaseUser;
    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_avatars,
                parent, false);

        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        holder.bind(intrests.get(position));

    }

    @Override
    public int getItemCount() {
        return intrests.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        LinearLayout linearLayout;
        RoundedImageView imageView;


        public MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.int_name);
            linearLayout = itemView.findViewById(R.id.linear_item);
            imageView = itemView.findViewById(R.id.img_avatar);

        }

        void bind(final Avatars intrests) {


            textView.setText(intrests.getName());
            Glide.with(context).load(intrests.getUrl()) .placeholder(R.drawable.loadingavt).into(imageView);
            if (intrests.isCheked()) {
                linearLayout.setBackgroundResource(R.drawable.item_stroked_selected);
            } else {
                linearLayout.setBackgroundResource(R.drawable.item_stroked);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intrests.setCheked(!intrests.isCheked());

                    if (intrests.isCheked()) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        linearLayout.setBackgroundResource(R.drawable.item_stroked_selected);
                        bottomSheetDialog.cancel();
                        Glide.with(context).load(intrests.getUrl()).into(img);
                        url = intrests.getUrl();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

                        SharedPreferences.Editor myEdit = sharedPreferences.edit();


                     sharedPreferences.edit().remove("anmsimg").commit();
                        myEdit.putString("anmsimg",intrests.getUrl());

                        myEdit.commit();


                    } else {
                        linearLayout.setBackgroundResource(R.drawable.item_stroked);
                    }

                }
            });
        }


    }

    public String getAll() {
        return intrests.get(intrests.indexOf(0)).getUrl();


    }

    public void setIntrests(ArrayList<Avatars> intrests) {
        this.intrests = new ArrayList<>();
        this.intrests = intrests;
        notifyDataSetChanged();
    }

    private final Context context;
    private ArrayList<Avatars> intrests;
    private BottomSheetDialog bottomSheetDialog;
    private RoundedImageView img;

    public AvatarsAdapter(Context context, ArrayList<Avatars> intrests, BottomSheetDialog bottomSheetDialog, RoundedImageView img) {
        this.context = context;
        this.intrests = intrests;
        this.bottomSheetDialog = bottomSheetDialog;
        this.img = img;
    }


    public ArrayList<Avatars> getSelected() {
        ArrayList<Avatars> selected = new ArrayList<>();

        for (int i = 0; i < intrests.size(); i++) {
            if (intrests.get(i).isCheked()) {
                selected.add(intrests.get(i));
            }
        }
        return intrests;
    }


}
