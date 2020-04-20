package com.fyp.alike;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class UserViewModel {
    private static LinkedList<User> userList;
    public LinkedList<User> getUserList(){return userList;}
    private static User user;
    public User getUser(){return user;}
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void readAllUserList(final FirestoreResults results){
        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userList = null;
                userList = new LinkedList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    userList.add(documentSnapshot.toObject(User.class));
                }
                results.onResult();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                results.onResult();
            }
        });
    }
    public void readUserWithId(String email, final FirestoreResults results){
        db.collection("Users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                results.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                results.onResult();
            }
        });
    }
    public void addUserToDb(User u, final FirestoreResults results){
        db.collection("Users").document(u.getEmail()).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                results.onResult();
            }
        });
    }
    public void updateUser(final String email, final String photoName){
        db.collection("Users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                assert u != null;
                u.setPhotoName(photoName);
                db.collection("Users").document(email).set(u);
            }
        });
    }

}
