package com.example.shreyas.hola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Auth
//        mAuth = FirebaseAuth.getInstance();

        // Load Contacts form DB
        String names[] = {"Shreyas","Sheru","Mummy","Papa"};
        final ArrayList<ContactDisplay> contacts = new ArrayList<>();
        for (int i=0;i<names.length;i++) {
            contacts.add(new ContactDisplay(names[i]));
        }

        ContactDisplayAdapter itemsAdapter = new ContactDisplayAdapter(this, contacts);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,MessagingPageActivity.class);
                intent.putExtra("ContactDisplay",contacts.get(i));
                MainActivity.this.startActivity(intent);
            }
        });






    }

}
