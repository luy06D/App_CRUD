package com.example.crusws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btMostrarRegistro, btMostrarLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUI();

        btMostrarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(Registrar.class);

            }
        });

        btMostrarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(Listar.class);
            }
        });
    }
    //Método para aperturar cualquier ACTIVITY
    private  void openActivity(Class activity){
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }
    private void loadUI(){
        btMostrarRegistro =findViewById(R.id.btMostrarRegistro);
        btMostrarLista = findViewById(R.id.btMostrarLista);
    }
}