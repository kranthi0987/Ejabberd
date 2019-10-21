package com.sanjay.ejabberd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sanjay.ejabberd.app.Constants;
import com.sanjay.ejabberd.service.XMPP;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import java.io.IOException;

public class GroupChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupChatActivity.this,NewGroupCreate.class);
                startActivity(i);
            }
        });
//        try {
//            createJoinGroup("welcome test");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SmackException e) {
//            e.printStackTrace();
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        }
    }

    //    public boolean createGroup(XMPPConnection connection, String groupName) {
//        if (connection == null)
//            return false;
//        try {
//            connection.getRoster().createGroup(groupName);
//            Log.v("Group created : ", groupName);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean createJoinGroup(String message) throws InterruptedException, IOException, SmackException, XMPPException {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(XMPP.getInstance().getConnection());

//        try {
//            muc.create(StringUtils.parseName(XMPP.getInstance().getConnection(getActivity()).getUser()));
//            muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
//        } catch (XMPPException e) {
//        } catch (SmackException.NoResponseException e) {
//            e.printStackTrace();
//        } catch (SmackException e) {
//            e.printStackTrace();
//        }

//        for (int i = 0; i < alSelectedContacts.size(); i++) {
//
//            Log.e("tag", "group chating purpose1 ::" + alSelectedContacts.get(i).get("id"));
//            try {
//                muc.invite((alSelectedContacts.get(i).get("id") + "_user") + "@" + XMPP.HOST,
//                        alSelectedContacts.get(i).get("id") + "_user");
//            } catch (SmackException.NotConnectedException e) {
//                e.printStackTrace();
//            }
//
//        }

        // Create the XMPP address (JID) of the MUC.
//        chat
        EntityBareJid mucJid = (EntityBareJid) JidCreate.bareFrom("g1@conference." + Constants.HOST);

        // Create the nickname.
        Resourcepart nickname = Resourcepart.from("admin");

// Create a MultiUserChat using an XMPPConnection for a room
        MultiUserChat muc2 = manager.getMultiUserChat(mucJid);
        try {
//            muc2.create(nickname);
            muc2.join(nickname);
            muc2.sendMessage(message);
        } catch (SmackException.NotConnectedException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
