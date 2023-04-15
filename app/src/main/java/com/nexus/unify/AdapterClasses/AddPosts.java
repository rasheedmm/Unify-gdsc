package com.nexus.unify.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.nexus.unify.ModelClasses.Comment;
import com.nexus.unify.ModelClasses.Posts;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.PlayerActivity;
import com.nexus.unify.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AddPosts {

    public static void main(String[] args) {

    }


    public static void addposts(ArrayList<Posts> videoList, Context applicationContext, String TYP) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Posts posts = postSnapshot.getValue(Posts.class);

                    if (posts.getPrivacy().equals(TYP)) {

                      videoList.add(posts);



                    } else {

                    }


                }
                Collections.reverse(videoList);
                //  recyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void publisherInfo(ImageView image_profile, TextView username, Posts posts, Context context) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(posts.getPublisher());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (posts.getAnms().equals("on")) {
                    Glide.with(context).load(user.getAnmsimg())
                            .placeholder(R.drawable.avatar)
                            .into(image_profile);
                    username.setText(user.getAnmsname());
                } else {

                    Glide.with(context).load(user.getImg1())
                            .placeholder(R.drawable.avatar)
                            .into(image_profile);
                    username.setText(user.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void more_btn(PlayerActivity context, View v, Posts videoItem) {
        FirebaseUser firebaseUser;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        editPost(videoItem.getPostid(), context);
                        return true;
                    case R.id.delete:


                        new iOSDialogBuilder(context)
                                .setTitle("Confirm Delete!")
                                .setSubtitle("Are you sure want to delete thsi post?")
                                .setBoldPositiveLabel(true)
                                .setCancelable(false)
                                .setPositiveListener(context.getString(R.string.ok), new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {

                                        FirebaseDatabase.getInstance().getReference("Posts")
                                                .child(videoItem.getPostid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .setNegativeListener(context.getString(R.string.dismiss), new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .build().show();

                        return true;
                    case R.id.report:

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reports");
                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("postid", videoItem.getPostid());
                        hashMap.put("user", firebaseUser.getUid());
                        reference.push().setValue(hashMap);
                        Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.comment:
                        ////  showcomments(videoItem.getPostid(), videoItem.getPublisher(), context);
                        return true;
                    case R.id.share:

                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.menu_post);
        popupMenu.getMenu().findItem(R.id.share).setVisible(false);
        popupMenu.getMenu().findItem(R.id.comment).setVisible(false);
        if (!videoItem.getPublisher().equals(firebaseUser.getUid())) {
            popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
            popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
        }
        popupMenu.show();

    }

    public static void editPost(String postid, Context context) {

        EditText prdct_name, desc, qnty, mrp, offer;
        TextView cancel, submit;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                context, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.bottomnav_editposts,
                        bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        prdct_name = bottomSheetDialog.findViewById(R.id.prdctname);

        cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
        submit = bottomSheetDialog.findViewById(R.id.btn_submit);

        getText(postid, prdct_name);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("text", prdct_name.getText().toString());


                FirebaseDatabase.getInstance().getReference("Posts")
                        .child(postid).updateChildren(hashMap);
                Toast.makeText(context, "Post Upadated", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.cancel();
            }
        });

    }

    public static void getText(String postid, EditText prdct_name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts")
                .child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prdct_name.setText(snapshot.getValue(Posts.class).getText());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void showcomments(String postid, String publisher, Context mcontext) {


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                mcontext, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(mcontext)
                .inflate(R.layout.bottom_sheet_container_comments,
                        bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();


        RecyclerView recyclerView;
        CommentAdapter commentAdapter;
        List<Comment> commentList;
        EditText addcomment;
        ImageView image_profile;
        TextView post, love, angry, sadcry, superone, fire, awsome;
        ImageView like, dislike;
        String username;
        String publisherid;
        FirebaseUser firebaseUser;


        angry = bottomSheetDialog.findViewById(R.id.angry);
        sadcry = bottomSheetDialog.findViewById(R.id.sadcry);
        superone = bottomSheetDialog.findViewById(R.id.superone);
        dislike = bottomSheetDialog.findViewById(R.id.dislike);
        like = bottomSheetDialog.findViewById(R.id.like);
        fire = bottomSheetDialog.findViewById(R.id.fire);
        awsome = bottomSheetDialog.findViewById(R.id.awesome);

        recyclerView = bottomSheetDialog.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(mcontext, commentList);
        recyclerView.setAdapter(commentAdapter);

        addcomment = bottomSheetDialog.findViewById(R.id.add_comment);
        image_profile = bottomSheetDialog.findViewById(R.id.image_profile);
        post = bottomSheetDialog.findViewById(R.id.post);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        postid = postid;
        publisherid = publisher;
        love = bottomSheetDialog.findViewById(R.id.love);


        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("❤❤❤❤");
            }
        });
        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("😡😡😡");
            }
        });
        sadcry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("😭😭😭");
            }
        });
        superone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("👌👌👌");
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("👎👎👎");
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("👍👍👍");
            }
        });
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("🔥🔥🔥");
            }
        });
        awsome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcomment.setText("❤❤❤");
            }
        });


        String finalPostid = postid;
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addcomment.getText().toString().equals("")) {
                    Toast.makeText(mcontext, "Write Something", Toast.LENGTH_SHORT).show();
                } else {
                    new iOSDialogBuilder(mcontext)
                            .setTitle("Privacy!")
                            .setSubtitle("Do you want to put this comment as anonymous?")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener(mcontext.getString(R.string.yes),new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    addComment(addcomment, finalPostid, firebaseUser);
                                    dialog.dismiss();

                                }
                            })
                            .setNegativeListener(mcontext.getString(R.string.no), new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    addCommentnrm(addcomment, finalPostid, firebaseUser);
                                    dialog.dismiss();
                                }
                            })
                            .build().show();










                }
            }
        });
        getImage(image_profile, mcontext);
        readComment(commentList, commentAdapter, postid);


    }

    private static void getImage(ImageView image_profile, Context mcontext) {
        FirebaseUser firebaseUser;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mcontext.getApplicationContext()).load(user.getImg1()).into(image_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void addCommentnrm(EditText addcomment, String postid, FirebaseUser firebaseUser) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("type", "nrm");
        hashMap.put("publisher", firebaseUser.getUid());
        reference.push().setValue(hashMap);

        addNotificationscomments(addcomment, postid, firebaseUser);
        addcomment.setText("");
    }
    private static void addComment(EditText addcomment, String postid, FirebaseUser firebaseUser) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("comment", addcomment.getText().toString());

        hashMap.put("publisher", firebaseUser.getUid());
        reference.push().setValue(hashMap);

        addNotificationscomments(addcomment, postid, firebaseUser);
        addcomment.setText("");
    }

    private static void addNotificationscomments(EditText addcomment, String postid, FirebaseUser firebaseUser) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "commented:" + addcomment.getText().toString());
        hashMap.put("postid", postid);
        hashMap.put("isPost", true);
        reference.push().setValue(hashMap);

    }

    private static void readComment(List<Comment> commentList, CommentAdapter commentAdapter, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


}