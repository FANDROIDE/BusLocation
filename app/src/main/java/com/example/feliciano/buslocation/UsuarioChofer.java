package com.example.feliciano.buslocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class UsuarioChofer extends AppCompatActivity {
    //private Bundle b;
    private TextView texto;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_chofer);
        texto=(TextView)findViewById(R.id.textView1);
        //b=getIntent().getExtras();
        //texto.setText("holaa: "+b.getString("nombre"));
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        //i.putExtra("nombre", nombre);
        startActivity(i);

    }
}
