package com.example.feliciano.buslocation;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


import io.fabric.sdk.android.Fabric;

////

public class Inicio extends AppCompatActivity implements View.OnClickListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "nLcpEfjKOKCHnx4Ua2SZmfZIY";
    private static final String TWITTER_SECRET = "OuWZIYDYnJUxwumL4mF9KNdSrJUTPCQ715t5sMRNSA8kLGmaPH";


    Button bChofer,bUsuario;
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_inicio);
        bChofer=(Button)findViewById(R.id.buttonChofer);
        bUsuario=(Button)findViewById(R.id.buttonUsuario);
        bChofer.setOnClickListener(this);
        bUsuario.setOnClickListener(this);

        ////////////
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                if(session.getUserName().equals("felicianoms543")){
                    //System.out.println("bienbenido feliciano");
                    Toast.makeText(getApplicationContext(), "bienvenido feliciano", Toast.LENGTH_LONG).show();
                    IrUsuario(session.getUserName());
                }
                else if(session.getUserName().equals("felicianoms543a")){
                    IrChofer(session.getUserName());

                }
                else{
                    Toast.makeText(getApplicationContext(),"acceso denegado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(TwitterException e) {
                //Log.d("TwitterKit", "Login with Twitter failure", exception);
                System.out.println("Login with Twitter failure");
            }
        });

    }
    public void IrUsuario(String nombre){
        Intent i=new Intent(getApplicationContext(),MenuUsuario.class);
        //i.putExtra("nombre",nombre);
        startActivity(i);
    }
    public void IrChofer(String nombre){
        Intent i=new Intent(getApplicationContext(),UsuarioChofer.class);
        i.putExtra("nombre", nombre);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonChofer:
                Intent siguiente=new Intent(getApplicationContext(),UsuarioChofer.class);
                siguiente.putExtra("nombre","chofer");
                startActivity(siguiente);
                break;
            case R.id.buttonUsuario:
                Intent siguienteU=new Intent(getApplicationContext(),MenuUsuario.class);
                startActivity(siguienteU);
                break;
            default:
                break;
        }

    }


}