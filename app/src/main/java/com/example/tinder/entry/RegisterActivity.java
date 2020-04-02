package com.example.tinder.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinder.R;
import com.example.tinder.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private int level=-1;
    private Button male;
    private Button female;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);

        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mName=findViewById(R.id.name);

        male.getCompoundDrawables()[3].setLevel(1);
        female.getCompoundDrawables()[3].setLevel(1);

        mAuth=FirebaseAuth.getInstance();
        usersRef= FirebaseDatabase.getInstance().getReference().child("users");

    }



    public void chooseGender(View view) {
        String gender=((Button)view).getText().toString();
        level=gender.equals("male")?0:1;
        male.getCompoundDrawables()[3].setLevel(level);
        female.getCompoundDrawables()[3].setLevel(1-level);
    }


    public void register(View view){
        final String name;
        final String password;
        final String email;

        Log.d("TAG", "register: name  = "+mName.getText().toString());
        Log.d("TAG", "register: password  = "+mPassword.getText().toString());
        Log.d("TAG", "register: email  = "+mEmail.getText().toString());
        Log.d("TAG", "register: level  = "+level);
        if (!((name=mName.getText().toString()).equals(""))
        && !((password=mPassword.getText().toString()).equals(""))
        && !((email=mEmail.getText().toString()).equals(""))
        && (level!=-1)){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Map userInfo=new HashMap();
                        userInfo.put("name",name);
                        userInfo.put("password",password);
                        userInfo.put("email",email);
                        userInfo.put("avatar_url","default");
                        userInfo.put("about","No Data");
                        userInfo.put("phone","No Data");
                        userInfo.put("age","No data");
                        userInfo.put("job","No Data");
                        if (level==0) {
                            userInfo.put("gender","male");
                            userInfo.put("gender_search","female");

                        }
                        else {
                            userInfo.put("gender","female");
                            userInfo.put("gender_search","male");
                        };

                        String currentUserId=mAuth.getCurrentUser().getUid();
                        Log.d("TAG", "onComplete: UserId"+currentUserId);
                        usersRef.child(currentUserId).updateChildren(userInfo);

                        Intent intent=new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                    }
                };
            });
        }
        else {

            Toast.makeText(this, "Please Fill Full The Information !!!", Toast.LENGTH_SHORT).show();
        }
    }
}
