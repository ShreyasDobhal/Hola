package com.example.shreyas.hola;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final int RC_SIGN_IN = 1;

//    private FirebaseAuth mAuth;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mMessagesDatabaseReferenceLastMsg;
    private ChildEventListener mChildEventListener;

    // Notifications
    private static final String CHANNEL_ID="shreyas_dobhal";
    private static final String CHANNEL_NAME="Shreyas";
    private static final String CHANNEL_DESC = "Notification example";

    private ContactDisplayAdapter itemsAdapter;

    private HashMap<String,ContactDisplay> usersMap;
    private HashMap<String,String> lastMsgMap;
    private HashMap<String,String> lastTimeMap;

    private User currentUser;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTxt = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbarTxt.setText("Hola");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_edit_name:
                        Toast.makeText(getApplicationContext(),"Edit profile",Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fr)
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        usersMap = new HashMap<>();
        lastMsgMap = new HashMap<>();
        lastTimeMap = new HashMap<>();

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


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_mainactivity,menu);
//        return true;
//    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.signout:
//                AuthUI.getInstance().signOut(this);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }


    private void onSignedInInitialize(FirebaseUser user) {
        currentUser = new User(user.getDisplayName());
        currentUser.setUID(user.getUid());
        currentUser.setFCMToken(FirebaseInstanceId.getInstance().getToken());
//        user.

//        notificationInit();



        // Load Contacts form DB
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mMessagesDatabaseReferenceLastMsg = mFirebaseDatabase.getReference().child("lastMessages");

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
                    ContactDisplay contact = new ContactDisplay(user.getUsername(),user.getUID());
                    contact.setFCMToken(user.getFCMToken());
//                    dataSnapshot.
//                    contact.setLastMessage();
                    Log.e("LSTMSG","other user "+user.getUsername());
                    contacts.add(contact);
                    usersMap.put(user.getUID(),contact);
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


        mMessagesDatabaseReferenceLastMsg.child(currentUser.getUID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG","child added");
                HashMap<String,Object> lstMsg = (HashMap<String,Object>)dataSnapshot.getValue();
                Log.e("LOG","Data "+lstMsg.toString());
                if (lstMsg != null) {
                    lstMsg = (HashMap<String,Object>)lstMsg.get("message");
                    Log.e("LSTMSG","last message not null "+lstMsg.get("messageText").toString());
                    String lastMessageText = lstMsg.get("messageText").toString();
                    if (lastMessageText!=null && lastMessageText.length() > 30) {
                        lastMessageText = lastMessageText.substring(0,30)+"..";
                    }
                    usersMap.get(dataSnapshot.getKey()).setLastMessage(lastMessageText);
                    usersMap.get(dataSnapshot.getKey()).setLastMessageTime(lstMsg.get("messageTime").toString());
                    itemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG","child added");
                HashMap<String,Object> lstMsg = (HashMap<String,Object>)dataSnapshot.getValue();
                Log.e("LOG","Data "+lstMsg.toString());
                if (lstMsg != null) {
                    lstMsg = (HashMap<String,Object>)lstMsg.get("message");
                    Log.e("LSTMSG","last message not null "+lstMsg.get("messageText").toString());
                    String lastMessageText = lstMsg.get("messageText").toString();
                    if (lastMessageText!=null && lastMessageText.length() > 30) {
                        lastMessageText = lastMessageText.substring(0,30)+"..";
                    }
                    usersMap.get(dataSnapshot.getKey()).setLastMessage(lastMessageText);
                    usersMap.get(dataSnapshot.getKey()).setLastMessageTime(lstMsg.get("messageTime").toString());
                    itemsAdapter.notifyDataSetChanged();
                }
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

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    private void notificationInit() {

        FirebaseMessaging.getInstance().subscribeToTopic("Updates");

        String id = FirebaseInstanceId.getInstance().getToken();
        Log.e("LOG","FCM "+id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("LOG","Creating channel");
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void detachListener() {

    }

}
