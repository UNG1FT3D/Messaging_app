package com.example.tar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class HomePage2 extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    DatabaseReference rootRef;
    UserAdapterNew  adapter;
    ArrayList<UserSorting> usersArrayList;
    FirebaseDatabase database,database2;
    FirebaseFirestore db;
    ExtendedFloatingActionButton inviteBtn;
    String userID;
    String image;
    ImageView logout,currentProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);
        getWindow().setStatusBarColor(ContextCompat.getColor(HomePage2.this, R.color.teal_700));
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();
        database2=FirebaseDatabase.getInstance();
        rootRef=FirebaseDatabase.getInstance().getReference();
        inviteBtn=findViewById(R.id.AddContact);
        currentProfile=findViewById(R.id.currentUser_profile);

        userID= Objects.requireNonNull(auth.getCurrentUser().getUid());

        mainUserRecyclerView=findViewById(R.id.mainUSerRecyclerview);
        mainUserRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mainUserRecyclerView.setLayoutManager(manager);
        usersArrayList=new ArrayList<>();
        adapter=new UserAdapterNew(this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);


        inviteBtn.setOnClickListener(view -> {
            Intent intent=new Intent(HomePage2.this,AllUsersNew.class);
            startActivity(intent);
        });
        currentProfile.setOnClickListener(v -> {
            Intent intent=new Intent(HomePage2.this,EditProfile.class);
            intent.putExtra("Name",auth.getCurrentUser().getDisplayName());
            intent.putExtra("uid",auth.getUid());
            intent.putExtra("profileImg",auth.getCurrentUser().getPhotoUrl());
            startActivity(intent);
        });


        rootRef.child("UserChat").child(userID).child("Friends").orderByChild("lastMsgTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UserSorting userSorting=dataSnapshot.getValue(UserSorting.class);
                    usersArrayList.add(userSorting);
                    Log.d("SORT", "onDataChange: "+userSorting.getLastMsgTime());

                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logout=findViewById(R.id.logout);
        inviteBtn=findViewById(R.id.AddContact);
        currentProfile=findViewById(R.id.currentUser_profile);

        if(auth.getCurrentUser()==null){
            startActivity(new Intent(HomePage2.this,Login.class));
        }

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomePage2.this, Login.class));
            finish();
            /*Intent intent=new Intent(HomePage2.this,HomePage2.class);
            startActivity(intent);*/
        });

        database.getReference().child("user").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    image=snapshot.child("userProfile").getValue(String.class);
                    Picasso.get().load(image).placeholder(R.drawable.watsapp).into(currentProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}