package com.example.tar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tar.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    TextView user_name,status;
    ImageView backBtn,profileImg;
    MessagesAdapter adapter;
    ArrayList<Message> messages;
    RecyclerView recyclerView;
    DatabaseReference rootRef;
    FirebaseAuth auth;
    EditText messageBox;
    String CurrentUserTime,CurrentDate;

    String senderRoom, receiverRoom,userID;

    FirebaseDatabase database;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        getWindow().setStatusBarColor(ContextCompat.getColor(ChatActivity.this, R.color.teal_700));
        userID = Objects.requireNonNull(auth.getCurrentUser().getUid());


        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages);
        messageBox=findViewById(R.id.messageBox);
        messageBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateUserStatus("Typing");
                return false;
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        status = findViewById(R.id.status);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);


        adapter = new MessagesAdapter(ChatActivity.this, messages);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        user_name = findViewById(R.id.user_name);
        backBtn = findViewById(R.id.backBtn);
        profileImg = findViewById(R.id.profile);

        String name = getIntent().getStringExtra("Name");
        String receiverUid = getIntent().getStringExtra("uid");
        String profileUri = getIntent().getStringExtra("profileImg");
        String state=getIntent().getStringExtra("status");


        String senderUid = FirebaseAuth.getInstance().getUid();
        String newName=name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
        user_name.setText("" + newName);

        Picasso.get().load(profileUri).placeholder(R.drawable.avatar).into(binding.profile);


////onlineOffline

        senderRoom=senderUid+receiverUid;
        receiverRoom=receiverUid+senderUid;
        database =FirebaseDatabase.getInstance();

        DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatRef=database.getReference().child("chats").child(senderRoom).child("messages");


        database.getReference().child("users")
                .child(receiverUid)
                .child("userState")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String state="";
                        String lastSeen="";
                        if(snapshot.exists()){
                            state = snapshot.child("State").getValue(String.class);
                            switch (state) {
                                case "Offline":
                                    lastSeen=snapshot.child("Time").getValue(String.class);
                                    Calendar calendar=Calendar.getInstance();
                                    SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
                                    CurrentUserTime=currentTime.format(calendar.getTime());
                                    if(CurrentUserTime==lastSeen){
                                        status.setText(state);
                                    }else{
                                        status.setText("Last Seen: "+lastSeen);
                                    }
                                    break;
                                case "Online":
                                    status.setText(state);
                                default:
                                    status.setText(state);
                                    break;
                            }
                            //status.setText(state);
                        }
                        else{
                            lastSeen=snapshot.child("Time").getValue(String.class);
                            status.setText(lastSeen);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                            Message message=dataSnapshot1.getValue(Message.class);
                            messages.add(message);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.sendBtn.setOnClickListener(view -> {
            String messageTxt=binding.messageBox.getText().toString();
            if(messageTxt.isEmpty()){
                    updateUserStatus("Online");

            }else {
                Date date = new Date();

                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
                CurrentUserTime=currentTime.format(calendar.getTime());
                //status.setText(""+CurrentUserTime);
                Message message = new Message(messageTxt, senderUid, date.getTime());
                binding.messageBox.setText("");

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());
                lastMsgObj.put("key",CurrentUserTime);
                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            //sending time to sort user data on home page

                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .push()
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                HashMap<String,Object> onLineState=new HashMap<>();
                                //onLineState.put("lastMsgTime",message.getMessage());
                                onLineState.put("lastTimeMsg",CurrentUserTime);
                                rootRef.child("user").child(auth.getCurrentUser().getUid())
                                        .updateChildren(onLineState);

                                updateUserStatus("Online");
                            }

                        });


                    }
                });
            }
        });

        //getSupportActionBar().setTitle(name);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this,Homepage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
            updateUserStatus("Online");

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(userID!=null){
            updateUserStatus("Offline");
        }
    }

    private void updateUserStatus(String state){
        String CurrentUserTime,CurrentDate;
        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        CurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        CurrentUserTime=currentTime.format(calendar.getTime());

        HashMap<String,Object> onLineState=new HashMap<>();
        onLineState.put("Time",CurrentUserTime);
        onLineState.put("Date",CurrentDate);
        onLineState.put("State",state);

        rootRef.child("users").child(auth.getCurrentUser().getUid()).child("userState")
                .updateChildren(onLineState);

    }
}