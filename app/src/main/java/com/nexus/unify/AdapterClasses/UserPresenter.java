package com.nexus.unify.AdapterClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.otaliastudios.autocomplete.RecyclerViewPresenter;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UserPresenter extends RecyclerViewPresenter<User> {

    @SuppressWarnings("WeakerAccess")
    protected Adapter adapter;

    @SuppressWarnings("WeakerAccess")
    public UserPresenter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @NonNull
    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<User> usersList;
        usersList = new ArrayList<User>();




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User users = postSnapshot.getValue(User.class);

                    if (!users.getUid().equals("latest99")) {

                        usersList.add(users);
                    }




                }
                Collections.reverse(usersList);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        if (TextUtils.isEmpty(query)) {
            adapter.setData(usersList);
        } else {
            query = query.toString().toLowerCase();
            List<User> list = new ArrayList<>();
            for (User u : usersList) {
                if (u.getName().toLowerCase().contains(query) ||
                        u.getName().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("UserPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<User> data;

        @SuppressWarnings("WeakerAccess")
        protected class Holder extends RecyclerView.ViewHolder {
            private final View root;
            private final TextView fullname;
            private final TextView username;
            private final RoundedImageView imageView;
            Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = itemView.findViewById(R.id.fullname);
                username = itemView.findViewById(R.id.username);
               imageView = itemView.findViewById(R.id.img_profile);
            }
        }

        @SuppressWarnings("WeakerAccess")
        protected void setData(@Nullable List<User> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("No user here!");
                holder.username.setText("Sorry!");
                holder.root.setOnClickListener(null);
                return;
            }
            final User user = data.get(position);
            holder.fullname.setText(user.getName());
            holder.username.setText("@" + user.getUsername());
            Glide.with(getContext()).load(user.getImg1())
                    .placeholder(R.drawable.avatar)
                    .into(holder.imageView);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(user);
                }
            });
        }
    }
}
