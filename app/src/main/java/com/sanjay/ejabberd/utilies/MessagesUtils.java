/*
 * Copyright (c) 2019.
 * Project created and maintained by sanjay kranthi kumar
 * if need to contribute contact us on
 * kranthi0987@gmail.com
 */

package com.sanjay.ejabberd.utilies;

import android.util.Log;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.forward.packet.Forwarded;
import org.jivesoftware.smackx.rsm.packet.RSMSet;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

import java.util.ArrayList;
import java.util.List;

import co.intentservice.chatui.models.ChatMessage;

public class MessagesUtils {
    String TAG = MessagesUtils.class.getSimpleName();

    public List<ChatMessage> getChatHistoryWithJID(String jid, int maxResults) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        MamManager.MamQueryResult mamQueryResult = getArchivedMessages(jid, maxResults);
        String userSendTo = XmppUtils.parseNameFromJID(jid);

        try {
            if (mamQueryResult != null && userSendTo != null) {
                for (Forwarded forwarded : mamQueryResult.forwardedMessages) {
                    if (forwarded.getForwardedStanza() instanceof Message) {
                        Message msg = (Message) forwarded.getForwardedStanza();
                        Log.d(TAG, "onCreate: " + msg.toString());
                        Log.d(TAG, "processStanza: " + msg.getFrom() + " Say：" + msg.getBody() + " String length：" + (msg.getBody() != null ? msg.getBody().length() : ""));
                        ChatMessage chatMessage;
                        if (XmppUtils.parseNameFromJID(msg.getFrom().toString()).equalsIgnoreCase(userSendTo)) {
                            chatMessage = new ChatMessage(msg.getBody(), forwarded.getDelayInformation().getStamp().getTime(), ChatMessage.Type.RECEIVED);
                        } else {
                            chatMessage = new ChatMessage(msg.getBody(), forwarded.getDelayInformation().getStamp().getTime(), ChatMessage.Type.SENT);
                        }
                        chatMessageList.add(chatMessage);
                    }
                }
            } else {
                return chatMessageList;
            }
            return chatMessageList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatMessageList;
    }

    public MamManager.MamQueryResult getArchivedMessages(String jid, int maxResults) {

        MamManager mamManager = MamManager.getInstanceFor(connection);
        try {
            DataForm form = new DataForm(DataForm.Type.submit);
            FormField field = new FormField(FormField.FORM_TYPE);
            field.setType(FormField.Type.hidden);
            field.addValue(MamElements.NAMESPACE);
            form.addField(field);

            FormField formField = new FormField("with");
            formField.addValue(jid);
            form.addField(formField);

            // "" empty string for before
            RSMSet rsmSet = new RSMSet(maxResults, "", RSMSet.PageDirection.before);
            MamManager.MamQueryResult mamQueryResult = mamManager.page(form, rsmSet);

            return mamQueryResult;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
