package com.example.tinder.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tinder.R;
import com.example.tinder.ViewProfileActivity;
import com.example.tinder.entry.EntryActivity;
import com.example.tinder.entry.RegisterActivity;
import com.example.tinder.matches.MatchActivity;
import com.example.tinder.settings.ChooseSettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.FlingCardListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;



public class HomeActivity extends Activity {

    private ArrayList<CardObject> cardList;
    private CardAdapter cardAdapter;
    private int i;

    private SwipeFlingAdapterView flingContainer;

    private DatabaseReference usersRef;
    private String currentUserId;
    private String genderSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usersRef=FirebaseDatabase.getInstance().getReference().child("users");
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        cardList = new ArrayList<>();

        cardAdapter = new CardAdapter(HomeActivity.this, R.layout.item_card, cardList );
        flingContainer=findViewById(R.id.swipeFling);


        flingContainer.setAdapter(cardAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                cardList.remove(0);
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                String potentialUserId=((CardObject)o).getUserId();
                usersRef.child(potentialUserId).child("connections").child("denies").child(currentUserId).setValue(true);
            }

            @Override
            public void onRightCardExit(Object o) {
                String potentialUserId=((CardObject)o).getUserId();
                usersRef.child(potentialUserId).child("connections").child("accepts").child(currentUserId).setValue(true);
                listenMatchFromPotentialUser(potentialUserId);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Intent intent=new Intent(HomeActivity.this, ViewProfileActivity.class);
                intent.putExtra("userId",((CardObject)o).getUserId());
                startActivity(intent);
            }
        });

        getCurrentUserGender();
    }

    private void listenMatchFromPotentialUser(final String potentialUserId) {
        Log.d("TAG", "listenMatchFromPotentialUser: ");
        usersRef.child(currentUserId).child("connections").child("accepts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()
                && dataSnapshot.getKey().toString().equals(potentialUserId)){
                    Log.d("TAG", "onChildAdded: Id Of Accept Me "+dataSnapshot.getKey().toString());

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("chats").push();
                    ref.setValue(true);
                    String chatId=ref.getKey();
                    usersRef.child(potentialUserId).child("connections").child("matches").child(currentUserId).child("chat_id").setValue(chatId);
                    usersRef.child(currentUserId).child("connections").child("matches").child(potentialUserId).child("chat_id").setValue(chatId);
                    Toast.makeText(HomeActivity.this, "New Match !!!", Toast.LENGTH_SHORT).show();
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

    private void getCurrentUserGender() {
        usersRef.child(currentUserId).child("gender_search").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    genderSearch=dataSnapshot.getValue().toString();
                    getPotentialUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPotentialUsers() {
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()
                && !dataSnapshot.child("connections").child("denies").hasChild(currentUserId)
                && !dataSnapshot.child("connections").child("accepts").hasChild(currentUserId)){
                    String potentialUserGender=dataSnapshot.child("gender").getValue().toString();
                    if ( (genderSearch.equals("both") || potentialUserGender.equals(genderSearch))
                    && !(dataSnapshot.getKey().equals(currentUserId))){
                        CardObject card=new CardObject();
                        card.setName(dataSnapshot.child("name").getValue().toString());
                        card.setAge(dataSnapshot.child("age").getValue().toString());
                        card.setAvatarUrl(dataSnapshot.child("avatar_url").getValue().toString());
                        card.setUserId(dataSnapshot.getKey());
                        cardList.add(card);

                        cardAdapter.notifyDataSetChanged();
                    }

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


    public void goToSettings(View view) {
        Intent intent=new Intent(HomeActivity.this, ChooseSettingActivity.class);
        startActivity(intent);
    }

    public void goToMatches(View view) {
        Intent intent=new Intent(HomeActivity.this, MatchActivity.class);
        startActivity(intent);
    }

}