package com.fyp.alike;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fyp.alike.LoginActivity.firebaseUserr;

public class MainActivity extends AppCompatActivity {

    public static String name,email,photoUrl;
    private TextView userDisplayName;
    private static final int PICK_IMAGE_REQUEST = 36;
    private ImageView selectedImg;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private String imgId;
    private String sampleImgName;
    private Button proceedBtn;
    private boolean isImgUploaded,isSampleImageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleImageView circleImageView = findViewById(R.id.profileImg);
        userDisplayName = findViewById(R.id.UserDisplayName);
        selectedImg = findViewById(R.id.selectedImg);
        Button selectImgBtn = findViewById(R.id.selectImgBtn);
        proceedBtn = findViewById(R.id.proceedBtn);
        ImageView logout = findViewById(R.id.logutBtn);
        ImageView one = findViewById(R.id.one);
        ImageView two = findViewById(R.id.two);
        ImageView three = findViewById(R.id.three);
        ImageView four = findViewById(R.id.four);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        photoUrl = getIntent().getStringExtra("photo");
        isImgUploaded = false;
        isSampleImageSelected = false;
        proceedBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGrey));
        try {
            circleImageView.setImageBitmap(getBitmapFromURL(Objects.requireNonNull(firebaseUserr.getPhotoUrl()).toString()));
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

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                SelectImage();
            }
        });
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImgUploaded) uploadImage();
                else Toast.makeText(MainActivity.this, "Please select image first", Toast.LENGTH_SHORT).show();
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isImgUploaded = true;
                isSampleImageSelected = true;
                sampleImgName = "r1";
                proceedBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                selectedImg.setImageDrawable(getDrawable(R.drawable.r1));
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isImgUploaded = true;
                isSampleImageSelected = true;
                sampleImgName = "r2";
                proceedBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                selectedImg.setImageDrawable(getDrawable(R.drawable.r2));
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isImgUploaded = true;
                isSampleImageSelected = true;
                sampleImgName = "r3";
                proceedBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                selectedImg.setImageDrawable(getDrawable(R.drawable.r3));
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isImgUploaded = true;
                isSampleImageSelected = true;
                sampleImgName = "r4";
                proceedBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                selectedImg.setImageDrawable(getDrawable(R.drawable.r4));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                mGoogleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                selectedImg.setImageBitmap(bitmap);
                isImgUploaded = true;
                proceedBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
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
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            imgId = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + imgId);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    //image uploaded

                    new UserViewModel().updateUser(email,imgId);
                    Toast.makeText(MainActivity.this, "image successfully uploaded", Toast.LENGTH_SHORT).show();
                    
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
        if (isSampleImageSelected){
            new UserViewModel().updateUser(email,sampleImgName);
            Toast.makeText(this, "Image added", Toast.LENGTH_SHORT).show();
        }
    }
}
