package com.example.tar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tar.databinding.ItemReceiveBinding;
import com.example.tar.databinding.ItemSentBinding;
import com.example.tar.utils.DateTimeHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagesAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    String senderRoom;
    String receiverRoom;
    DateTimeHelper dt = new DateTimeHelper();



    public MessagesAdapter(Context context, ArrayList<Message> messages) {

        this.context = context;
        this.messages = messages;
        //this.senderRoom = senderRoom;
        //this.receiverRoom = receiverRoom;
    }
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        if(viewType==ITEM_SENT){
            View view=LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false);
            return  new SentViewHolder(view);
        }else{
            View view=LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return  new ReceiverViewHolder(view);
        }
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(holder.getClass()==SentViewHolder.class){
            SentViewHolder viewHolder=(SentViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
            String time = dt.getTimestampToTime(message.getTimestamp());
            viewHolder.binding.LastMsgStamp.setText(time);
        }else {
            ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
            String time = dt.getTimestampToTime(message.getTimestamp());
            viewHolder.binding.lastMsgTime .setText(time);
        }
    }

    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }
    public int getItemCount() {
        return messages.size();
    }


    public class SentViewHolder extends RecyclerView.ViewHolder {
        ItemSentBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);



        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ItemReceiveBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);

        }
    }

}
