package com.nexus.unify.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.Common.Constants;
import com.nexus.unify.Common.NodeNames;
import com.nexus.unify.Common.Util;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.R;
import com.nexus.unify.lottiedialoglike;
import com.nexus.unify.lottiedialogswipe;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StrikeClick {

    private static FirebaseUser firebaseUser;
    static FirebaseDatabase database;

    public static void main(String[] args) {

    }

    public static void add(ImageView btn_like, String uid, String name, String img1, String token) {
        lottiedialogswipe lottie;
        lottie = new lottiedialogswipe(btn_like.getContext());
        lottie.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write whatever to want to do after delay specified (1 sec)
                lottie.dismiss();
                          /*  Intent intent = new Intent(btn_like.getContext(),MessageActivity.class);
                            intent.putExtra("userid",items.get(getAdapterPosition()).getUid());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            btn_close.getContext().startActivity(intent);*/
                DatabaseReference friendRequestDatabase;
                FirebaseUser currentUser;

                friendRequestDatabase = FirebaseDatabase.getInstance().getReference().child(NodeNames.FRIEND_REQUESTS);
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
             //   holder.btnSendRequest.setVisibility(View.GONE);
               // holder.pbRequest.setVisibility(View.VISIBLE);

            String    userId = uid;

                friendRequestDatabase.child(currentUser.getUid()).child(userId).child(NodeNames.REQUEST_TYPE)
                        .setValue(Constants.REQUEST_STATUS_SENT).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    friendRequestDatabase.child(userId).child(currentUser.getUid()).child(NodeNames.REQUEST_TYPE)
                                            .setValue(Constants.REQUEST_STATUS_RECEIVED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {


                                                        Toast.makeText(btn_like.getContext(), R.string.request_sent_successfully, Toast.LENGTH_SHORT).show();

                                                        String title = "New Friend Request";
                                                        String message= "You have a new friend request";
                                                        Util.sendNotification(btn_like.getContext(), title, message, userId);

                                                    }
                                                    else
                                                    {


                                                    }
                                                }
                                            });
                                }
                                else
                                {

                                }

                            }
                        });






            }
        }, 1000);

    }
    public static void likenormal(Context context, String uid) {
        lottiedialoglike lottie;
        lottie = new lottiedialoglike(context);
        lottie.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write whatever to want to do after delay specified (1 sec)
                lottie.dismiss();
                addNotifications(uid);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String message = "Wow! you have a new ❤";
                       // sendNotification(user.getName(), message, user.getToken(),context);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 1000);

        //  FirebaseDatabase.getInstance().getReference().child("RemoveSwipe").child(firebaseUser.getUid())
        //   .child(items.get(getAdapterPosition()).getUid()).setValue(true);

    }

    public static void like(ImageView btn_close, String uid) {
        lottiedialoglike lottie;
        lottie = new lottiedialoglike(btn_close.getContext());
        lottie.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write whatever to want to do after delay specified (1 sec)
                lottie.dismiss();
                addNotifications(uid);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String message = "Wow! you have a new ❤";
                        sendNotification(user.getName(), message, user.getToken(), btn_close);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 1000);

        //  FirebaseDatabase.getInstance().getReference().child("RemoveSwipe").child(firebaseUser.getUid())
        //   .child(items.get(getAdapterPosition()).getUid()).setValue(true);

    }
   public static boolean check(String intrests, String intrests1, Context context) {

        if (intrests1!=null){
            String str = intrests.replaceAll("\\s", "");
            String str1 = intrests1.replaceAll(","," ");
            String words[] = str.split(",");

            for(String token : words) {

                if ((str1.toLowerCase().trim()).contains(token.toLowerCase().trim())) {
                   return  true;
                }
                else {
                return false;
                }


            }


        }
return false;
    }

    public static void report(String uid, ImageView btn_like) {


        PopupMenu popupMenu = new PopupMenu(btn_like.getContext(), btn_like);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.menu_swipe, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getTitle().equals("Report")) {

                    FirebaseUser firebaseUser2;
                    firebaseUser2 = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Reports").child(firebaseUser2.getUid());
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("username", uid);

                    reference1.updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(btn_like.getContext(), "Profile Reported", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {

                    FirebaseUser firebaseUser2;
                    firebaseUser2 = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Blocks").child(firebaseUser2.getUid());
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("username", uid);

                    reference1.updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(btn_like.getContext(), "Profile Blocked", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }



    static void sendNotification(String name, String message, String token, ImageView btn_like) {
        try {

            RequestQueue queue = Volley.newRequestQueue(btn_like.getContext());

            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject data = new JSONObject();
            data.put("title", name);
            data.put("body", message);

            JSONObject notificationData = new JSONObject();
            notificationData.put("notification", data);
            notificationData.put("to", token);

            JsonObjectRequest request = new JsonObjectRequest(url, notificationData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // Toast.makeText(btn_like.getContext(), "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //
                    //  Toast.makeText(btn_like.getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    // Firebase Cloud Messaging Server Key
                    String key = "Key=AAAAjdmdefg:APA91bH763tdSLSoVhyO1x316YpWs8ABYzL1sm-plQBZfM1DM_b2YUCylweL3hgTFCwGdnYtlExqH8l8bEg_frUZvgc011eby-penOPyV3o6PYISaNQA4eR90nr-RGtgJot--2IYHKlG";
                    hashMap.put("Authorization", key);
                    hashMap.put("Content-Type", "application/json");
                    return hashMap;
                }
            };

            queue.add(request);
        } catch (Exception ex) {
            Toast.makeText(btn_like.getContext(), "Notification sending error!", Toast.LENGTH_SHORT).show();
        }
    }

    public static double findDifference(String start_date, String end_date, Context mContext) {
        Toast.makeText(mContext, start_date, Toast.LENGTH_SHORT).show();
        return 5.000000000000000000;
    }



    private static void addNotifications(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "Liked Your Profile");
        hashMap.put("postid", "");
        hashMap.put("isPost", false);
        reference.push().setValue(hashMap);

    }

    public static String getCount(Activity context) {
        DatabaseReference  databaseReferenceUsers;
        final String[] fullName = new String[1];
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("values");
        databaseReferenceUsers.child("limit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullName[0] = dataSnapshot.child("swipelimit").getValue()!=null?
                        dataSnapshot.child("swipelimit").getValue().toString():"";


                Toast.makeText(context, fullName.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
        return String.valueOf(fullName);

    }
}
