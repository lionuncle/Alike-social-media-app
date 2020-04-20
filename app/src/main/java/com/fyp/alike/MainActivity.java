package com.fyp.alike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fyp.alike.LoginActivity.firebaseUserr;

public class MainActivity extends AppCompatActivity {

    public static String name,email,photoUrl;
    private CircleImageView circleImageView;
    private TextView userDisplayName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleImageView = findViewById(R.id.profileImg);
        userDisplayName = findViewById(R.id.UserDisplayName);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        photoUrl = getIntent().getStringExtra("photo");
        Toast.makeText(this, "Welcome "+ name, Toast.LENGTH_SHORT).show();
        try {
            circleImageView.setImageBitmap(getBitmapFromURL(firebaseUserr.getPhotoUrl().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ProgressDialog dialog = ProgressDialog.show(this, "Getting ready",
                "Please wait...", true,false);
        new UserViewModel().readUserWithId(email, new FirestoreResults() {
            @Override
            public void onResult() {
                userDisplayName.setText(new UserViewModel().getUser().getName());
                dialog.dismiss();
            }
        });

    }
    private Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException | NetworkOnMainThreadException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            return null;
        }
    }
}
