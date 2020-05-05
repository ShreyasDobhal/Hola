'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const util = require('util');

admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/allMessages/{current_user_id}/{other_user_id}/{message_id}').onCreate((data,context) => {
    const other_user_id = context.params.other_user_id;
    const current_user_id = context.params.current_user_id;
    const message_id = context.params.message_id;

    // console.log("Current user : "+current_user_id);
    // console.log("Other user : "+other_user_id);
    // console.log("Message id : "+message_id);

    return admin.database().ref(`allMessages/${current_user_id}/${other_user_id}/${message_id}`).once('value').then(snapshot => {
        const message_obj = snapshot.val();
       
        // console.log(util.inspect(message_obj,{depth:null}));
       
        if (message_obj==null) {
            console.log("Message null exiting");
            return "Invalid message";
        }

        // Message Received
        if (message_obj['isMessageSent']==false) {

            if (message_obj['isDateLabel']!=null && message_obj['isDateLabel']==false) {
                // Normal Message

                return admin.database().ref('/users').orderByChild('uid').equalTo(current_user_id).once('value').then(snapshot => {
               
                    const user_id = Object.keys(snapshot.val());
                    const user_obj = (snapshot.val())[user_id[0]];
                    const token_id = user_obj['fcmtoken'];
                    const user_name = user_obj['username'];
                    const message_text = message_obj['messageText'];
                    const sender_name = message_obj['messageUser'];
   
                    console.log("Message : "+message_text);
                    console.log("Sender : "+sender_name);
                    console.log("Receiver : "+user_name);
                   
                    const payload = {
                        notification : {
                            title: sender_name,
                            body: message_text
                        }
                    };
                   
                    console.log("Payload : "+util.inspect(payload,{depth:null}));

                    if (token_id==null || token_id=="") {
                        console.log("Token Id Empty");
                        return "Invalid token id";
                    }
   
                    return admin.messaging().sendToDevice(token_id,payload).then(response => {
                        console.log("Notification sent");
                    });
   
                });

               
            } else {
                // Date Stamp Message
                return "Date stamp";
            }

        }
       
    });

});