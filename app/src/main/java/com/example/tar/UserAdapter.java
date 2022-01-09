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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    ArrayList<Users> usersArrayList;
    String lastMsg;
    String state;
    String timestr;
    FirebaseDatabase database;
    DateTimeHelper dt = new DateTimeHelper();

    public UserAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context =context;
        this.usersArrayList=usersArrayList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_user_row,parent,false);
        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Users users=usersArrayList.get(position);
        String senderId=FirebaseAuth.getInstance().getUid();
        String senderRoom=senderId+users.getUid();
        database=FirebaseDatabase.getInstance();



        database.getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //lastMsg=snapshot.child("lastMsg").getValue(String.class);
                        if(snapshot.exists()){
                            lastMsg=snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            timestr = dt.getTimeTodayYestFromMilli(time);
                            holder.time.setText(timestr);
                            holder.phone.setText(lastMsg);
                            Picasso.get().load(users.getUserProfile()).placeholder(R.drawable.watsapp).into(holder.imageView);

                        }else
                            {
                                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                params.height = 0;
                                holder.itemView.setLayoutParams(params);
                                holder.itemView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())){
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            holder.itemView.setLayoutParams(params);
            holder.itemView.setVisibility(View.GONE);
        }

        /*FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(users.getUid())
                .child("userState")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            state = snapshot.child("State").getValue(String.class);
                        }else{
                            state="active Now";
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

        String upperString = users.Name.substring(0, 1).toUpperCase() + users.Name.substring(1).toLowerCase();

        holder.name.setText(upperString);
            holder.itemView.setOnClickListener(view -> {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("Name",users.getName());
                intent.putExtra("uid",users.getUid());
                intent.putExtra("profileImg",users.getUserProfile());
                intent.putExtra("CurrentUri",state);
                context.startActivity(intent);
            });

    }

    @Override
    public int getItemCount() {

        return usersArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,phone,time;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.Profile_Name);
            phone=itemView.findViewById(R.id.lastMsg);
            time=itemView.findViewById(R.id.lastMsgTime);
            imageView=itemView.findViewById(R.id.profile_image);

        }
    }
}
