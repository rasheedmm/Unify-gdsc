package com.nexus.unify.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.ModelClasses.Notification;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.R;
import com.nexus.unify.SharedProfileActivity;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Notification> mNotification;

    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = mNotification.get(position);
        holder.text.setText(notification.getText());
        getUserInfo(holder.image_profile, holder.username, notification.getUserid());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(notification.getUserid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);
                        if (snapshot.hasChild("username")) {

                            Intent intent = new Intent(mContext, SharedProfileActivity.class);
                            intent.putExtra("uid", user.getUid());
                            mContext.startActivity(intent);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        setAnimation(holder.cardView, position);
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView image_profile;
        public TextView username, text;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.contact);
            text = itemView.findViewById(R.id.text);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    private void getUserInfo(ImageView imageView, TextView username, String publisherid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (mContext == null) {
                    return;
                }
                try {
                    Glide.with(mContext).load(user.getImg1()).into(imageView);
                    username.setText(user.getName());

                } catch (Exception e) {


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setAnimation(View viewanimate, int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
        viewanimate.setAnimation(animation);
    }

}
