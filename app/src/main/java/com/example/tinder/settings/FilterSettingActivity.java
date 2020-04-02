package com.example.tinder.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tinder.R;
import com.example.tinder.entry.EntryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.Selector;

public class FilterSettingActivity extends AppCompatActivity {

    private Button male;
    private Button female;
    private Button both;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_setting);

        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        both=findViewById(R.id.both);

        ref=FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("gender_search");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.d("TAG", "onDataChange: "+dataSnapshot.getValue().toString());
                    showGenderSearch(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showGenderSearch(String gender) {
        male.getCompoundDrawables()[3].setLevel(gender.equals("male")?0:1);
        female.getCompoundDrawables()[3].setLevel(gender.equals("female")?0:1);
        both.getCompoundDrawables()[3].setLevel(gender.equals("both")?0:1);
    }


    public void goToEntry(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(FilterSettingActivity.this, EntryActivity.class);
        startActivity(intent);
        finish();
    }

    public void goBack(View view) {
        finish();
    }

    public void chooseGender(View view) {
        String gender=((Button)view).getText().toString();
        showGenderSearch(gender);
        ref.setValue(gender);
    }


}
