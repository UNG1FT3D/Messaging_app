package com.example.tar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tar.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    ImageView profileImage,BacKBtn;
    TextView email;
    ActivityEditProfileBinding activityEditProfileBinding;
    FirebaseAuth auth;
    ExtendedFloatingActionButton logout;
    DatabaseReference rootRef;
    FirebaseDatabase database;
    EditText Username;
    FirebaseStorage storage;
    final int ImageGalleryRequest=123;
    ImageButton edit,getImage;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditProfileBinding= ActivityEditProfileBinding.inflate(getLayoutInflater());
        getWindow().setStatusBarColor(ContextCompat.getColor(EditProfile.this, R.color.teal_700));
        setContentView(activityEditProfileBinding.getRoot());
        rootRef = FirebaseDatabase.getInstance().getReference();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();



        profileImage=findViewById(R.id.profile_image);
        Username=findViewById(R.id.user_name);
        email=findViewById(R.id.Email);
        BacKBtn=findViewById(R.id.BackBtn);
        logout=findViewById(R.id.LOGOUT);
        getImage=findViewById(R.id.getImage);
        edit=findViewById(R.id.saveUsername);

        BacKBtn.setOnClickListener(v -> {
            Intent intent=new Intent(EditProfile.this,Homepage.class);
            startActivity(intent);
        });

        getImage.setOnClickListener(v -> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select Picture"),10);
        });

        auth=FirebaseAuth.getInstance();

        database.getReference().child("user").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name;
                String image;
                if(snapshot.exists()){
                    name = snapshot.child("name").getValue(String.class);
                    image=snapshot.child("userProfile").getValue(String.class);
                    Username.setText(name);
                    Picasso.get().load(image).placeholder(R.drawable.watsapp).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //setUserData
        String mail=Objects.requireNonNull(auth.getCurrentUser().getEmail());
        email.setText(""+mail);
        String uid=auth.getUid();

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(EditProfile.this, Login.class));
            finish();
        });
        StorageReference storageReference=storage.getReference().child("userProfile").child(uid);
        edit.setOnClickListener(v -> {
            String newName=Username.getText().toString();
            HashMap<String,Object> onLineState=new HashMap<>();
            onLineState.put("name",newName);

            rootRef.child("user").child(auth.getCurrentUser().getUid())
                    .updateChildren(onLineState);
            Intent intent=new Intent(EditProfile.this,Homepage.class);
            startActivity(intent);
            if(imageUri!=null){
                storageReference.putFile(imageUri).addOnCompleteListener(task -> storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri uri) {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            String finalImageUri= uri1.toString();
                            HashMap<String,Object> onLineState1 =new HashMap<>();
                            onLineState1.put("userProfile",finalImageUri);
                            rootRef.child("user").child(auth.getCurrentUser().getUid())
                                    .updateChildren(onLineState1);

                        });

                    }
                }));
            }



        });


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageUri=data.getData();
                String toString=imageUri.toString();
                profileImage.setImageURI(imageUri);
            }
        }
    }
}