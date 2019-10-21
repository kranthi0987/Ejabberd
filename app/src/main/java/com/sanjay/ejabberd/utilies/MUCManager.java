/*
 * Copyright (c) 2019.
 * Project created and maintained by sanjay kranthi kumar
 * if need to contribute contact us on
 * kranthi0987@gmail.com
 */

package com.sanjay.ejabberd.utilies;


import android.widget.Toast;

import com.sanjay.ejabberd.app.Constants;
import com.sanjay.ejabberd.app.OelpchatApplication;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

public class MUCManager {
    private ConnectionUtils connectionUtils = new ConnectionUtils();

    public MultiUserChat getMUCChat(String roomName) {
        XMPPConnection connection = connectionUtils.getXmptcConnection();
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
        MultiUserChat muc = null;
        try {
            EntityBareJid mucJid = JidCreate.entityBareFrom(roomName + "@conference." + Constants.HOST);
            muc = manager.getMultiUserChat(mucJid);
            Userutils userutils = new Userutils();
            userutils.setUserNickName("admin");

            Resourcepart nickname = Resourcepart.from(userutils.getUserNickname());

            muc.createOrJoin(nickname).getConfigFormManager().submitConfigurationForm();
            muc.sendMessage("hai");
            Toast.makeText(OelpchatApplication.getContext(), "joined to group", Toast.LENGTH_SHORT).show();
        } catch (SmackException.NotConnectedException | SmackException.NoResponseException | XMPPException.XMPPErrorException | MultiUserChatException.NotAMucServiceException | MultiUserChatException.MucAlreadyJoinedException e1) {
            e1.printStackTrace();
        } catch (InterruptedException | XmppStringprepException e) {
            e.printStackTrace();
        }
        return muc;
    }

    public boolean sendMessageinMuc(String message, MultiUserChat multiUserChat) {
        if (multiUserChat != null) {
            try {
                multiUserChat.sendMessage(message);
                return true;
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
