package com.example.tinder.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tinder.R;
import com.example.tinder.home.HomeActivity;
import com.example.tinder.matches.MatchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ChooseSettingActivity extends AppCompatActivity {

    CircularImageView currentUserAvatar;
    TextView currentUserName;
    String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_setting);

        currentUserName=findViewById(R.id.currentUserName);
        currentUserAvatar=findViewById(R.id.currentUserAvatar);
        currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName.setText(dataSnapshot.child("name").getValue().toString());
                    if (dataSnapshot.child("avatar_url").getValue().toString().equals("default")){
                        Glide.with(ChooseSettingActivity.this).load(R.drawable.avatar).into(currentUserAvatar);
                    }
                    else{
                        Glide.with(ChooseSettingActivity.this).load(dataSnapshot.child("avatar_url").getValue().toString()).into(currentUserAvatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void goToProfileSetting(View view) {
        Intent intent=new Intent(ChooseSettingActivity.this, ProfileSettingActivity.class);
        startActivity(intent);
    }

    public void goToFilterSetting(View view) {
        Intent intent=new Intent(ChooseSettingActivity.this, FilterSettingActivity.class);
        startActivity(intent);
    }

    public void goToHome(View view) {
        Intent intent=new Intent(ChooseSettingActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void goToMatches(View view) {
        Intent intent=new Intent(ChooseSettingActivity.this, MatchActivity.class);
        startActivity(intent);
    }
}
