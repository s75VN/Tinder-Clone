package com.example.tinder.chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinder.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private int resource ;
    private List<MessageObject> messageList;
    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<MessageObject> objects) {
        this.context=context;
        this.resource=resource;
        this.messageList=objects;
    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView message;
        public LinearLayout line;
        public LinearLayout messageContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message);
            line=itemView.findViewById(R.id.line);
            messageContainer=itemView.findViewById(R.id.messageContainer);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resource,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemCount()==0) return ;
        MessageObject obj=messageList.get(position);
        holder.message.setText(obj.getMessage());
        if (obj.isMyMessage()){
            holder.line.setGravity(Gravity.END);
            holder.messageContainer.setBackgroundResource(R.drawable.my_message);
            holder.message.setTextColor(Color.parseColor("#a3f5fe"));
        }
        else {
            holder.line.setGravity(Gravity.START);
            holder.messageContainer.setBackgroundResource(R.drawable.your_message);
            holder.message.setTextColor(Color.parseColor("#7a7a7a"));
        }

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
