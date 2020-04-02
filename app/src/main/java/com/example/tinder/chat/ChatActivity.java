package com.example.tinder.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tinder.R;
import com.example.tinder.ViewProfileActivity;
import com.example.tinder.matches.MatchActivity;
import com.example.tinder.matches.NewMatchObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    RecyclerView messageRecycleView;
    MessageAdapter messageAdapter;
    List<MessageObject> messageObjectList;
    String matchedUserId;
    String chatId;
    String currentUserId;

    EditText newMessage;
    DatabaseReference chatRef;

    TextView matchedUserName;
    CircularImageView matchedUserAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchedUserName=findViewById(R.id.matchedUserName);
        matchedUserAvatar=findViewById(R.id.matchedUserAvatar);

        newMessage=findViewById(R.id.newMessage);
        matchedUserId=getIntent().getStringExtra("matchedUserId");

        getMatchedUserInfo();

        Log.d("TAG", "onCreate: matchedUserId"+matchedUserId);
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();



        messageObjectList = new ArrayList<MessageObject>();



        messageAdapter = new MessageAdapter(ChatActivity.this, R.layout.item_message, messageObjectList);

        messageRecycleView = findViewById(R.id.messageRecycleView);
        messageRecycleView.setHasFixedSize(false);
        messageRecycleView.setLayoutManager(new LinearLayoutManager(this));
        messageRecycleView.setAdapter(messageAdapter);

        getChatId();


    }

    private void getMatchedUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("users").child(matchedUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            matchedUserName.setText(dataSnapshot.child("name").getValue().toString());
                            if (dataSnapshot.child("avatar_url").getValue().toString().equals("default")){
                                Glide.with(ChatActivity.this).load(R.drawable.avatar).into(matchedUserAvatar);
                            }
                            else {
                                Glide.with(ChatActivity.this).load(dataSnapshot.child("avatar_url").getValue().toString()).into(matchedUserAvatar);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getChatId() {
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUserId).child("connections").child("matches").child(matchedUserId).child("chat_id")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            chatId=dataSnapshot.getValue().toString();
                            getChatList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getChatList(){
        chatRef= FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
        Log.d("TAG",chatRef.getKey());
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    MessageObject messageObject=new MessageObject();
                    messageObject.setMessage(dataSnapshot.child("message").getValue().toString());
                    messageObject.setMyMessage(dataSnapshot.child("sender_id").getValue().toString().equals(currentUserId)?true:false);

                    messageObjectList.add(messageObject);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        });
    }

    public void sendNewMessage(View view) {

        if (chatId==null || chatRef==null) {
            Toast.makeText(this, "Please waiting ....", Toast.LENGTH_SHORT).show();
            return ;
        };
        Log.d("TAG",chatId);
        Log.d("TAG",newMessage.getText().toString());
        Log.d("TAG",chatRef.getKey());

        if (newMessage.getText().toString().equals("")) return ;

        Map msg=new HashMap();
        msg.put("message",newMessage.getText().toString());
        msg.put("sender_id",currentUserId);
        chatRef.push().setValue(msg);
        newMessage.setText("");
    }

    public void goBack(View view) {
        Intent intent=new Intent(ChatActivity.this,MatchActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToViewProfile(View view) {
        Intent intent=new Intent(ChatActivity.this, ViewProfileActivity.class);
        intent.putExtra("userId",matchedUserId);
        startActivity(intent);
    }
}
