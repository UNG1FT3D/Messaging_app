package com.example.tar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    CircleImageView profile_image;
    TextInputLayout reg_name, reg_password, reg_email, reg_phone;
    Button registerBtn,txt_login;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String personName,personEmail;
    FirebaseFirestore db;
    String userID;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        db= FirebaseFirestore.getInstance();
        registerBtn = findViewById(R.id.registerBtn);
        txt_login = findViewById(R.id.txt_login);
        profile_image = findViewById(R.id.profile_image);
        storage=FirebaseStorage.getInstance();


        //txt_login.setOnClickListener(view -> startActivity(new Intent(Register.this, Login.class)));
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            String personId = acct.getId();
            //userID = auth.getCurrentUser().getUid();
            String photoId=personPhoto.toString();

            StorageReference storageReference=storage.getReference().child("images").child(auth.getUid());

            /*DocumentReference documentReference = db.collection("user").document(personId);
            Map<String, Object> user = new HashMap<>();
            user.put("email", personEmail);
            user.put("Name", personName);
            user.put("uid", auth.getUid());
            user.put("photoUri",photoId);
            storageReference.putFile(personPhoto)
                    .addOnCompleteListener(task -> storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    personIdPhoto=uri.toString();
                }
            }));
            documentReference.set(user).addOnSuccessListener(unused -> Log.d("User created", auth.getUid()));*/
            userID = auth.getCurrentUser().getUid();
            DatabaseReference reference = database.getReference().child("user").child(Objects.requireNonNull(auth.getUid()));
            String status = "hey am new here";
            Users users = new Users(personEmail, personName, auth.getUid(),photoId);
            reference.setValue(users).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Intent intent=new Intent(Register.this,Homepage.class);
                    startActivity(intent);
                }
            });
        }

        reg_name = findViewById(R.id.reg_name);
        reg_password = findViewById(R.id.reg_password);
        reg_email = findViewById(R.id.reg_email);
        reg_phone = findViewById(R.id.reg_phone);
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Homepage.class));
            finish();
        }

        registerBtn.setOnClickListener(view -> {
            String email = reg_email.getEditText().getText().toString();
            String password = reg_password.getEditText().getText().toString();
            String phone = reg_phone.getEditText().getText().toString();
            String name = reg_name.getEditText().getText().toString();
        });


    }


}