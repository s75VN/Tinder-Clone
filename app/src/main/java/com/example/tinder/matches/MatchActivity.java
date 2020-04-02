package com.example.tinder.matches;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tinder.R;
import com.example.tinder.chat.MessageAdapter;
import com.example.tinder.home.HomeActivity;
import com.example.tinder.settings.ChooseSettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity {

    RecyclerView newMatchesRecycleView;
    NewMatchesAdapter newMatchesAdapter;
    List<NewMatchObject> newMatchObjectList;

    RecyclerView connectedMatchesRecycleView;
    ConnectedMatchesAdapter connectedMatchesAdapter;
    List<ConnectedMatchObject> connectedMatchObjectList;

    String currentUserId;
    DatabaseReference chatsRef;
    DatabaseReference matchesRef;
    DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);



        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        chatsRef= FirebaseDatabase.getInstance().getReference().child("chats");
        usersRef= FirebaseDatabase.getInstance().getReference().child("users");
        matchesRef=FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("connections").child("matches");
        newMatchObjectList=new ArrayList<NewMatchObject>();


        newMatchesAdapter=new NewMatchesAdapter(MatchActivity.this,R.layout.item_new_match,newMatchObjectList);

        newMatchesRecycleView=findViewById(R.id.newMatches);
        newMatchesRecycleView.setHasFixedSize(false);
        newMatchesRecycleView.setLayoutManager(new GridLayoutManager(this,4));
        newMatchesRecycleView.setAdapter(newMatchesAdapter);
        //---------------------//

        connectedMatchObjectList=new ArrayList<ConnectedMatchObject>();

        connectedMatchesAdapter=new ConnectedMatchesAdapter(MatchActivity.this,R.layout.item_connected_match,connectedMatchObjectList);

        connectedMatchesRecycleView=findViewById(R.id.connectedMatches);
        connectedMatchesRecycleView.setHasFixedSize(false);
        connectedMatchesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        connectedMatchesRecycleView.setAdapter(connectedMatchesAdapter);

        getMatches();
        super.onResume();


    }

    @Override
    public void onStop() {
        Log.d("TAG", "onStop: OnStop is called");
        super.onStop();
        finish();
    }
    public void goToSettings(View view) {
        Intent intent=new Intent(MatchActivity.this, ChooseSettingActivity.class);
        startActivity(intent);
    }

    public void goToHome(View view) {
        Intent intent=new Intent(MatchActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void getMatches(){
        Log.d("TAG", "getMatches: Go Into Here"+  matchesRef.getKey());

        matchesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    String chatId=dataSnapshot.child("chat_id").getValue().toString();
                    Log.d("\n \n \n TAG", "onChildAdded:chat_id "+chatId);
                    getChat(chatId,dataSnapshot.getKey());
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

    private void getChat(final String chatId, final String matchedUserId) {
        chatsRef.child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if (dataSnapshot.getChildrenCount()==0) {
                        Log.d("TAG", "onChildAdded:is new match ");
                        getNewMatch(matchedUserId);
                    }
                    else {
                        Log.d("TAG", "onChildAdded:is connected match ");
                        if (dataSnapshot.getChildrenCount()==1)
                            for (NewMatchObject newMatchObject:newMatchObjectList){
                                if (newMatchObject.getId().equals(matchedUserId)) {
                                    newMatchObjectList.remove(newMatchObject);
                                    Log.d("TAG", "onDataChange: new match name   " + newMatchObject.getName());
                                    Log.d("TAG", "onDataChange: new match id    " + newMatchObject.getId());
                                    Log.d("TAG", "onDataChange: matched user id   " + matchedUserId);
                                    newMatchesAdapter.notifyDataSetChanged();
                                }
                            }

                        for (ConnectedMatchObject connectedMatchObject:connectedMatchObjectList){
                            if (connectedMatchObject.getId().equals(matchedUserId)) connectedMatchObjectList.remove(connectedMatchObject);
                            connectedMatchesAdapter.notifyDataSetChanged();
                        }
                        getConnectedMatch(chatId,matchedUserId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    String lastestMessage;

    private void getConnectedMatch(String chatId, String matchedUserId) {
        Query query=chatsRef.child(chatId).orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot child:dataSnapshot.getChildren()){
                        lastestMessage=child.child("message").getValue().toString();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        usersRef.child(matchedUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ConnectedMatchObject connectedMatchObject=new ConnectedMatchObject();
                    connectedMatchObject.setName(dataSnapshot.child("name").getValue().toString());
                    connectedMatchObject.setAvatarUrl(dataSnapshot.child("avatar_url").getValue().toString());
                    connectedMatchObject.setId(dataSnapshot.getKey().toString());
                    connectedMatchObject.setLastestMessage(lastestMessage);

                    connectedMatchObjectList.add(connectedMatchObject);
                    connectedMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNewMatch(String matchedUserId) {
        usersRef.child(matchedUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    NewMatchObject newMatchObject=new NewMatchObject();
                    newMatchObject.setName(dataSnapshot.child("name").getValue().toString());
                    newMatchObject.setAvatarUrl(dataSnapshot.child("avatar_url").getValue().toString());
                    newMatchObject.setId(dataSnapshot.getKey().toString());

                    newMatchObjectList.add(newMatchObject);
                    newMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
