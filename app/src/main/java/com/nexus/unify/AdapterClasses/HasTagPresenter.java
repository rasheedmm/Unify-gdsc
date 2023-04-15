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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;
import com.nexus.unify.ModelClasses.Hashtags;
import com.nexus.unify.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HasTagPresenter extends RecyclerViewPresenter<Hashtags> {

    @SuppressWarnings("WeakerAccess")
    protected Adapter adapter;

    @SuppressWarnings("WeakerAccess")
    public HasTagPresenter(@NonNull Context context) {
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
        List<Hashtags> hashtagsList;
       hashtagsList = new ArrayList<Hashtags>();




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("hashtags");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               hashtagsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Hashtags hashtags = postSnapshot.getValue(Hashtags.class);

                    if (!hashtags.getId().equals("latest99")) {

                        hashtagsList.add(hashtags);
                    }




                }
                Collections.reverse(hashtagsList);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        if (TextUtils.isEmpty(query)) {
            adapter.setData(hashtagsList);
        } else {
            query = query.toString().toLowerCase();
            List<Hashtags> list = new ArrayList<>();
            for (Hashtags u : hashtagsList) {
                if (u.getTag().toLowerCase().contains(query) ||
                        u.getTag().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("UserPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Hashtags> data;

        @SuppressWarnings("WeakerAccess")
        protected class Holder extends RecyclerView.ViewHolder {
            private final View root;
            private final TextView fullname;

            Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = itemView.findViewById(R.id.tv_hashtag);

            }
        }

        @SuppressWarnings("WeakerAccess")
        protected void setData(@Nullable List<Hashtags> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.hashtag_view, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("No hashtag here!");

                holder.root.setOnClickListener(null);
                return;
            }
            final Hashtags hashtags = data.get(position);
            holder.fullname.setText(hashtags.getTag());


            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(hashtags);
                }
            });
        }
    }
}
