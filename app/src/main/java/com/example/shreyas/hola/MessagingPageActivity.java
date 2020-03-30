package com.example.shreyas.hola;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
//import android.support.design.widget.FloatingActionButton;

public class MessagingPageActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE=1;
//    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
//    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_page);

        String otherContact = "Shreyas";
        String messages[] = {"Hello","Good morning","How are you"};
        String time[] = {"8:00 am","8:05 am","8:10 am"};
        final ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        for (int i=0;i<messages.length;i++) {
            chatMessages.add(new ChatMessage(messages[i],otherContact,time[i]));
        }

        ChatMessageAdapter itemsAdapter = new ChatMessageAdapter(this, chatMessages);
        ListView listView = (ListView) findViewById(R.id.list_of_message);
        listView.setAdapter(itemsAdapter);

        Toast.makeText(getApplicationContext(),"Messaging Page",Toast.LENGTH_SHORT).show();
    }
}
