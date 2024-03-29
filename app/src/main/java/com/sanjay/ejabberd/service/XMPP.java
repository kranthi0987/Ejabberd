package com.sanjay.ejabberd.service;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import com.sanjay.ejabberd.AppSettings;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static android.content.ContentValues.TAG;
import static com.sanjay.ejabberd.app.Constants.ACTION_LOGGED_IN;
import static com.sanjay.ejabberd.app.Constants.HOST;
import static com.sanjay.ejabberd.app.Constants.PORT;

public class XMPP {
    private static XMPP instance;
    private XMPPTCPConnection connection;

    private XMPPTCPConnectionConfiguration buildConfiguration() throws XmppStringprepException {
        XMPPTCPConnectionConfiguration.Builder builder =
                XMPPTCPConnectionConfiguration.builder();


        builder.setHost(HOST);
        try {
            builder.setHostAddress(InetAddress.getByName(HOST));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        builder.setPort(PORT);
        builder.setCompressionEnabled(false);
        builder.setDebuggerEnabled(true);
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
        builder.setSendPresence(true);

        builder.setKeystoreType("AndroidCAStore");
        // config.setTruststorePassword(null);
        builder.setKeystorePath(null);
        DomainBareJid serviceName = JidCreate.domainBareFrom(HOST);
        builder.setServiceName(serviceName);


        return builder.build();
    }

    public XMPPTCPConnection getConnection() throws XMPPException, SmackException, IOException, InterruptedException {
        Log.d(TAG, "Getting XMPP Connect");
        if (isConnected()) {
            Log.d(TAG, "Returning already existing connection");
            return this.connection;
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        long l = System.currentTimeMillis();
        try {
            if (this.connection != null) {
                Log.d(TAG, "Connection found, trying to connect");
                this.connection.connect();
            } else {
                Log.d(TAG, "No Connection found, trying to create a new connection");
                XMPPTCPConnectionConfiguration config = buildConfiguration();
                SmackConfiguration.DEBUG = true;
//                SSLContext context = SSLContext.getInstance("TLS");
//                context.init(null, null, null);
//                SSLSocketFactory noSSLv3Factory = null;
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//                    noSSLv3Factory = new TLSSocketFactory(context.getSocketFactory());
//                } else {
//                    noSSLv3Factory = context.getSocketFactory();
//                }
                //connection.setSSLSocketFactory(noSSLv3Factory);

                this.connection = new XMPPTCPConnection(config);
                this.connection.connect();
            }
        } catch (Exception e) {
            Log.e(TAG, "some issue with getting connection :" + e.getMessage());

        }

        Log.d(TAG, "Connection Properties: " + connection.getHost() + " " + connection.getServiceName());
        Log.d(TAG, "Time taken in first time connect: " + (System.currentTimeMillis() - l));
        return this.connection;
    }

    public static XMPP getInstance() {
        if (instance == null) {
            synchronized (XMPP.class) {
                if (instance == null) {
                    instance = new XMPP();
                }
            }
        }
        return instance;
    }

    public void close() {
        Log.i(TAG, "Inside XMPP close method");
        if (this.connection != null) {
            this.connection.disconnect();
        }
    }

    private XMPPTCPConnection connectAndLogin(Context context) {
        Log.d(TAG, "Inside connect and Login");
        if (!isConnected()) {
            Log.d(TAG, "Connection not connected, trying to login and connect");
            try {
                // Save username and password then use here
                String username = AppSettings.getUser(context);
                String password = AppSettings.getPassword(context);
                this.connection = getConnection();
                Log.d(TAG, "XMPP username :" + username);
                Log.d(TAG, "XMPP password :" + password);
                this.connection.login(username, password);
                Log.d(TAG, "Connect and Login method, Login successful");
                context.sendBroadcast(new Intent(ACTION_LOGGED_IN));
            } catch (XMPPException localXMPPException) {
                Log.e(TAG, "Error in Connect and Login Method");
                localXMPPException.printStackTrace();
            } catch (SmackException e) {
                Log.e(TAG, "Error in Connect and Login Method");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "Error in Connect and Login Method");
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.e(TAG, "Error in Connect and Login Method");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error in Connect and Login Method");
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(TAG, "Error in Connect and Login Method");
                e.printStackTrace();
            }
        }
        Log.i(TAG, "Inside getConnection - Returning connection");
        return this.connection;
    }

    public boolean isConnected() {
        return (this.connection != null) && (this.connection.isConnected());
    }

    public EntityFullJid getUser() {
        if (isConnected()) {
            return connection.getUser();
        } else {
            return null;
        }
    }

    public void login(String user, String pass, String username)
            throws XMPPException, SmackException, IOException, InterruptedException {
        Log.i(TAG, "inside XMPP getlogin Method");
        long l = System.currentTimeMillis();
        XMPPTCPConnection connect = getConnection();
        if (connect.isAuthenticated()) {
            Log.i(TAG, "User already logged in");
            return;
        }

        Log.i(TAG, "Time taken to connect: " + (System.currentTimeMillis() - l));

        l = System.currentTimeMillis();
        try {
            connect.login(user, pass);
        } catch (Exception e) {
            Log.e(TAG, "Issue in login, check the stacktrace");
            e.printStackTrace();
        }

        Log.i(TAG, "Time taken to login: " + (System.currentTimeMillis() - l));

        Log.i(TAG, "login step passed");

        PingManager pingManager = PingManager.getInstanceFor(connect);
        pingManager.setPingInterval(5000);

    }

    public void register(String user, String pass) throws XMPPException, SmackException, InterruptedException, IOException {
        Log.i(TAG, "inside XMPP register method, " + user + " : " + pass);
        long l = System.currentTimeMillis();
        AccountManager accountManager = AccountManager.getInstance(XMPP.getInstance().getConnection());
        try {
            if (accountManager.supportsAccountCreation()) {
                accountManager.sensitiveOperationOverInsecureConnection(true);
                accountManager.createAccount(Localpart.from(user), pass);

            }
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Time taken to register: " + (System.currentTimeMillis() - l));
    }

//    public String getNickName(){
//        Log.i(TAG, "inside getnick name, ");
//        long l = System.currentTimeMillis();
//        AccountManager accountManager = AccountManager.getInstance(connection);
//        try {
//            accountManager.getAccountAttribute()
//        } catch (SmackException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.i(TAG, "Time taken to register: " + (System.currentTimeMillis() - l));
//    }


    public void addStanzaListener(Context context, StanzaListener stanzaListener) {
        XMPPTCPConnection connection = connectAndLogin(context);
        connection.addAsyncStanzaListener(stanzaListener, null);
    }

    public void removeStanzaListener(Context context, StanzaListener stanzaListener) {
        XMPPTCPConnection connection = connectAndLogin(context);
        connection.removeAsyncStanzaListener(stanzaListener);
    }

    public void addChatListener(Context context, ChatManagerListener chatManagerListener) {
        ChatManager.getInstanceFor(connectAndLogin(context))
                .addChatListener(chatManagerListener);
    }

    public void removeChatListener(Context context, ChatManagerListener chatManagerListener) {
        ChatManager.getInstanceFor(connectAndLogin(context)).removeChatListener(chatManagerListener);
    }

    public void getSrvDeliveryManager(Context context) {
        ServiceDiscoveryManager sdm = ServiceDiscoveryManager
                .getInstanceFor(XMPP.getInstance().connectAndLogin(
                        context));
        //sdm.addFeature("http://jabber.org/protocol/disco#info");
        //sdm.addFeature("jabber:iq:privacy");
        sdm.addFeature("jabber.org/protocol/si");
        sdm.addFeature("http://jabber.org/protocol/si");
        sdm.addFeature("http://jabber.org/protocol/disco#info");
        sdm.addFeature("jabber:iq:privacy");

    }

    public String getUserLocalPart(Context context) {
        return connectAndLogin(context).getUser().getLocalpart().toString();
    }

    public EntityFullJid getUser(Context context) {
        return connectAndLogin(context).getUser();
    }

    public Chat getThreadChat(Context context, String party1, String party2) {
        Chat chat = ChatManager.getInstanceFor(
                XMPP.getInstance().connectAndLogin(context))
                .getThreadChat(party1 + "-" + party2);
        return chat;
    }

    public Chat createChat(Context context, EntityJid jid, String party1, String party2, ChatMessageListener messageListener) {
        Chat chat = ChatManager.getInstanceFor(
                XMPP.getInstance().connectAndLogin(context))
                .createChat(jid, party1 + "-" + party2,
                        messageListener);
        return chat;
    }

    public void sendPacket(Context context, Stanza packet) {
        try {
            connectAndLogin(context).sendStanza(packet);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
