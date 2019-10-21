package com.sanjay.ejabberd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sanjay.ejabberd.service.XMPP;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

import static com.sanjay.ejabberd.app.Constants.ACTION_LOGGED_IN;

public class LoginActivity extends AppCompatActivity {


    EditText e1, e2;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                if(login(username, password, "")){
                    Intent i1=new Intent(LoginActivity.this,GroupChatActivity.class);
                    startActivity(i1);
                    Toast.makeText(LoginActivity.this,"success fully loggd in",Toast.LENGTH_SHORT).show();
                    try {
                        System.out.println("name = " + XMPP.getInstance().getConnection().getUser().toString());
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
            }
        });
    }

    private boolean login(final String user, final String pass, final String username) {

        try {

            XMPP.getInstance().login(user, pass, username);
            sendBroadcast(new Intent(ACTION_LOGGED_IN));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                XMPP.getInstance().login(user, pass, username);
                sendBroadcast(new Intent(ACTION_LOGGED_IN));

                return true;
            } catch (XMPPException e1) {
                e1.printStackTrace();
                return false;
            } catch (SmackException e1) {
                e1.printStackTrace();
                return false;
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return false;
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            } catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }
        }
    }

}
