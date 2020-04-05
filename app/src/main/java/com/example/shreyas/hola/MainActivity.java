package com.example.shreyas.hola;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final int RC_SIGN_IN = 1;

//    private FirebaseAuth mAuth;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;


    private ContactDisplayAdapter itemsAdapter;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    onSignedInInitialize(user);

//                    Toast.makeText(getApplicationContext(),user.getDisplayName()+" Signed in successfully",Toast.LENGTH_SHORT).show();
                } else {
//                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setProviders(AuthUI.EMAIL_PROVIDER,AuthUI.GOOGLE_PROVIDER).build(),RC_SIGN_IN);
                    onSignedOutCleanup();
                    List<AuthUI.IdpConfig> providers;
                    providers = new ArrayList<>();
                    providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
                    providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(providers)
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };


    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==RC_SIGN_IN) {
            if (resultCode==RESULT_OK) {
//                Toast.makeText(getApplicationContext(),"Signed in",Toast.LENGTH_SHORT).show();
            } else if (resultCode==RESULT_CANCELED) {
//                Toast.makeText(getApplicationContext(),"Sign in cancelled",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void onSignedInInitialize(FirebaseUser user) {
        currentUser = new User(user.getDisplayName());
        currentUser.setUID(user.getUid());
//        user.



        // Load Contacts form DB
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("users");

        mMessagesDatabaseReference.orderByChild("uid").equalTo(currentUser.getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(),"Welcome "+currentUser.getUsername(),Toast.LENGTH_SHORT).show();
                } else {
                    // New User Sign-up
                    mMessagesDatabaseReference.push().setValue(currentUser);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final ArrayList<ContactDisplay> contacts = new ArrayList<>();
//        for (int i=0;i<names.length;i++) {
//            contacts.add(new ContactDisplay(names[i]));
//        }

        itemsAdapter = new ContactDisplayAdapter(this, contacts);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        mMessagesDatabaseReference.orderByChild("username").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if (!user.getUID().equals(currentUser.getUID())) {
                    contacts.add(new ContactDisplay(user.getUsername(),user.getUID()));
                    itemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,MessagingPageActivity.class);
                intent.putExtra("ContactDisplay",contacts.get(i));
                intent.putExtra("User",currentUser);
//                intent.putExtra("ContactDisplay",contacts.get(i));
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void onSignedOutCleanup() {
        if (itemsAdapter!=null)
            itemsAdapter.clear();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mAuthStateListener !=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void attachListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
//                    chatMessages.add(message);
//                    itemsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    private void detachListener() {

    }

}
