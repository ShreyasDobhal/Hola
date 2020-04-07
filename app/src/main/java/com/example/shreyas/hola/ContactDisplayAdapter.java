package com.example.shreyas.hola;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactDisplayAdapter extends ArrayAdapter<ContactDisplay> {

    public ContactDisplayAdapter(Context context, ArrayList<ContactDisplay> contacts) {
        super(context,0,contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contactname_listitem, parent, false);
        }
        ContactDisplay contact = getItem(position);
        TextView txtView = (TextView) listItemView.findViewById(R.id.contact_name);
        txtView.setText(contact.getName());
//        ImageView img = (ImageView)listItemView.findViewById(R.id.image);
//        img.setImageResource(contact.getDisplayImgPath());

        return listItemView;
    }
}
