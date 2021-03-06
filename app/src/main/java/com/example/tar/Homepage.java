package com.example.tar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class Homepage extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database,database2;
    FirebaseFirestore db;
    DatabaseReference rootRef;
    ExtendedFloatingActionButton inviteBtn;
    String userID;
    ImageView logout,currentProfile;
    ArrayList<Users> usersArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        getWindow().setStatusBarColor(ContextCompat.getColor(Homepage.this,R.color.teal_700));


        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();
        database2=FirebaseDatabase.getInstance();
        rootRef=FirebaseDatabase.getInstance().getReference();

        userID= Objects.requireNonNull(auth.getCurrentUser().getUid());
        usersArrayList =new ArrayList<>();
        adapter=new UserAdapter(this,usersArrayList);

        logout=findViewById(R.id.logout);
        inviteBtn=findViewById(R.id.AddContact);
        currentProfile=findViewById(R.id.currentUser_profile);
        mainUserRecyclerView=findViewById(R.id.mainUSerRecyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mainUserRecyclerView.setHasFixedSize(true);
        adapter = new UserAdapter(Homepage.this, usersArrayList);
        mainUserRecyclerView.setLayoutManager(manager);

        //manager.setReverseLayout(true);//Working
        //manager.setStackFromEnd(true);


        //For Account Setting Image
        database.getReference().child("user").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image;
                if(snapshot.exists()){
                    image=snapshot.child("userProfile").getValue(String.class);
                    Picasso.get().load(image).placeholder(R.drawable.watsapp).into(currentProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mainUserRecyclerView.setAdapter(adapter);

        currentProfile.setOnClickListener(v -> {
            Intent intent=new Intent(Homepage.this,EditProfile.class);
            intent.putExtra("Name",auth.getCurrentUser().getDisplayName());
            intent.putExtra("uid",auth.getUid());
            intent.putExtra("profileImg",auth.getCurrentUser().getPhotoUrl());
            startActivity(intent);
        });

        inviteBtn.setOnClickListener(view -> {
            Intent intent=new Intent(Homepage.this,AllUsersNew.class);
            startActivity(intent);
        });



        if(auth.getCurrentUser()==null){
            startActivity(new Intent(Homepage.this,Login.class));
        }

        logout.setOnClickListener(view -> {
            /*FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Homepage.this, Login.class));
            finish();*/
            Intent intent=new Intent(Homepage.this,HomePage2.class);
            startActivity(intent);
        });

        database.getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
               }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}