package com.sanjay.ejabberd;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sanjay.ejabberd.app.Constants;
import com.sanjay.ejabberd.service.XMPP;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.jid.util.JidUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NewGroupCreate extends AppCompatActivity {
    EditText editText;
    private String TAG=NewGroupCreate.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_create);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = findViewById(R.id.editText);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (createGroup(editText.getText().toString())) {
                        Toast.makeText(getApplication(), "group created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "group not created", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            listAllGroups();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    public boolean createGroup(String roomName) throws InterruptedException, IOException, SmackException, XMPPException {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(XMPP.getInstance().getConnection());

        try {
            // Create the XMPP address (JID) of the MUC.
            EntityBareJid mucJid = JidCreate.entityBareFrom(roomName + "@conference." + Constants.HOST);

            // Create a MultiUserChat using an XMPPConnection for a room
            MultiUserChat muc = manager.getMultiUserChat(mucJid);


// Prepare a list of owners of the new room
//            Set<Jid> owners = JidUtil.jidSetFrom(new String[]{"admin@206.189.136.186"});
            // Create the nickname.
            Resourcepart nickname = Resourcepart.from("admin");

            muc.create(nickname).getConfigFormManager().submitConfigurationForm();
//            muc2.create(nickname);
//            muc.join(nickname);
//            muc.sendMessage(message);
            return true;
        } catch (SmackException.NotConnectedException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static void createRoom(String roomName, XMPPTCPConnection connection) throws XmppStringprepException, InterruptedException, SmackException.NoResponseException, MultiUserChatException.MucAlreadyJoinedException, SmackException.NotConnectedException, MultiUserChatException.MissingMucCreationAcknowledgeException, MultiUserChatException.NotAMucServiceException {
//        if (connection == null) {
//            return;
//        }
//        try {
//            // Create a MultiUserChat
//            MultiUserChat muc = new MultiUserChat(connection, roomName
//                    + "@conference." + connection.getServiceName());
//            // Create a chat room
//            muc.create(Resourcepart.from(roomName)); // RoomName room name
//            // To obtain the chat room configuration form
//            Form form = muc.getConfigurationForm();
//            // Create a new form to submit the original form according to the.
//            Form submitForm = form.createAnswerForm();
//            // To submit the form to add a default reply
////            for (List<FormField> fields = form.getFields(); fields.hasNext();) {
////                FormField field = (FormField) fields.next();
////                if (!FormField.TYPE_HIDDEN.equals(field.getType())
////                        && field.getVariable() != null) {
////                     Set default values for an answer
////                    submitForm.setDefaultAnswer(field.getVariable());
////                }
////            }
//            // Set the chat room of the new owner
//            List<String> owners = new ArrayList<String>();
//            owners.add(String.valueOf(connection.getUser()));// The user JID
//            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
//            // Set the chat room is a long chat room, soon to be preserved
//            submitForm.setAnswer("muc#roomconfig_persistentroom", false);
//            // Only members of the open room
//            submitForm.setAnswer("muc#roomconfig_membersonly", false);
//            // Allows the possessor to invite others
//            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
//            // Enter the password if needed
//            //submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
//            // Set to enter the password
//            //submitForm.setAnswer("muc#roomconfig_roomsecret", "password");
//            // Can be found in possession of real JID role
//            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
//            // Login room dialogue
//            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
//            // Only allow registered nickname log
//            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
//            // Allows the user to modify the nickname
//            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
//            // Allows the user to register the room
//            submitForm.setAnswer("x-muc#roomconfig_registration", false);
//            // Send the completed form (the default) to the server to configure the chat room
//            submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
//            // Send the completed form (the default) to the server to configure the chat room
//            muc.sendConfigurationForm(submitForm);
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean joinGroup(String roomName) throws InterruptedException, IOException, SmackException, XMPPException {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(XMPP.getInstance().getConnection());

        try {
            // Create the XMPP address (JID) of the MUC.
            EntityBareJid mucJid = JidCreate.entityBareFrom(roomName + "@conference." + Constants.HOST);

            // Create a MultiUserChat using an XMPPConnection for a room
            MultiUserChat muc = manager.getMultiUserChat(mucJid);


// Prepare a list of owners of the new room
            Set<Jid> owners = JidUtil.jidSetFrom(new String[]{"admin@206.189.136.186"});
            // Create the nickname.
            Resourcepart nickname = Resourcepart.from("admin");

//            muc.create(nickname).getConfigFormManager().submitConfigurationForm();
//            muc2.create(nickname);
            muc.join(nickname);
//            muc.sendMessage(message);
            return true;
        } catch (SmackException.NotConnectedException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean listAllGroups() throws InterruptedException, IOException, SmackException, XMPPException {
        String groupchatHosted = "";
        XMPPTCPConnection connection = XMPP.getInstance().getConnection();
        DomainBareJid service = connection.getServiceName();
        Log.d(TAG, "service"+service);
        List<HostedRoom> hosted = MultiUserChatManager.getInstanceFor(connection).getHostedRooms(service);

        for (HostedRoom room : hosted) {
            groupchatHosted += room.getJid() + " ";
            Log.d(TAG, "listAllGroups:"+groupchatHosted);
        }
        return true;
    }

    private void setConfig(MultiUserChat multiUserChat) {

        try {
            Form form = multiUserChat.getConfigurationForm();
            Form submitForm = form.createAnswerForm();
//            for (Iterator<FormField> fields = submitForm.getFields(); fields
//                    .hasNext();) {
//                FormField field = (FormField) fields.next();
//                if (!FormField.Type.hidden.equals(field.getType())
//                        && field.getVariable() != null) {
//                    submitForm.setDefaultAnswer(field.getVariable());
//                }
//            }
            submitForm.setTitle(multiUserChat.getReservedNickname());
            submitForm.setAnswer("muc#roomconfig_publicroom", true);
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            multiUserChat.sendConfigurationForm(submitForm);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
