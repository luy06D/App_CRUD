package com.example.crusws;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Editar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            Toast.makeText(getApplicationContext(), String.valueOf(parametros.getInt("idcolaborador")), Toast.LENGTH_SHORT).show();
        }

    }
}