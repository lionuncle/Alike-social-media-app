package com.fyp.alike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView profile;
    private TextView name;
    private EditText msg;
    private ImageView send;
    private RecyclerView recyclerView;
    private String otherPersonName;
    private String otherPersonEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        profile = findViewById(R.id.chatImage);
        name = findViewById(R.id.chatNameText);
        msg = findViewById(R.id.chatMsgText);
        send = findViewById(R.id.chatSendImgView);
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        otherPersonEmail = getIntent().getStringExtra("otherPersonEmail");
        new UserViewModel().readUserWithId(otherPersonEmail, new FirestoreResults() {
            @Override
            public void onResult() {
                otherPersonName = new UserViewModel().getUser().getName();
                name.setText(otherPersonName);
            }
        });
        new ChatViewModel().readAllChatOf(MainActivity.email, otherPersonEmail, new FirestoreResults() {
            @Override
            public void onResult() {
                if (new ChatViewModel().getChatList().size() != 0){
                    recyclerView.setAdapter(new chatAdapter(new ChatViewModel().getChatList(),MainActivity.name));
                }
            }
        });
        if (resultAdapter.otherPersonImage != null){
            profile.setImageBitmap(resultAdapter.otherPersonImage);
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getText().toString().trim().equals("")){
                    Toast.makeText(ChatActivity.this, "Please write something", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar cc = Calendar.getInstance();
                int year = cc.get(Calendar.YEAR);
                int month = cc.get(Calendar.MONTH);
                int mDay = cc.get(Calendar.DAY_OF_MONTH);
                int mHour = cc.get(Calendar.HOUR_OF_DAY);
                int mMinute = cc.get(Calendar.MINUTE);
                Chat c = new Chat();
                c.setSender(MainActivity.email);
                c.setReceiver(otherPersonEmail);
                c.setTime(mHour+":"+mMinute+" "+mDay+"/"+month+"/"+year);
                c.setMsg(msg.getText().toString());
                new ChatViewModel().sendMsg(c);
                msg.setText("");
            }
        });
        new ChatViewModel().listener(MainActivity.email, otherPersonEmail, new FirestoreResults() {
            @Override
            public void onResult() {
                new ChatViewModel().readAllChatOf(MainActivity.email, otherPersonEmail, new FirestoreResults() {
                    @Override
                    public void onResult() {
                        if (new ChatViewModel().getChatList().size() != 0){
                            recyclerView.setAdapter(new chatAdapter(new ChatViewModel().getChatList(),MainActivity.name));

                        }
                    }
                });
            }
        });

    }
}
