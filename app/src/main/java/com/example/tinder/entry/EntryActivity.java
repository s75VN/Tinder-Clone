package com.example.tinder.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class EntryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

    }

    public void goToSignIn(View view) {
        Intent intent=new Intent(EntryActivity.this,SignInActivity.class);
        startActivity(intent);
    }

    public void goToRegister(View view) {
        Intent intent=new Intent(EntryActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}
