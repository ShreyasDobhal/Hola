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

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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

        RelativeLayout dateUnit = (RelativeLayout) listItemView.findViewById(R.id.date_unit);
        LinearLayout messageUnit = (LinearLayout) listItemView.findViewById(R.id.message_unit);
        ChatMessage chatMessage = getItem(position);

        if (!chatMessage.getIsDateLabel()) {
            // Message Block
            dateUnit.setVisibility(View.GONE);
            messageUnit.setVisibility(View.VISIBLE);

            RelativeLayout msgBlock = (RelativeLayout) listItemView.findViewById(R.id.message_component);
            if (getItem(position).getIsMessageSent()) {
                msgBlock.setGravity(Gravity.RIGHT | Gravity.END);
            }else{
                msgBlock.setGravity(Gravity.LEFT | Gravity.START);
            }

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
            CircleImageView imgProfile = (CircleImageView) listItemView.findViewById(R.id.profile_image_message);

            if (chatMessage.getReplyToMessage() == null || chatMessage.getReplyToMessage().equals("")) {
                reply_block.setVisibility(View.GONE);
//                Log.i("LOG","not a reply");
            } else {
                reply_block.setVisibility(View.VISIBLE);
                if (replyToMsgTxt == null) {
//                    Log.i("LOG","replyToMsgTxt was null");
                }
                replyToMsgTxt.setText(chatMessage.getReplyToMessage());
                replyToUserTxt.setText(chatMessage.getReplyToUser());
                Log.i("REPLY","replying done");
            }

            if (!chatMessage.getIsMessageSent()) {
                int nextIndex = position+1;
                if (getCount()<=nextIndex || getItem(nextIndex)!=null && getItem(nextIndex).getIsMessageSent()) {
                    imgProfile.setVisibility(View.VISIBLE);
                    if (State.getOtherUser()!=null && State.getOtherUser().getDisplayImgPath()!=null) {
                        Glide.with(imgProfile.getContext())
                                .load(State.getOtherUser().getDisplayImgPath())
                                .into(imgProfile);
                    }
                } else {
                    imgProfile.setVisibility(View.GONE);
                }

            } else {
                imgProfile.setVisibility(View.GONE);
            }

            if (chatMessage.getImageURL() == null) {
                imgView.setVisibility(View.GONE);
            } else {
                imgView.setVisibility(View.VISIBLE);
                Glide.with(imgView.getContext())
                        .load(chatMessage.getImageURL())
                        .into(imgView);
            }

            if (chatMessage.getIsLiked()) {
                likeTxt.setVisibility(View.VISIBLE);
            } else {
                likeTxt.setVisibility(View.GONE);
            }
        } else {
            // Date Block
            dateUnit.setVisibility(View.VISIBLE);
            messageUnit.setVisibility(View.GONE);

            TextView txtDate = (TextView) listItemView.findViewById(R.id.date_text);
            txtDate.setText(chatMessage.getMessageText());
        }




        return listItemView;
    }
}
