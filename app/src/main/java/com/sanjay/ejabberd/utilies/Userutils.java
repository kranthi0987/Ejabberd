/*
 * Copyright (c) 2019.
 * Project created and maintained by sanjay kranthi kumar
 * if need to contribute contact us on
 * kranthi0987@gmail.com
 */

package com.sanjay.ejabberd.utilies;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

public class Userutils {
    ConnectionUtils connectionUtils = new ConnectionUtils();
    XMPPConnection connection;

    public Userutils() {
    }

    public String getUserNickname() {
        if (connection != null)
            connection = connectionUtils.getXmptcConnection();
        VCard mVCard = new VCard();
        try {
            mVCard.load(connection, connection.getUser().asEntityBareJidIfPossible());
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mVCard.getNickName();
    }

    public void setUserNickName(String Nickname) {
        if (connection != null)
            connection = connectionUtils.getXmptcConnection();
        VCard mVCard = new VCard();
        try {
            mVCard.load(connection, connection.getUser().asEntityBareJidIfPossible());
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mVCard.setNickName(Nickname);
    }
}
