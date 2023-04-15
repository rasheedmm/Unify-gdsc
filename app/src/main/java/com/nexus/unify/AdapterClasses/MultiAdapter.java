package com.nexus.unify.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nexus.unify.ModelClasses.Intrests;
import com.nexus.unify.R;

import java.util.ArrayList;

public class MultiAdapter extends RecyclerView.Adapter<MultiAdapter.MultiViewHolder> {

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.item_intrests,
              parent,false);

      return  new MultiViewHolder(view);
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



        public MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.int_name);
            linearLayout= itemView.findViewById(R.id.linear_item);

        }

        void bind(final Intrests intrests) {


            textView.setText(intrests.getName());
            if (intrests.isCheked()){
                linearLayout.setBackgroundResource(R.drawable.item_stroked_selected);
            }else {
                linearLayout.setBackgroundResource(R.drawable.item_stroked);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intrests.setCheked(!intrests.isCheked());

                   if (intrests.isCheked()){
                      linearLayout.setBackgroundResource(R.drawable.item_stroked_selected);
                   }else {
                     linearLayout.setBackgroundResource(R.drawable.item_stroked);
                   }

                }
            });
        }


    }

    public ArrayList<Intrests> getAll() {
        return intrests;


    }

    public void setIntrests(ArrayList<Intrests> intrests){
        this.intrests=new ArrayList<>();
        this.intrests=intrests;
        notifyDataSetChanged();
    }

    private final Context context;
    private ArrayList<Intrests> intrests;

    public MultiAdapter(Context context, ArrayList<Intrests> intrests) {
        this.context = context;
        this.intrests = intrests;
    }


public  ArrayList<Intrests>getSelected(){
     ArrayList<Intrests> selected = new ArrayList<>();

    for(int i=0;i<intrests.size();i++)

    {
        if (intrests.get(i).isCheked()) {
            selected.add(intrests.get(i));
        }
    }
    return selected;
    }


}
