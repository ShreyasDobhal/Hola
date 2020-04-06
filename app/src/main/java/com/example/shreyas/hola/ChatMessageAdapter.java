package com.example.shreyas.hola;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        LinearLayout message_block = (LinearLayout) listItemView.findViewById(R.id.message_block);
        LinearLayout reply_block = (LinearLayout) listItemView.findViewById(R.id.reply_block);
        TextView replyToUserTxt = (TextView) listItemView.findViewById(R.id.message_replyToUser);
        TextView replyToMsgTxt = (TextView) listItemView.findViewById(R.id.message_replyToMessage);
        ImageView imgView = (ImageView)listItemView.findViewById(R.id.message_image);
        TextView likeTxt = (TextView) listItemView.findViewById(R.id.message_like);


        if (chatMessage.getReplyToMessage() == null || chatMessage.getReplyToMessage().equals("")) {
//            message_block.removeView(reply_block);
            reply_block.setVisibility(View.GONE);
            Log.e("LOG","not a reply");
        } else {
            reply_block.setVisibility(View.VISIBLE);
            if (replyToMsgTxt == null) {
                Log.e("LOG","replyToMsgTxt was null");
            }
            replyToMsgTxt.setText(chatMessage.getReplyToMessage());
            replyToUserTxt.setText(chatMessage.getReplyToUser());
            Log.e("LOG","replying done");
        }

        if (chatMessage.getImageURL() == null) {
//            message_block.removeView(img);
            imgView.setVisibility(View.GONE);
        } else {
            // TODO display image
            imgView.setVisibility(View.VISIBLE);
        }

        if (chatMessage.getIsLiked()) {
            likeTxt.setVisibility(View.VISIBLE);
        } else {
            likeTxt.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
