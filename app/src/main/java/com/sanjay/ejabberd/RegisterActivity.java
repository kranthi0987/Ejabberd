package com.sanjay.ejabberd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sanjay.ejabberd.service.XMPP;
import com.sanjay.ejabberd.utilies.InternetConnection;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

public class RegisterActivity extends AppCompatActivity {

    EditText e1, e2;
    Button b1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        e1 = findViewById(R.id.username);
        e2 = findViewById(R.id.password);
        b1 = findViewById(R.id.email_sign_in_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = e1.getText().toString();
                String password = e2.getText().toString();
                InternetConnection internetConnection=new InternetConnection(getApplicationContext());

                if(internetConnection.isNetworkAvailable()) {
                    if (register(username, password)) {
                        Intent i1 = new Intent(RegisterActivity.this, GroupChatActivity.class);
                        startActivity(i1);
                    }
                }else{
                    Toast.makeText(getApplication(),"Connectivity problem",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean register(final String paramString1, final String paramString2) {
        try {
            XMPP.getInstance().register(paramString1, paramString2);
            return true;

        } catch (XMPPException localXMPPException) {
            localXMPPException.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
