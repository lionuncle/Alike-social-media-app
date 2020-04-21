package com.fyp.alike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class ChatViewModel {
    private static LinkedList<Chat> chatList= new LinkedList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public LinkedList<Chat> getChatList(){return chatList;}


    public void readAllChatOf(final String sender, final String receiver, final FirestoreResults results){

        db.collection("Chats")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                chatList = new LinkedList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Chat c = (documentSnapshot.toObject(Chat.class));
                    if (c== null) return;
                    if ( (c.getSender().equals(sender) && c.getReceiver().equals(receiver)) ||
                            (c.getReceiver().equals(sender) && c.getSender().equals(receiver)) ){
                        chatList.add(c);
                    }
                }
                Collections.reverse(chatList);
                results.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                results.onResult();
            }
        });
    }

    void sendMsg(Chat c){
        db.collection("Chats").document().set(c);
    }
    void listener(final String sender, final String receiver, final FirestoreResults results){
        db.collection("Chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert queryDocumentSnapshots != null;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Chat c = documentSnapshot.toObject(Chat.class);
                    assert c != null;
                    if ( (c.getSender().equals(sender) && c.getReceiver().equals(receiver)) ||
                            (c.getReceiver().equals(sender) && c.getSender().equals(receiver)) ){
                        results.onResult();
                    }
                }
            }
        });
    }
}
