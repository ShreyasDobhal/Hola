package com.example.shreyas.hola;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagingPageActivity extends AppCompatActivity  {

    // Firebase Database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference1;
    private DatabaseReference mMessagesDatabaseReference2;
    private ChildEventListener mChildEventListener;
    // Firebase Storage
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;


    private ArrayList<ChatMessage> chatMessages;
    private HashMap<String,ChatMessage> chatMessageMap;
    private HashMap<String,Integer> chatMessageIndexMap;
    private ChatMessageAdapter itemsAdapter;

    private ContactDisplay otherUser;
    private User currentUser;
    private ChatMessage replyMessage;

    RelativeLayout activity_main;

    private int clickCount = 0;
    private int lastItem = -1;
    private boolean isSingleClick = true;

    private static final int RC_PHOTO_PICKER =  2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_page);

        otherUser = (ContactDisplay) getIntent().getSerializableExtra("ContactDisplay");
        currentUser = (User) getIntent().getSerializableExtra("User");

        chatMessages = new ArrayList<>();
        chatMessageMap = new HashMap<>();
        chatMessageIndexMap = new HashMap<>();

        itemsAdapter = new ChatMessageAdapter(this, chatMessages);
        final ListView listView = (ListView) findViewById(R.id.list_of_message);
        listView.setAdapter(itemsAdapter);

        final EditText txtInput = (EditText) findViewById(R.id.inputtxt);
        Button sendBtn = (Button) findViewById(R.id.sendbtn);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mMessagesDatabaseReference1 = mFirebaseDatabase.getReference().child("allMessages").child(currentUser.getUID()).child(otherUser.getUID());
        mMessagesDatabaseReference2 = mFirebaseDatabase.getReference().child("allMessages").child(otherUser.getUID()).child(currentUser.getUID());
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        // Send button click
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txtInput.getText().toString();
                if (msg.length()>0) {
                    msg = msg.trim();
                    String msgTime = timeStamp.getTime();
                    ChatMessage sentMessage = new ChatMessage(msg,"You",msgTime,true);
                    ChatMessage receivedMessage = new ChatMessage(msg,currentUser.getUsername(),msgTime,false);

                    if (replyMessage != null) {
                        Log.e("LOG","replying to message");
                        if (replyMessage.getMessageUser().equals("You")) {
                            sentMessage.setReplyToUser("You");
                            receivedMessage.setReplyToUser(currentUser.getUsername());
                        } else {
                            sentMessage.setReplyToUser(otherUser.getName());
                            receivedMessage.setReplyToUser("You");
                        }
                        String replyDisplayMsg = replyMessage.getMessageText();
                        if (replyDisplayMsg.length()>100) {
                            replyDisplayMsg = replyDisplayMsg.substring(0,90)+"...";
                        }
                        sentMessage.setReplyToMessage(replyDisplayMsg);
                        receivedMessage.setReplyToMessage(replyDisplayMsg);

                        sentMessage.setReplyId(replyMessage.getId());
                        receivedMessage.setReplyId(replyMessage.getId());

                        Log.e("LOG",sentMessage.getReplyToUser()+" "+sentMessage.getReplyToMessage());

                        replyMessage = null;
                    } else {
                        sentMessage.setReplyToUser("");
                        sentMessage.setReplyToMessage("");
                        receivedMessage.setReplyToUser("");
                        receivedMessage.setReplyToMessage("");
                    }

                    txtInput.setText("");

                    mMessagesDatabaseReference1.push().setValue(sentMessage);
                    mMessagesDatabaseReference2.push().setValue(receivedMessage);
                    Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Send button Long click
        sendBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                return false;
            }
        });

        // Data updated in Firebase Database
        attachListener();




        // Message click (single / double)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {
                if (lastItem!=i) {
                    lastItem=i;
                    clickCount=1;
                } else {
                    clickCount+=1;
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (clickCount==1) {
                            // Reply text clear
                            if (isSingleClick) {
                                if (chatMessages.get(i).getReplyId() != null && !chatMessages.get(i).getReplyId().equals("")) {
                                    int pos = chatMessageIndexMap.get(chatMessages.get(i).getReplyId());
                                    Log.e("LOG","Reply message Position "+pos);
                                    listView.setSelection(pos);
                                }

                            }
                            isSingleClick = true;

                        } else if (clickCount==2) {
                            // TODO like the message
                            boolean likeVal = false;
                            if (chatMessages.get(i).getIsLiked()==false) {
                                likeVal = true;
                            }
//                            chatMessages.get(i).setIsLiked(false);
                            mMessagesDatabaseReference1.child(chatMessages.get(i).getId()).child("isLiked").setValue(likeVal);
                            Log.e("LOG","liked : "+likeVal);
//                            Toast.makeText(getApplicationContext(),"Double Click",Toast.LENGTH_SHORT).show();
                        }
                        clickCount=0;
                    }
                },500);

            }
        });

        // Message Long Press
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (replyMessage == chatMessages.get(i)) {
                    Toast.makeText(getApplicationContext(),"Selection cleared",Toast.LENGTH_SHORT).show();
                    replyMessage = null;
                } else {
                    Toast.makeText(getApplicationContext(),"Selected message to reply",Toast.LENGTH_SHORT).show();
                    replyMessage = chatMessages.get(i);
                }
                isSingleClick = false;
                return false;
            }
        });

    }

    private void attachListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    message.setId(dataSnapshot.getKey());
                    chatMessageMap.put(message.getId(),message);
                    chatMessageIndexMap.put(message.getId(),chatMessages.size());
                    chatMessages.add(message);
                    itemsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    chatMessageMap.get(dataSnapshot.getKey()).setIsLiked(message.getIsLiked());
//                    message.setId(dataSnapshot.getKey());
//                    chatMessages.add(message);
                    itemsAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Liked : "+chatMessageMap.get(dataSnapshot.getKey()).getIsLiked(),Toast.LENGTH_SHORT).show();
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
            mMessagesDatabaseReference1.addChildEventListener(mChildEventListener);
        }

    }

    private void detachListener() {
        if (mChildEventListener !=null) {
            mMessagesDatabaseReference1.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
//        if (mAuthStateListener !=null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//        }
//        detachListener();
//        itemsAdapter.clear();

    }

    @Override
    protected void onResume(){
        super.onResume();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//        attachListener();
    }

}
