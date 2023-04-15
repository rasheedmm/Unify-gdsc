package com.nexus.unify.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nexus.unify.AdapterClasses.AdsAdapter;
import com.nexus.unify.AdapterClasses.RecyclerAdapter;
import com.nexus.unify.AdapterClasses.SliderAdapter;
import com.nexus.unify.BugActivity;
import com.nexus.unify.MainChatsActivity;
import com.nexus.unify.ModelClasses.Ads;
import com.nexus.unify.ModelClasses.Posts;
import com.nexus.unify.ModelClasses.SliderItem;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.R;
import com.nexus.unify.ResultActivity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeNewFragment extends Fragment {

    RecyclerView recyclerView, rc_cnf, rv, rv_events, rv_others;
    RecyclerAdapter recyclerAdapter, recyclerAdapterCnf, recyclerAdapterevents, recyclerAdapterOthr;
    ImageView chats, btn_addposts, btn_chat, btn_connect, img1;
    List<Posts> postsList, post_cnf_list, list_other, list_events;
    List<Ads> list_ads;
    List<SliderItem> slidsList;
    FirebaseUser firebaseUser;
    ViewPager2 viewPager2, viewPagerads;
    SliderAdapter sliderAdapterExample;
    AdsAdapter adsAdapter;
    CircleImageView user_avatar;
    TextView user, tv_addposts, tv_chat, tv_connect;
    private ShimmerFrameLayout mShimmerViewContainer;
    Class<?> myClass1;
    String myClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home_new, container, false);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        postsList = new ArrayList<Posts>();
        post_cnf_list = new ArrayList<Posts>();
        list_ads = new ArrayList<Ads>();
        slidsList = new ArrayList<SliderItem>();
        list_other = new ArrayList<Posts>();
        list_events = new ArrayList<Posts>();
        recyclerView = view.findViewById(R.id.recyclerView);
        viewPager2 = view.findViewById(R.id.viewpagerimageslider);
        rv_others = view.findViewById(R.id.recyclerViewothers);
        rv_events = view.findViewById(R.id.recyclerViewevnts);
        viewPagerads = view.findViewById(R.id.recyclerViewads);
        btn_addposts = view.findViewById(R.id.imageView11);
        btn_chat = view.findViewById(R.id.imageView12);
        btn_connect = view.findViewById(R.id.imageView14);
        user_avatar = view.findViewById(R.id.circleImageView);
        rc_cnf = view.findViewById(R.id.recyclerViewcnf);
        tv_addposts = view.findViewById(R.id.textView12);
        img1 = view.findViewById(R.id.img1);
        tv_chat = view.findViewById(R.id.textView13);
        tv_connect = view.findViewById(R.id.textView14);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        viewPagerads.setClipChildren(false);
        viewPagerads.setClipToPadding(false);
        viewPagerads.setOffscreenPageLimit(3);
        viewPagerads.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        user = view.findViewById(R.id.textView10);

      //  setAnimation(user);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPagerads.setPageTransformer(compositePageTransformer);

        recyclerAdapter = new RecyclerAdapter(postsList, getContext());
        recyclerAdapterCnf = new RecyclerAdapter(post_cnf_list, getContext());
        recyclerAdapterOthr = new RecyclerAdapter(list_other, getContext());
        recyclerAdapterevents = new RecyclerAdapter(list_events, getContext());


//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chats = view.findViewById(R.id.btn_chat);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);

                if (snapshot.hasChild("name")){
                    user.setText("Hi "+user1.getName());

                }


                if (getContext()==null){
                    return;
                }
                else {
                    Glide.with(getContext()).load(user1.getImg1())
                            .placeholder(R.drawable.avatar)
                            .into(user_avatar);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setAdapter(recyclerAdapter);


        rc_cnf.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        rc_cnf.setAdapter(recyclerAdapterCnf);

        rv_others.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        rv_others.setAdapter(recyclerAdapterOthr);

        rv_events.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        rv_events.setAdapter(recyclerAdapterevents);

        sliderAdapterExample = new SliderAdapter(slidsList, viewPager2, getContext());
        adsAdapter = new AdsAdapter(list_ads, viewPagerads, getContext());
        viewPager2.setAdapter(sliderAdapterExample);
        viewPagerads.setAdapter(adsAdapter);


        addSlides();
        addAds();
        addPosts();
        adddynamic();
        addCnf();
        addevents();
        addothers();
btn_chat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), MainChatsActivity.class));
    }
});
        tv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainChatsActivity.class));
            }
        });
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ResultActivity.class));
            }
        });
        btn_addposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ResultActivity.class));
            }
        });
        tv_addposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ResultActivity.class));
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BugActivity.class));
            }
        });
        tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BugActivity.class));
            }
        });
        return view;
    }

    private void addevents() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_events.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Posts posts = postSnapshot.getValue(Posts.class);

                    if (posts.getPrivacy().equals("Events")) {

                        list_events.add(posts);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }


                }
                Collections.reverse(list_events);
                recyclerAdapterevents.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addothers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_other.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Posts posts = postSnapshot.getValue(Posts.class);

                    if (posts.getPrivacy().equals("Courses")) {

                        list_other.add(posts);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }


                }
                Collections.reverse(list_other);
                recyclerAdapterOthr.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Posts posts = postSnapshot.getValue(Posts.class);

                    if (posts.getPrivacy().equals("Career")) {

                        postsList.add(posts);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }


                }
                Collections.reverse(postsList);
                recyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readuser() {


    }


    private void addSlides() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Slides");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slidsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SliderItem slider = postSnapshot.getValue(SliderItem.class);


                    slidsList.add(slider);


                }
                Collections.reverse(slidsList);
                sliderAdapterExample.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addAds() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_ads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Ads slider = postSnapshot.getValue(Ads.class);


                    list_ads.add(slider);


                }
                Collections.reverse(list_ads);
                adsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void adddynamic() {
        FirebaseRemoteConfig mFirebaseRemoteConfig;
        FirebaseRemoteConfigSettings configSettings;

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            String updated = String.valueOf(task.getResult());

                            String value = mFirebaseRemoteConfig.getString("img1");
                            String typ = mFirebaseRemoteConfig.getString("typ1");

                            if (!value.equals("null")) {

                                if ((getContext() == null)) {


                                } else {

                                    img1.setVisibility(View.VISIBLE);
                                    Glide.with(getContext()).load(value)
                                            .into(img1);

                                    img1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (typ.equals("web")) {

                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(value));
                                                startActivity(i);

                                            } else if (typ.equals("chat")) {
                                                startActivity(new Intent(getContext(), MainChatsActivity.class));

                                            } else if (typ.equals("post")) {
                                                startActivity(new Intent(getContext(), ResultActivity.class));
                                            }


                                        }
                                    });


                                }


                            }
                        } else {


                        }
                        //
                    }
                });


    }

    private void addCnf() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post_cnf_list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Posts posts = postSnapshot.getValue(Posts.class);

                    if (posts.getPrivacy().equals("Forum")) {

                        post_cnf_list.add(posts);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }


                }
                Collections.reverse(post_cnf_list);
                recyclerAdapterCnf.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setAnimation(View viewanimaten) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
       viewanimaten.setAnimation(animation);
    }
}