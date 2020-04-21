package com.example.shreyas.hola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    private EditText txtName;
    private Button btnDone;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = (User) getIntent().getSerializableExtra("User");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("users");


        txtName = (EditText) findViewById(R.id.txt_username);
        btnDone = (Button) findViewById(R.id.btn_done);

        txtName.setText(currentUser.getUsername());

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = txtName.getText().toString().trim();
                if (!name.isEmpty() && !name.equals(currentUser.getUsername())) {
                    Query pendingTasks = mMessagesDatabaseReference.orderByChild("uid").equalTo(currentUser.getUID());
                    pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot datasnapshot:dataSnapshot.getChildren()) {
                                Log.e("USER",datasnapshot.toString());
                                datasnapshot.getRef().child("username").setValue(name);
                            }

//                            dataSnapshot.getRef().child("username").setValue(name);
//                            Toast.makeText(getApplicationContext(),"Display name changed",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(DatabaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                }
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ProfileActivity.this.startActivity(intent);
            }
        });





    }
}
