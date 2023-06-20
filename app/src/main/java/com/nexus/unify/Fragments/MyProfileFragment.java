package com.nexus.unify.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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


import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.live.newvideocall.kotlin.Swipe;
import com.nexus.PayoutActivity;
import com.nexus.unify.AdapterClasses.RecyclerAdapter;
import com.nexus.unify.AddPhotosActivity;
import com.nexus.unify.BasicDetailsActivity;
import com.nexus.unify.EditProfileActivity;
import com.nexus.unify.ModelClasses.Posts;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.R;
import com.nexus.unify.SettingsActivity;
import com.nexus.unify.UrlActivity;
import com.nexus.unify.kotlin.MyKotlinKt;


import org.checkerframework.checker.fenum.qual.SwingBoxOrientation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileFragment extends Fragment {

    FirebaseUser firebaseUser;
    String profileid;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<Posts> postsList;
    ImageView btn_edit_profile, btn_settings;
    TextView username, name;
    CircleImageView img_profile;
    ImageView img_back;
    LinearLayout btn_edit, share;
    TextView tv_crs, tv_dep, bio;
    private ShimmerFrameLayout mShimmerViewContainer;
    private Uri dynamicLink = null;
    private static final String TAG = "DynamicLinks";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        btn_settings = view.findViewById(R.id.img_settings);
        username = view.findViewById(R.id.tv_username);
        recyclerView = view.findViewById(R.id.recyclerView);
        name = view.findViewById(R.id.tv_name);
        img_profile = view.findViewById(R.id.avt_view);
        postsList = new ArrayList<Posts>();
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        bio = view.findViewById(R.id.tv_bio);
        img_back = view.findViewById(R.id.imageView);
        tv_crs = view.findViewById(R.id.tv_crs);
        share = view.findViewById(R.id.share);
        tv_dep = view.findViewById(R.id.tv_dep);
        btn_edit = view.findViewById(R.id.logout2);
        mShimmerViewContainer.startShimmer();
        btn_edit_profile = view.findViewById(R.id.img_edit_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profileid = firebaseUser.getUid();


        userInfo();
        recyclerAdapter = new RecyclerAdapter(postsList, getContext());

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setAdapter(recyclerAdapter);
        addPosts();

        /*    tv_fans_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
   Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });
        tv_hooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");
                startActivity(intent);

            }
        });



    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        addPosts();
        recyclerView.setAdapter(recyclerAdapter);*/

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createlink();
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UrlActivity.class));
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(getContext(), PayoutActivity.class));
            }
        });
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
        return view;
    }



    public void createlink() {

        // manuall link
        String sharelinktext  = "https://bookease.page.link/?"+
                "link=http://www.malluhub.tk/myrefer.php?custid="+firebaseUser.getUid()+"-"+"456"+
                "&apn="+ getActivity().getPackageName()+
                "&st="+"BOOK EASE App"+
                "&sd="+"Shared"+
                "&si="+"https://www.pngitem.com/pimgs/m/215-2157237_transparent-celebration-vector-png-transparent-celebration-ribbon-png.png";
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(sharelinktext))
                .setDomainUriPrefix("https://bookease.page.link")

                // Set parameters
                // ...
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            String shareMessage = String.valueOf(shortLink);
                            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            intent.setType("text/plain");
                            startActivity(Intent.createChooser(intent, "Share  via"));
                        } else {
                            // Error
                            // ...
                        }
                    }
                });








    }





    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }
                User user = snapshot.getValue(User.class);
                if (snapshot.hasChild("username")) {


                    username.setText(user.getUsername());
                    name.setText(user.getName());
                    bio.setText(user.getBio());
                    tv_crs.setText(user.getCourse());
                    tv_dep.setText(user.getDep());
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    //  tv_intrests.setText(user.getIntrests());

                } else {
                    startActivity(new Intent(getContext(), BasicDetailsActivity.class));
                    Toast.makeText(getContext(), "Please Add basic details", Toast.LENGTH_SHORT).show();
                }
                if (snapshot.hasChild("img1")) {

                    Glide.with(getContext()).load(user.getImg1())
                            .placeholder(R.drawable.avatar)
                            .into(img_profile);

                    Glide.with(getContext()).load(user.getImg1())
                            .placeholder(R.drawable.avatar)
                            .into(img_back);

                } else {
                    startActivity(new Intent(getContext(), AddPhotosActivity.class));
                    Toast.makeText(getContext(), "Please Add Photo to Continue", Toast.LENGTH_SHORT).show();
                }


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

                    if (posts.getPublisher().equals(firebaseUser.getUid())) {

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

}