/*
 * Copyright (c) 2019.
 * Project created and maintained by sanjay kranthi kumar
 * if need to contribute contact us on
 * kranthi0987@gmail.com
 */

package com.sanjay.ejabberd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sanjay.ejabberd.app.Constants;
import com.sanjay.ejabberd.service.XMPP;
import com.sanjay.ejabberd.utilies.ConnectionUtils;

import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ConversationActivity extends AppCompatActivity {
    XMPPTCPConnection connection = null;
    ConnectionUtils connectionUtils = new ConnectionUtils();
    private String contactJid;
    private ChatView mChatView;
    private BroadcastReceiver mBroadcastReceiver;
    private String TAG = ConversationActivity.class.getSimpleName();
    private String name = null;
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        connection = connectionUtils.getXmptcConnection();
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("group");
            username = intent.getStringExtra("username");
        }
        mChatView = findViewById(R.id.rooster_chat_view);
        mChatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                // perform actual message sending
                if (XMPP.getInstance().isConnected()) {
                    Log.d(TAG, "The client is connected to the server,Sending Message");
                    //Send the message to the server
                    Intent intent = new Intent(Constants.SEND_MESSAGE);
                    intent.putExtra(Constants.BUNDLE_MESSAGE_BODY,
                            mChatView.getTypedMessage());
                    intent.putExtra(Constants.BUNDLE_TO, contactJid);

                    sendBroadcast(intent);
                    sendMessagetochat(chatMessage.getMessage(), name + "@conference." + Constants.HOST);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Client not connected to server ,Message not sent!",
                            Toast.LENGTH_LONG).show();
                }
                //message sending ends here
                return true;
            }
        });
        Intent intent1 = getIntent();
        contactJid = intent1.getStringExtra("EXTRA_CONTACT_JID");
        setTitle(contactJid);
    }

    private void sendMessagetochat(String body, String toJid) {
        Log.d(TAG, "Sending message to :" + toJid);

        EntityBareJid jid = null;


        ChatManager chatManager = null;
        chatManager = ChatManager.getInstanceFor(connectionUtils.getXmptcConnection());

        try {
            jid = JidCreate.entityBareFrom(toJid);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        Chat chat = chatManager.chatWith(jid);
        try {
            Message message = new Message(jid, Message.Type.chat);
            message.setBody(body);
            chat.send(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatManager.getInstanceFor(connectionUtils.getXmptcConnection()).addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid messageFrom, Message message, Chat chat) {
                ///ADDED
                Log.d(TAG, "message.getBody() :" + message.getBody());
                Log.d(TAG, "message.getFrom() :" + message.getFrom());

                String from = message.getFrom().toString();

                String contactJid = "";
                if (from.contains("/")) {
                    contactJid = from.split("/")[0];
                    Log.d(TAG, "The real jid is :" + contactJid);
                    Log.d(TAG, "The message is from :" + from);
                } else {
                    contactJid = from;
                }
                //Bundle up the intent and send the broadcast.
                Intent intent = new Intent(Constants.NEW_MESSAGE);
                intent.setPackage(getApplication().getPackageName());
                intent.putExtra(Constants.BUNDLE_FROM_JID, contactJid);
                intent.putExtra(Constants.BUNDLE_MESSAGE_BODY, message.getBody());
                getApplication().sendBroadcast(intent);
                Log.d(TAG, "Received message from :" + contactJid + " broadcast sent.");
                ///ADDED
            }
        });


        ReconnectionManager reconnectionManager = null;
        reconnectionManager = ReconnectionManager.getInstanceFor(connectionUtils.getXmptcConnection());
        ReconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case Constants.NEW_MESSAGE:
                        String from = intent.getStringExtra(Constants.BUNDLE_FROM_JID);
                        String body = intent.getStringExtra(Constants.BUNDLE_MESSAGE_BODY);
                        if (from.equals(contactJid)) {
                            ChatMessage chatMessage = new ChatMessage(body, System.currentTimeMillis(), ChatMessage.Type.RECEIVED);
                            mChatView.addMessage(chatMessage);
                        } else {
                            Log.d(TAG, "Got a message from jid :" + from);
                        }
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter(Constants.NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

}
