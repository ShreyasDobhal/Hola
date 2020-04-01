package com.example.shreyas.hola;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MessagingPageActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE=1;
    private ContactDisplay otherUser;
    RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_page);

        otherUser = (ContactDisplay) getIntent().getSerializableExtra("ContactDisplay");

        String otherContact = otherUser.getName();
        String messages[] = {"Hello","Good morning","How are you"};
        String time[] = {"8:00","8:05","8:10"};
        final ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        for (int i=0;i<messages.length;i++) {
            chatMessages.add(new ChatMessage(messages[i],otherContact,time[i]));
        }

        final ChatMessageAdapter itemsAdapter = new ChatMessageAdapter(this, chatMessages);
        final ListView listView = (ListView) findViewById(R.id.list_of_message);
        listView.setAdapter(itemsAdapter);

        final EditText txtInput = (EditText) findViewById(R.id.inputtxt);
        Button sendBtn = (Button) findViewById(R.id.sendbtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txtInput.getText().toString();
                if (msg.length()>0) {
                    msg = msg.trim();
                    chatMessages.add(new ChatMessage(msg,"You",timeStamp.getTime(),true));
                    itemsAdapter.notifyDataSetChanged();
                    txtInput.setText("");
                }
            }
        });
    }
}
