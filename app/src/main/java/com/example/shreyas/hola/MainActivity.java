package com.example.shreyas.hola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load Contacts form DB
        String names[] = {"Shreyas","Sheru","Mummy","Papa"};
        final ArrayList<ContactDisplay> contacts = new ArrayList<>();
        for (int i=0;i<names.length;i++) {
            contacts.add(new ContactDisplay(names[i]));
        }

        ContactDisplayAdapter itemsAdapter = new ContactDisplayAdapter(this, contacts);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

//        Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(),contacts.get(i).getName(),Toast.LENGTH_SHORT);
//                Log.e("ERROR","Toast didn't appear for "+contacts.get(i).getName());
//                Toast.makeText(getApplicationContext(),"Hello "+contacts.get(i).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MessagingPageActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }
}
