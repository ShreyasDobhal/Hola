package com.example.shreyas.hola;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;

    private EditText txtName;
    private Button btnDone;
    private CircleImageView imgProfile;

    private User currentUser;
    private String imageURL;

    private static final int RC_PHOTO_PICKER =  2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = (User) getIntent().getSerializableExtra("User");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("profile_photos");


        txtName = (EditText) findViewById(R.id.txt_username);
        btnDone = (Button) findViewById(R.id.btn_done);
        imgProfile = (CircleImageView) findViewById(R.id.profile_image);

        Log.e("IMG",currentUser.getImageURL());
        if (currentUser.getImageURL()!=null) {
            Glide.with(imgProfile.getContext())
                    .load(currentUser.getImageURL())
                    .into(imgProfile);
        }

        txtName.setText(currentUser.getUsername());

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = txtName.getText().toString().trim();
                Query pendingTasks = mMessagesDatabaseReference.orderByChild("uid").equalTo(currentUser.getUID());
                pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot:dataSnapshot.getChildren()) {
                            Log.e("USER","old info : "+datasnapshot.toString());
                            if (!name.isEmpty() && !name.equals(currentUser.getUsername())) {
                                datasnapshot.getRef().child("username").setValue(name);
                            }
                            if (imageURL!=null && imageURL!=currentUser.getImageURL()) {
                                datasnapshot.getRef().child("imageURL").setValue(imageURL);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });

                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ProfileActivity.this.startActivity(intent);
            }
        });





    }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        Log.e("IMG","Activity executed");
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Log.e("IMG","Selected a file");

            Uri selectedImageUri = data.getData();
            StorageReference photoRef = mChatPhotoStorageReference.child(selectedImageUri.getLastPathSegment());

            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] imgdata = baos.toByteArray();
                //uploading the image
                UploadTask uploadTask2 = photoRef.putBytes(imgdata);
                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("IMG","Compressed successfully");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        imageURL = downloadUrl.toString();
                        Toast.makeText(getApplicationContext(),"Photo uploaded",Toast.LENGTH_SHORT).show();
//                        txtInput.setText("Image");
                        Log.e("IMG","Image URL : "+downloadUrl);
                        Log.e("IMG","Setting new image");
                        Glide.with(imgProfile.getContext())
                                .load(imageURL)
                                .into(imgProfile);
//                        Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Log.e("IMG","Failed to compress");
                photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        imageURL = downloadUrl.toString();
                        Toast.makeText(getApplicationContext(),"Photo uploaded",Toast.LENGTH_SHORT).show();
//                        txtInput.setText("Image");
                        Log.e("IMG","Image URL : "+downloadUrl);
                        Log.e("IMG","Setting new image");
                        Glide.with(imgProfile.getContext())
                                .load(imageURL)
                                .into(imgProfile);
                    }
                });
            } finally {


            }

//            ImageView imgView = (ImageView) findViewById(R.id.profile_image);
//            img.setImageBitmap();


//            Glide.with(imgView.getContext())
//                    .load(imageURL)
//                    .into(imgView);



        }
    }
}
