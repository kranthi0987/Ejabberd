package com.sanjay.ejabberd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sanjay.ejabberd.service.XMPP;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jxmpp.jid.Jid;


public class MainActivity extends AppCompatActivity {
//    private UserLoginTask mAuthTask = null;
    private ChatManagerListener chatListener;
    private Chat chat;
    private Jid opt_jid;
    private ChatMessageListener messageListener;
    private StanzaListener packetListener;
    private String TAG="main activity";

    Button btn_login,btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login=findViewById(R.id.button);
        btn_register=findViewById(R.id.button2);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);

            }
        });


    }




//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        public UserLoginTask() {
//        }
//
//        protected Boolean doInBackground(Void... paramVarArgs) {
//            String mEmail = "abc";
//            String mUsername = "abc";
//            String mPassword = "welcome";
//
//            if (register(mEmail, mPassword)) {
//                try {
//                    XMPP.getInstance().close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return login(mEmail, mPassword, mUsername);
//
//        }
//
//        protected void onCancelled() {
//            mAuthTask = null;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        protected void onPostExecute(Boolean success) {
//            mAuthTask = null;
//            try {
//                if (success) {
//
//                    messageListener = new ChatMessageListener() {
//                        @Override
//                        public void processMessage(Chat chat, Message message) {
//
//                            // here you will get only connected user by you
//
//                        }
//                    };
//
//
//                    packetListener = new StanzaListener() {
//                        @Override
//                        public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
//                            if (packet instanceof Message) {
//                                final Message message = (Message) packet;
//
//                                // here you will get all messages send by anybody
//                            }
//                        }
//
//                    };
//
//                    chatListener = new ChatManagerListener() {
//
//                        @Override
//                        public void chatCreated(Chat chatCreated, boolean local) {
//                            onChatCreated(chatCreated);
//                        }
//                    };
//
//
//                    try {
//                        String opt_jidStr = "abc";
//
//                        try {
//                            opt_jid = JidCreate.bareFrom(Localpart.from(opt_jidStr), Domainpart.from(HOST));
//                        } catch (XmppStringprepException e) {
//                            e.printStackTrace();
//                        }
//                        String addr1 = XMPP.getInstance().getUserLocalPart(MainActivity.this);
//                        String addr2 = opt_jid.toString();
//                        if (addr1.compareTo(addr2) > 0) {
//                            String addr3 = addr2;
//                            addr2 = addr1;
//                            addr1 = addr3;
//                        }
//                        chat = XMPP.getInstance().getThreadChat(MainActivity.this, addr1, addr2);
//                        if (chat == null) {
//                            chat = XMPP.getInstance().createChat(MainActivity.this, (EntityJid) opt_jid, addr1, addr2, messageListener);
//                            //PurplkiteLogs.logInfo(TAG, "chat value single chat 1 :" + chat);
//                        } else {
//                            chat.addMessageListener(messageListener);
//                            //PurplkiteLogs.logInfo(TAG, "chat value single chat  2:" + chat);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                    XMPP.getInstance().addStanzaListener(MainActivity.this, packetListener);
//                    XMPP.getInstance().addChatListener(MainActivity.this, chatListener);
//                    XMPP.getInstance().getSrvDeliveryManager(MainActivity.this);
//
//                } else {
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    /**
     * user attemptLogin for xmpp
     *
     */

//    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }
//
//        boolean cancel = false;
//        View focusView = null;
//
//        if (cancel) {
//            focusView.requestFocus();
//        } else {
//            try {
//                mAuthTask = new UserLoginTask();
//                mAuthTask.execute((Void) null);
//            } catch (Exception e) {
//
//            }
//
//        }
//    }

    void onChatCreated(Chat chatCreated) {
        if (chat != null) {
            if (chat.getParticipant().getLocalpart().toString().equals(
                    chatCreated.getParticipant().getLocalpart().toString())) {
                chat.removeMessageListener(messageListener);
                chat = chatCreated;
                chat.addMessageListener(messageListener);
            }
        } else {
            chat = chatCreated;
            chat.addMessageListener(messageListener);
        }
    }

    private void sendMessage(String message) {
        if (chat != null) {
            try {
                chat.sendMessage(message);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            XMPP.getInstance().removeChatListener(this, chatListener);
            if (chat != null && messageListener != null) {
                XMPP.getInstance().removeStanzaListener(this, packetListener);
                chat.removeMessageListener(messageListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
