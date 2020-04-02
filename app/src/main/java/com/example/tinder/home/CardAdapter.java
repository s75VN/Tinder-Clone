package com.example.tinder.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tinder.R;
import com.example.tinder.ViewProfileActivity;

import java.util.List;

public class CardAdapter extends ArrayAdapter<CardObject> {

    private Context context;
    private int resource ;
    private List<CardObject> cardList;
    public CardAdapter(@NonNull Context context, int resource, @NonNull List<CardObject> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.cardList=objects;
    }

    public class ViewHolder  {
        public TextView name;
        public TextView age;
        public ImageView avatar;

    };
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (getCount()==0) return null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(resource,parent,false);

            ViewHolder holder=new ViewHolder();
            holder.name=convertView.findViewById(R.id.name);
            holder.age=convertView.findViewById(R.id.age);
            holder.avatar=convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        };

        ViewHolder holder= (ViewHolder) convertView.getTag();
        CardObject card=cardList.get(position);
        holder.name.setText(card.getName());
        if (card.getAge().equals("No data")){
            holder.age.setText("");
        }
        else {
            holder.age.setText(card.getAge());
        }
        if (card.getAvatarUrl().equals("default")){
            Glide.with(context).load(R.drawable.avatar).into(holder.avatar);
        }
        else {
            Glide.with(context).load(card.getAvatarUrl()).into(holder.avatar);
        };

        return convertView;
    };

    @Override
    public int getCount() {
        return cardList.size();
    }
}
