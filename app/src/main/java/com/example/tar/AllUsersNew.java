package com.example.tar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AllUsersNew extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter2 adapter;
    FirebaseDatabase database;
    FirebaseFirestore db;
    ExtendedFloatingActionButton inviteBtn;
    String userID;
    ImageView BackButton,userProfileImg;
    ArrayList<Users> usersArrayList;
    EditText etSearch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_new);
        getWindow().setStatusBarColor(ContextCompat.getColor(AllUsersNew.this,R.color.teal_700));

        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();

        userID= Objects.requireNonNull(auth.getCurrentUser().getUid());
        usersArrayList =new ArrayList<>();
        adapter=new UserAdapter2(this,usersArrayList);
        etSearch  = findViewById(R.id.et_search);

        BackButton=findViewById(R.id.backButton);
        inviteBtn=findViewById(R.id.addbtn);

        userProfileImg=findViewById(R.id.profile_image);

        //search=findViewById(R.id.SearchBar);

        mainUserRecyclerView=findViewById(R.id.mainUSerRecyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mainUserRecyclerView.setHasFixedSize(true);
        adapter = new UserAdapter2(AllUsersNew.this, usersArrayList);
        mainUserRecyclerView.setLayoutManager(manager);
        mainUserRecyclerView.setAdapter(adapter);


        inviteBtn.setOnClickListener(view -> {
            Intent intent=new Intent(AllUsersNew.this,InvitePage.class);
            startActivity(intent);
        });
        BackButton.setOnClickListener(v -> {
            Intent intent=new Intent(AllUsersNew.this,Homepage.class);
            startActivity(intent);
        });


        if(auth.getCurrentUser()==null){
            startActivity(new Intent(AllUsersNew.this,Login.class));
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str  = etSearch.getText().toString();
                List<Users> filterUsers = usersArrayList.stream()
                        .filter(user -> user.Name.contains(str.toString()))
                        .collect(Collectors.toList());

                adapter.updateData(new ArrayList<>(filterUsers));
                filterUsers.clear();
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("HomeTAG", "beforeTextChanged: beforetextchange}");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*db.collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(error!= null){
                        Log.d("Error",error.getMessage());
                    }
                    for(DocumentChange documentChange:value.getDocumentChanges()){
                        if(documentChange.getType()==DocumentChange.Type.ADDED){
                            usersArrayList.add(documentChange.getDocument().toObject(Users.class));
                        }
                        adapter.notifyDataSetChanged();
                    }
            }
        });*/
        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AllUsers.this, Login.class));
                finish();
            }
        });*/



        DatabaseReference reference=database.getReference().child("user");

        reference.orderByChild("name").addValueEventListener(new ValueEventListener() {
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