package com.nexus.unify.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nexus.unify.ChatsAnmsNewActivity;
import com.nexus.unify.Common.Extras;
import com.nexus.unify.Common.Util;
import com.nexus.unify.ModelClasses.ChatListModel;
import com.nexus.unify.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ChatListAdapterAnms extends RecyclerView.Adapter<ChatListAdapterAnms.ChatListViewHolder> {

    private Context context;
    private List<ChatListModel> chatListModelList;
    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

    public ChatListAdapterAnms(Context context, List<ChatListModel> chatListModelList) {
        this.context = context;
        this.chatListModelList = chatListModelList;
    }


    @NonNull
    @Override
    public ChatListAdapterAnms.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout, parent, false);
        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapterAnms.ChatListViewHolder holder, int position) {

        final ChatListModel chatListModel = chatListModelList.get(position);

        holder.tvFullName.setText(chatListModel.getUserName());
        Glide.with(context)
                .load(chatListModel.getPhotoName())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.ivProfile);


        String lastMessage  = chatListModel.getLastMessage();
        lastMessage = lastMessage.length()>30?lastMessage.substring(0,30):lastMessage;
        try {
            holder.tvLastMessage.setText(AESDecryptionMethod(lastMessage));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            holder.tvLastMessage.setText("");
        }

        String lastMessageTime = chatListModel.getLastMessageTime();
        if(lastMessageTime==null) lastMessageTime="";
        if(!TextUtils.isEmpty(lastMessageTime))
            holder.tvLastMessageTime.setText(Util.getTimeAgo(Long.parseLong(lastMessageTime)));


        if(!chatListModel.getUnreadCount().equals("0"))
        {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(chatListModel.getUnreadCount());
        }
        else
            holder.tvUnreadCount.setVisibility(View.GONE);


        holder.llChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatsAnmsNewActivity.class);
                intent.putExtra(Extras.USER_KEY, chatListModel.getUserId());
                intent.putExtra(Extras.USER_NAME, chatListModel.getUserName());
                intent.putExtra(Extras.PHOTO_NAME, chatListModel.getPhotoName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatListModelList.size();
    }

    public class ChatListViewHolder  extends  RecyclerView.ViewHolder{

        private LinearLayout llChatList;
        private TextView tvFullName, tvLastMessage, tvLastMessageTime, tvUnreadCount;
        private ImageView ivProfile;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            llChatList = itemView.findViewById(R.id.llChatList);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
            tvUnreadCount= itemView.findViewById(R.id.tvUnreadCount);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }
    }
    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = string;

        byte[] decryption;

        try {
            decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException e) {

            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return "";
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return "";
        }
        return decryptedString;
    }
}
