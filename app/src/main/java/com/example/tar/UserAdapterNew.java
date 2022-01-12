package com.example.tar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tar.utils.DateTimeHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UserAdapterNew extends RecyclerView.Adapter<UserAdapterNew.MyViewHolder> {
    Context context;
    ArrayList<UserSorting> list;
    String lastMsg;
    String state;
    String timeStr;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DateTimeHelper dt = new DateTimeHelper();

    public UserAdapterNew(Context context, ArrayList<UserSorting> list) {
        this.context =context;
        this.list=list;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_user_row,parent,false);
        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserSorting userSorting=list.get(position);
        auth=FirebaseAuth.getInstance();
        String userID=auth.getCurrentUser().getUid();
        /*database.getReference().child("UserChat").child(userID).child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long time=snapshot.child("lastMsgTime").getValue(long.class);
                    timeStr = dt.getTimeTodayYestFromMilli(time);
                    holder.lastTime.setText(timeStr);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        long time= userSorting.getLastMsgTime();
        String timeStr=dt.getTimeTodayYestFromMilli(time);
        String upperString = userSorting.name.substring(0, 1).toUpperCase() + userSorting.name.substring(1).toLowerCase();
        holder.name1.setText(upperString);
        holder.lastMsg1.setText(userSorting.getLastMsg());
        holder.lastTime.setText(timeStr);
        Picasso.get().load(userSorting.getProfileUri()).placeholder(R.drawable.watsapp).into(holder.imageView1);

        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context,ChatActivity.class);
            intent.putExtra("Name",userSorting.getName());
            intent.putExtra("uid",userSorting.getFriend());
            intent.putExtra("profileImg",userSorting.getProfileUri());
            //intent.putExtra("status",state);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name1,lastMsg1,lastTime;
        ImageView imageView1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name1=itemView.findViewById(R.id.Profile_Name);
            lastMsg1=itemView.findViewById(R.id.lastMsg);
            lastTime=itemView.findViewById(R.id.lastMsgTime);
            imageView1=itemView.findViewById(R.id.profile_image);

        }
    }
}
