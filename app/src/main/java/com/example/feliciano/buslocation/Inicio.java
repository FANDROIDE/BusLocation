package com.example.feliciano.buslocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity implements View.OnClickListener{

    Button bChofer,bUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        bChofer=(Button)findViewById(R.id.buttonChofer);
        bUsuario=(Button)findViewById(R.id.buttonUsuario);
        bChofer.setOnClickListener(this);
        bUsuario.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonChofer:
                Intent siguiente=new Intent(getApplicationContext(),UsuarioChofer.class);
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