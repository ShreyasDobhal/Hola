package com.example.shreyas.hola;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {


    public ChatMessageAdapter(Context context, ArrayList<ChatMessage> chatMessages) {
        super(context,0,chatMessages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_listitem, parent, false);
        }

        RelativeLayout msgBlock = (RelativeLayout) listItemView.findViewById(R.id.message_component);
        if (getItem(position).getIsMessageSent()) {
            msgBlock.setGravity(Gravity.RIGHT | Gravity.END);
        }else{
            msgBlock.setGravity(Gravity.LEFT | Gravity.START);
        }
        ChatMessage chatMessage = getItem(position);
        TextView msgTxt = (TextView) listItemView.findViewById(R.id.message_text);
        msgTxt.setText(chatMessage.getMessageText());
        TextView userTxt = (TextView) listItemView.findViewById(R.id.message_user);
        userTxt.setText(chatMessage.getMessageUser());
        TextView timeTxt = (TextView) listItemView.findViewById(R.id.message_time);
        timeTxt.setText(chatMessage.getMessageTime());

//        ImageView img = (ImageView)listItemView.findViewById(R.id.image);
//        img.setImageResource(contact.getDisplayImgPath());

        return listItemView;
    }
}
