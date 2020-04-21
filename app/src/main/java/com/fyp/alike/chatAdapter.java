package com.fyp.alike;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.LinkedList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.CourseViewHolder> {

    private static LinkedList<Chat> showList;
    private static String myName;
    chatAdapter(LinkedList<Chat> showList,String currUserName) {
        myName = currUserName;
        chatAdapter.showList = showList;
    }

    @NonNull
    @Override
    public chatAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_list_item_layout,parent,false);
        return new CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final chatAdapter.CourseViewHolder holder, final int position) {
        new UserViewModel().readUserWithId(showList.get(position).getSender(), new FirestoreResults() {
            @Override
            public void onResult() {
                if (new UserViewModel().getUser().getName().equals(myName)){
                    holder.name.setText("You");
                    holder.mainLayout.setBackgroundColor(Color.GREEN);
                    holder.name.setTextColor(Color.WHITE);
                    holder.msg.setTextColor(Color.WHITE);
                    holder.time.setTextColor(Color.WHITE);
                }
                else{
                    holder.name.setText(new UserViewModel().getUser().getName());
                    holder.mainLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.lightGrey));
                    holder.time.setTextColor(Color.BLACK);
                    holder.name.setTextColor(Color.BLACK);
                    holder.msg.setTextColor(Color.BLACK);
                }
                holder.time.setText(showList.get(position).getTime());
                holder.msg.setText(showList.get(position).getMsg());
            }
        });

    }

    @Override
    public int getItemCount() {
        return showList.size();
    }
    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView name,time,msg;
        ConstraintLayout mainLayout;
        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chatNameTextListItemLayout);
            time = itemView.findViewById(R.id.chatTimeTextListItemLayout);
            msg = itemView.findViewById(R.id.chatmsgTextListItemLayout);
            mainLayout = itemView.findViewById(R.id.chatParentLayout);
        }
    }
}