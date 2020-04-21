package com.fyp.alike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class ResultsActivity extends AppCompatActivity {

    private LinkedList<User> myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final ProgressDialog dialog = ProgressDialog.show(this, "Getting ready",
                "Please wait...", true,false);
        new UserViewModel().readAllUserList(new FirestoreResults() {
            @Override
            public void onResult() {
                myList = new UserViewModel().getUserList();
                for (User u : myList){
                    Random r = new Random();
                    int low = 30;
                    int high = 80;
                    int result = r.nextInt(high-low) + low;
                    u.setTempPercent(result);
                }
                Collections.sort(myList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o2.getTempPercent() - o1.getTempPercent();
                    }
                });
                resultAdapter adapter = new resultAdapter(myList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });

    }
}
