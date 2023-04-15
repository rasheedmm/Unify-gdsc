package com.nexus.unify.Listeners;

import android.widget.ImageView;

import com.nexus.unify.ModelClasses.Bgimages;


public interface BgimgClickListener {


        void onBgsClick(Bgimages getBgImages, ImageView imageView) ;


        void onBgsLongClick(Bgimages getBgImages, ImageView imgMovie);
    }
