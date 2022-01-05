package com.example.tar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tar.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    ImageView profileImage,BacKBtn;
    TextView email;
    ActivityEditProfileBinding activityEditProfileBinding;
    FirebaseAuth auth;
    ExtendedFloatingActionButton logout;
    FloatingActionButton getImage;
    DatabaseReference rootRef;
    FirebaseDatabase database;
    EditText Username;
    FirebaseStorage storage;
    private int ImageGalleryRequest=123;
    ImageButton edit;
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
        StorageReference storageReference=storage.getReference().child("userProfile");


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
                String name="";
                if(snapshot.exists()){
                    name = snapshot.child("name").getValue(String.class);
                    Username.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String userID= Objects.requireNonNull(auth.getCurrentUser().getUid());
        String mail=Objects.requireNonNull(auth.getCurrentUser().getEmail());
        //setUserData
        email.setText(""+mail);
        //Username.setText(""+name);
        Picasso.get().load(auth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.watsapp).into(profileImage);

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(EditProfile.this, Login.class));
            finish();
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName=Username.getText().toString();
                HashMap<String,Object> onLineState=new HashMap<>();
                onLineState.put("name",newName);

                rootRef.child("user").child(auth.getCurrentUser().getUid())
                        .updateChildren(onLineState);
                Intent intent=new Intent(EditProfile.this,Homepage.class);
                startActivity(intent);
                /*if(imageUri!=null){
                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(@NonNull Uri uri) {
                                    String finalImageUri=uri.toString();
                                    Users users = new Users(mail,newName, auth.getUid(),finalImageUri);
                                    rootRef.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent=new Intent(EditProfile.this,Homepage.class);
                                            startActivity(intent);
                                        }
                                    });

                                }
                            });
                        }
                    });
                }*/



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                 imageUri=data.getData();
                String toString=imageUri.toString();
                profileImage.setImageURI(imageUri);
                HashMap<String,Object> onLineState=new HashMap<>();
                onLineState.put("userProfile",toString);

                rootRef.child("users").child(auth.getCurrentUser().getUid()).child("userState")
                        .updateChildren(onLineState);

            }
        }
    }
}