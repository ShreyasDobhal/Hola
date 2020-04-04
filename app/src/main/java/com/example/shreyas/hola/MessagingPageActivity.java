package com.example.shreyas.hola;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class MessagingPageActivity extends AppCompatActivity  {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    private ContactDisplay otherUser;
    private ContactDisplay currentUser;

    RelativeLayout activity_main;

    private int clickCount = 0;
    private int lastItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_page);

        otherUser = (ContactDisplay) getIntent().getSerializableExtra("ContactDisplay");
        currentUser = new ContactDisplay("Shreyas");

        String otherContact = otherUser.getName();
//        String messages[] = {"Hello","Good morning","How are you"};
//        String time[] = {"8:00","8:05","8:10"};

        final ArrayList<ChatMessage> chatMessages = new ArrayList<>();
//        for (int i=0;i<messages.length;i++) {
//            chatMessages.add(new ChatMessage(messages[i],otherContact,time[i]));
//        }

        final ChatMessageAdapter itemsAdapter = new ChatMessageAdapter(this, chatMessages);
        final ListView listView = (ListView) findViewById(R.id.list_of_message);
        listView.setAdapter(itemsAdapter);

        final EditText txtInput = (EditText) findViewById(R.id.inputtxt);
        Button sendBtn = (Button) findViewById(R.id.sendbtn);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txtInput.getText().toString();
                if (msg.length()>0) {
                    msg = msg.trim();
                    ChatMessage sentMessage = new ChatMessage(msg,"You",timeStamp.getTime(),true);
//                    ChatMessage receivedMessage = new ChatMessage(msg,currentUser.getName(),timeStamp.getTime(),false);
//                    chatMessages.add(new ChatMessage(msg,"You",timeStamp.getTime(),true));
//                    itemsAdapter.notifyDataSetChanged();
                    txtInput.setText("");

                    mMessagesDatabaseReference.push().setValue(sentMessage);
                    Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                chatMessages.add(message);
                itemsAdapter.notifyDataSetChanged();
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



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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

                        } else if (clickCount==2) {
                            Toast.makeText(getApplicationContext(),"Double Click",Toast.LENGTH_SHORT).show();
                        }
                        clickCount=0;
                    }
                },500);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),chatMessages.get(i).getMessageText(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

}