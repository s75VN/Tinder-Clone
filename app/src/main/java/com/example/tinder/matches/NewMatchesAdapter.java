package com.example.tinder.matches;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinder.R;
import com.example.tinder.chat.ChatActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.ViewHolder> {

    private Context context;
    private int resource ;
    private List<NewMatchObject> newMatchObjectList;
    public NewMatchesAdapter(@NonNull Context context, int resource, @NonNull List<NewMatchObject> objects) {
        this.context=context;
        this.resource=resource;
        this.newMatchObjectList=objects;
    };


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public CircularImageView avatar;
        public TextView matchedUserId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            avatar=itemView.findViewById(R.id.avatar);
            matchedUserId=itemView.findViewById(R.id.matchedUserId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(v.getContext(), ChatActivity.class);
            intent.putExtra("matchedUserId",matchedUserId.getText().toString());
            v.getContext().startActivity(intent);
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
        NewMatchObject obj=newMatchObjectList.get(position);
        holder.name.setText(obj.getName());
        holder.matchedUserId.setText(obj.getId());
        if (obj.getAvatarUrl().equals("default")){
            Glide.with(context).load(R.drawable.avatar).into(holder.avatar);
        }
        else {
            Glide.with(context).load(obj.getAvatarUrl()).into(holder.avatar);
        }

    }


    @Override
    public int getItemCount() {
        return newMatchObjectList.size();
    }
}
