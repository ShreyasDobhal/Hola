package com.example.shreyas.hola;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        TextView txtName = (TextView) listItemView.findViewById(R.id.contact_name);
        txtName.setText(contact.getName());
        TextView txtLastMsg = (TextView) listItemView.findViewById(R.id.last_message);
        txtLastMsg.setText(contact.getLastMessage());
        TextView txtLstMsgTime = (TextView) listItemView.findViewById(R.id.message_time);
        txtLstMsgTime.setText(contact.getLastMessageTime());
        CircleImageView imgProfile = (CircleImageView) listItemView.findViewById(R.id.profile_image);
        if (contact.getDisplayImgPath()!=null) {
            Glide.with(imgProfile.getContext())
                    .load(contact.getDisplayImgPath())
                    .into(imgProfile);
        }

//        ImageView img = (ImageView)listItemView.findViewById(R.id.image);
//        img.setImageResource(contact.getDisplayImgPath());

        return listItemView;
    }
}
