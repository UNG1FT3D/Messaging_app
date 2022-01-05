package com.example.tar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.Query.Direction;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserAdapter2 extends RecyclerView.Adapter<UserAdapter2.MyViewHolder> {
    Context context;
    ArrayList<Users> usersArrayList;

    public UserAdapter2(Context context, ArrayList<Users> usersArrayList) {
        this.context =context;
        this.usersArrayList=usersArrayList;
    }

    void updateData(ArrayList<Users> usersArrayList){
        this.usersArrayList  = usersArrayList;
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

        /*ValueEventListener valueEventListener = FirebaseDatabase.getInstance().getReference()
                .child("chat")
          //    .orderByChild("name",Direction.ASCENDING)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                        } else if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())) {
                            holder.itemView.setVisibility(View.GONE);
                            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                            params.height = 0;
                            holder.itemView.setLayoutParams(params);
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
*/

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())){
                holder.itemView.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.height = 0;
                holder.itemView.setLayoutParams(params);

                }



                holder.name.setText(users.Name);
                //Picasso.get().load(users.getUserProfile()).placeholder(R.drawable.watsapp).into(holder.profileImg);
                holder.itemView.setOnClickListener(view -> {
                    Intent intent=new Intent(context,ChatActivity.class);
                    intent.putExtra("Name",users.getName());
                    intent.putExtra("uid",users.getUid());

                    context.startActivity(intent);
                     });


    }

    @Override
    public int getItemCount() {

        return usersArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,phone,time;
        ImageView profileImg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.Profile_Name);
            phone=itemView.findViewById(R.id.lastMsg);
            time=itemView.findViewById(R.id.lastMsgTime);
            profileImg=itemView.findViewById(R.id.profile_image);
        }
    }
}
