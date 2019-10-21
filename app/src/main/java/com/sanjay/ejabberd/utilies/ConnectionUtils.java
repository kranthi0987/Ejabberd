package com.sanjay.ejabberd.utilies;

import com.sanjay.ejabberd.service.XMPP;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

public class ConnectionUtils {

    XMPPTCPConnection connection = null;

    public XMPPTCPConnection getXmptcConnection() {

        if (XMPP.getInstance().isConnected()) {
            try {
                connection = XMPP.getInstance().getConnection();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                connection = XMPP.getInstance().getConnection();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}
