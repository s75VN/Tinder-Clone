package com.example.tinder.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tinder.R;
import com.example.tinder.entry.EntryActivity;
import com.example.tinder.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileSettingActivity extends AppCompatActivity {
    public static int PICK_IMAGE_REQUEST_CODE=1;
    private CircularImageView avatar;
    private EditText mName,mPhone,mAbout,mJob,mAge;
    private Button male,female;
    private DatabaseReference currentUserRef;
    private String currentUserId;
    Bitmap selectedImage=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        avatar=findViewById(R.id.avatar);
        mName=findViewById(R.id.name);
        mAbout=findViewById(R.id.about);
        mPhone=findViewById(R.id.phone);
        mAge=findViewById(R.id.age);
        mJob=findViewById(R.id.job);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);

        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserRef= FirebaseDatabase.getInstance().getReference()
                            .child("users").child(currentUserId);

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mName.setText(dataSnapshot.child("name").getValue().toString());
                    mAbout.setText(dataSnapshot.child("about").getValue().toString());
                    mAge.setText(dataSnapshot.child("age").getValue().toString());
                    mPhone.setText(dataSnapshot.child("phone").getValue().toString());
                    mJob.setText(dataSnapshot.child("job").getValue().toString());


                    showGender(dataSnapshot.child("gender").getValue().toString());

                    String avatarUrl;

                    Log.d("TAG"," Image URL "+ dataSnapshot.child("avatar_url").getValue().toString());
                    if ((avatarUrl=dataSnapshot.child("avatar_url").getValue().toString()).equals("default")){
                        Log.d("TAG"," use default image");
                        Glide.with(ProfileSettingActivity.this).load(R.drawable.avatar).into(avatar);
                    }
                    else {
                        Glide.with(ProfileSettingActivity.this).load(avatarUrl).into(avatar);
                    };
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showGender(String gender) {
        male.getCompoundDrawables()[3].setLevel(gender.equals("male")?0:1);
        female.getCompoundDrawables()[3].setLevel(gender.equals("female")?0:1);
    }


    public void goBack(View view) {
        finish();
    }

    public void updateAvatar(View view) {
        Log.d("TAG", "updateAvatar: ");
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri=data.getData();
            if (uri!=null) {
                try {
                    Log.d("TAG", "onActivityResult: URI"+uri.toString());
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    selectedImage = BitmapFactory.decodeStream(inputStream);
                    if (selectedImage!=null){
                        Log.d("TAG", "onActivityResult: Exists Image !!!");
                    }
                    avatar.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateProfile(View view) {
        final Map updatedInfo=new HashMap();
        updatedInfo.put("name",mName.getText().toString());
        updatedInfo.put("about",mAbout.getText().toString());
        updatedInfo.put("phone",mPhone.getText().toString());
        updatedInfo.put("age",mAge.getText().toString());
        updatedInfo.put("job",mJob.getText().toString());
        if (male.getCompoundDrawables()[3].getLevel()==0){
            updatedInfo.put("gender","male");
        }
        else{
            updatedInfo.put("gender","female");
        };
        currentUserRef.updateChildren(updatedInfo);

        if (selectedImage!=null){
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] data=baos.toByteArray();
            final StorageReference avatarRef= FirebaseStorage.getInstance().getReference().child("avatars").child(currentUserId);

            avatarRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            currentUserRef.child("avatar_url").setValue(uri.toString());
                            Log.d("TAG", "onSuccess: Image Url"+uri.toString());

                        }
                    });
                }
            });

        };

        Intent intent=new Intent(ProfileSettingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    public void chooseGender(View view) {
        showGender(((Button)view).getText().toString());
    }
}
