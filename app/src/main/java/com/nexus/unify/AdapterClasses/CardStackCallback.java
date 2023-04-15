package com.nexus.unify.AdapterClasses;

import androidx.recyclerview.widget.DiffUtil;


import com.nexus.unify.ModelClasses.User;

import java.util.List;

public class CardStackCallback extends DiffUtil.Callback {

    private final List<User> old;
    private final List<User> baru;

    public CardStackCallback(List<User> old, List<User> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getImg1() == baru.get(newItemPosition).getImg1();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}