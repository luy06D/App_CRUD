package com.example.crusws;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {

    EditText etApellidos, etNombres , etTelefono, etEmail, etDireccion;
    Button btRegistrar;
    String apellidos, nombres, telefono, email, direccion;
    final String URL = "http://192.168.1.101/wservice/controllers/colaborador.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        loadUI();

        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCajas();
            }
        });
    }

    private void validarCajas(){

        apellidos = etApellidos.getText().toString().trim();
        nombres = etNombres.getText().toString().trim();
        telefono = etTelefono.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        direccion = etDireccion.getText().toString().trim();

        if(apellidos.isEmpty()){
            etApellidos.setError("Complete el campo");

        }else if(nombres.isEmpty()){
            etNombres.setError("Complete este campo");

        } else if (telefono.isEmpty()) {
            etTelefono.setError("Complete el campo");

        }else {

            mostrarDialogoRegistro();
        }
    }

    private  void mostrarDialogoRegistro(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Colaboradores");
        dialogo.setMessage("¿Está seguro de registrar?");
        dialogo.setCancelable(false);

        //Definir ACEPTAR / CANCELAR
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Enviar datos utilizando Volley
                registrarColaborador();

            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //Mostrar dialogo
        dialogo.show();
    }

    private void registrarColaborador(){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                //Comunicación exitosa
                if(response.trim().equals("")){
                    resetUI();
                    etApellidos.requestFocus();
                    Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             error.printStackTrace();
             Log.d("Error", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("operacion", "add");
                parametros.put("apellidos", apellidos);
                parametros.put("nombres", nombres);
                parametros.put("telefono", telefono);
                parametros.put("email", email);
                parametros.put("direccion", direccion);
                return parametros;
            }
        };

        //Enviamos la consulta al WS

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    private void resetUI(){
        etApellidos.setText("");
        etNombres.setText("");
        etTelefono.setText("");
        etEmail.setText("");
        etDireccion.setText("");
    }


    private void loadUI(){
        etApellidos = findViewById(R.id.etApellidos);
        etNombres = findViewById(R.id.etNombres);
        etTelefono =  findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etCorreo);
        etDireccion = findViewById(R.id.etDireccion);
        btRegistrar = findViewById(R.id.btRegistrar);


    }
}