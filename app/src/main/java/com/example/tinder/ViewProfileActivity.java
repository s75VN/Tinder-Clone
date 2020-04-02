package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView mName,mAge,mJob,mAbout;
    private ImageView avatar;
    private String potentialUserId;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        mName=findViewById(R.id.name);
        mAge=findViewById(R.id.age);
        mJob=findViewById(R.id.job);
        mAbout=findViewById(R.id.about);
        avatar=findViewById(R.id.avatar);

        potentialUserId=getIntent().getStringExtra("userId");
        ref= FirebaseDatabase.getInstance().getReference().child("users").child(potentialUserId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mName.setText(dataSnapshot.child("name").getValue().toString());
                    if (dataSnapshot.child("age").getValue().toString().equals("No data")){
                        mAge.setText("");
                    }
                    else {
                        mAge.setText(dataSnapshot.child("age").getValue().toString());
                    };

                    mJob.setText(dataSnapshot.child("job").getValue().toString());
                    mAbout.setText(dataSnapshot.child("about").getValue().toString());

                    String avatarUrl=dataSnapshot.child("avatar_url").getValue().toString();
                    if (avatarUrl.equals("default")){
                        Glide.with(ViewProfileActivity.this).load(R.drawable.avatar).into(avatar);
                    }
                    else {
                        Glide.with(ViewProfileActivity.this).load(avatarUrl).into(avatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void goBack(View view) {
        finish();
    }
}
