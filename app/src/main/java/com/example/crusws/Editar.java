package com.example.crusws;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Editar extends AppCompatActivity {

    EditText etApellidos, etNombres, etTelefono, etEmail, etDireccion;
    Button btAtualizar, btCancelar;

    final String URL = "http://192.168.1.101/wservice/controllers/colaborador.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        loadUI();

        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
          //  Toast.makeText(getApplicationContext(), String.valueOf(parametros.getInt("idcolaborador")), Toast.LENGTH_SHORT).show();
            getData(parametros.getInt("idcolaborador"));
        }



        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarDialogoUpdate();

            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadUI() {
        etApellidos = findViewById(R.id.etUpdApellidos);
        etNombres = findViewById(R.id.etUpdNombres);
        etTelefono = findViewById(R.id.etUpdTelefono);
        etEmail = findViewById(R.id.etUpdCorreo);
        etDireccion = findViewById(R.id.etUpdDireccion);

        btAtualizar = findViewById(R.id.btActualizar);
        btCancelar = findViewById(R.id.btCancelarActualizar);
    }

    private void getData(int idcolaborador){
        //Añadir parametros a la URL Base
        Uri.Builder URLFULL = Uri.parse(URL).buildUpon();
        URLFULL.appendQueryParameter("operacion","getdata");
        URLFULL.appendQueryParameter("idcolaborador", String.valueOf(idcolaborador));
        String URLUpdate = URLFULL.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLUpdate, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Crearemos un objeto donde almacenamos el valor (JSON)
                try{
                    JSONObject jsonObject = response.getJSONObject(0);
                    etApellidos.setText(jsonObject.getString("apellidos"));
                    etNombres.setText(jsonObject.getString("nombres"));
                    etTelefono.setText(jsonObject.getString("telefono"));
                    //Estos campos pueden retornar "null"

                    etEmail.setText(jsonObject.getString("email").equals("null")? "": jsonObject.getString("email"));
                    etDireccion.setText(jsonObject.getString("direccion").equals("null")? "": jsonObject.getString("direccion"));

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });

        Volley.newRequestQueue(this).add(jsonArrayRequest);

    }

    private void mostrarDialogoUpdate(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Colaboradores");
        dialog.setMessage("¿Está seguro de actualizar esté registro?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actualizarColaborador();

            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }


    private void actualizarColaborador() {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Error", error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("operacion", "update");
                parametros.put("apellidos", etApellidos.getText().toString().trim());
                parametros.put("nombres", etNombres.getText().toString().trim());
                parametros.put("telefono", etTelefono.getText().toString().trim());
                parametros.put("email", etEmail.getText().toString().trim());
                parametros.put("direccion", etDireccion.getText().toString().trim());
                return parametros;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}