package com.nexus.unify.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.AdapterClasses.ChatListAdapterAnms;
import com.nexus.unify.ChatsAnmsNewActivity;
import com.nexus.unify.Common.Extras;
import com.nexus.unify.Common.NodeNames;
import com.nexus.unify.ModelClasses.ChatListModel;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.R;
import com.nexus.unify.lottiedialogfragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindFriendsFragment extends Fragment {

    private RecyclerView rvChatList;

    private TextView tvEmptyChatList;
    LinearLayoutManager linearLayoutManager;
    private ChatListAdapterAnms chatListAdapter;
    private List<ChatListModel> chatListModelList;
    DatabaseReference reference;
    private DatabaseReference databaseReferenceChats, databaseReferenceUsers;
    private FirebaseUser currentUser;
    public List<User> mUsers;
    private ChildEventListener childEventListener;
    private Query query;
    private FirebaseUser firebaseUser;
    private List<String> userIds;
    LottieAnimationView lottie1;
    private Random randomGenerator;
    lottiedialogfragment lottie;

    public FindFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChatList = view.findViewById(R.id.rvChats);
        tvEmptyChatList = view.findViewById(R.id.tvEmptyChatList);
        lottie1 = view.findViewById(R.id.lottie1);
        userIds = new ArrayList<>();
        chatListModelList = new ArrayList<>();
        chatListAdapter = new ChatListAdapterAnms(getActivity(), chatListModelList);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false);
  // linearLayoutManager.setReverseLayout(true);
        // linearLayoutManager.setStackFro

        rvChatList.smoothScrollToPosition(0);
        rvChatList.setLayoutManager(linearLayoutManager);
        mUsers = new ArrayList<>();
        rvChatList.setAdapter(chatListAdapter);


        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child(NodeNames.CHATSANMS).child(currentUser.getUid());

        query = databaseReferenceChats.orderByChild(NodeNames.TIME_STAMP);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                updateList(dataSnapshot, true, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                updateList(dataSnapshot, false, dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addChildEventListener(childEventListener);


        tvEmptyChatList.setVisibility(View.VISIBLE);
        lottie1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("profiles");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mUsers.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);

                            if (user.getUid() != firebaseUser.getUid()) {
                                mUsers.add(user);

                            }

                            randomGenerator = new Random();


                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(mUsers.get(new Random().nextInt(mUsers.size())).getUid());
                        ;
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                User user = snapshot.getValue(User.class);
                                if (snapshot.hasChild("name")) {


                                    Intent intent = new Intent(getContext(), ChatsAnmsNewActivity.class);
                                    intent.putExtra(Extras.USER_KEY, user.getUid());
                                    intent.putExtra(Extras.USER_NAME, user.getAnmsname());
                                    intent.putExtra(Extras.PHOTO_NAME, user.getAnmsimg());
                                    intent.putExtra("Message", "Hi");
                                    getContext().startActivity(intent);


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });
    }


    private void updateList(DataSnapshot dataSnapshot, final boolean isNew, final String userId) {

        tvEmptyChatList.setVisibility(View.GONE);

        final String lastMessage, lastMessageTime, unreadCount;

        if (dataSnapshot.child(NodeNames.LAST_MESSAGE).getValue() != null)
            lastMessage = dataSnapshot.child(NodeNames.LAST_MESSAGE).getValue().toString();
        else
            lastMessage = "";

        if (dataSnapshot.child(NodeNames.LAST_MESSAGE_TIME).getValue() != null)
            lastMessageTime = dataSnapshot.child(NodeNames.LAST_MESSAGE_TIME).getValue().toString();

        else
            lastMessageTime = "";

        unreadCount = dataSnapshot.child(NodeNames.UNREAD_COUNT).getValue() == null ?
                "0" : dataSnapshot.child(NodeNames.UNREAD_COUNT).getValue().toString();

        databaseReferenceUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //chatListModelList.clear();
                String fullName = dataSnapshot.child(NodeNames.NAMEANMS).getValue() != null ?
                        dataSnapshot.child(NodeNames.NAMEANMS).getValue().toString() : "";

                String photoName = dataSnapshot.child(NodeNames.PHOTOANMS).getValue() != null ?
                        dataSnapshot.child(NodeNames.PHOTOANMS).getValue().toString() : "";


                ChatListModel chatListModel = new ChatListModel(userId, fullName, photoName, unreadCount, lastMessage, lastMessageTime);
                chatListAdapter.notifyDataSetChanged();
                if (isNew) {
                    chatListModelList.add(chatListModel);

                    userIds.add(userId);


                } else {
                    int indexOfClickedUser = userIds.indexOf(userId);
                    chatListModelList.set(indexOfClickedUser, chatListModel);
                }

                Collections.reverse(userIds);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), getActivity().getString(R.string.failed_to_fetch_chat_list, databaseError.getMessage())
                        , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        query.removeEventListener(childEventListener);
    }
}

