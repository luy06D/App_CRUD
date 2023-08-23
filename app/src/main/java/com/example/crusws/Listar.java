package com.example.crusws;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listar extends AppCompatActivity {

    ListView lvColaboradores;

    //Objetos requeridos para transferir datos (WS > ListView)
    // Guardar apellidos y nombres (obtenido JSONObject)
    private List<String> dataList = new ArrayList<>();
    private List<Integer> dataID = new ArrayList<>(); // Guardar las primary key
    // El adaptador recibe los datos de lalista y lo enviará al ListView
    private CustomAdapter adapter;

    // Constante que almacena la url
    final String URL = "http://192.168.1.101/wservice/controllers/colaborador.php";

    //Arreglo con las opciones para mostrar al seleccionar un item
    private String[] opciones = {"Editar", "Eliminar", "Cancelar", };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        //Iniciando interfaz
        loadUI();

        //Al iniciar
        getData();
        //Evento al seleccionar un elemento de ListView
        lvColaboradores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                //EDITAR / ELIMINAR / CANCELAR
                //Ahora el mpétodo recibe la PK y la ubicación del elemento
                showAlertOptions( dataID.get(posicion), posicion);
            }
        });


    }


    private void showAlertOptions( int primaryKey, int posicionIndex){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(dataList.get(posicionIndex));
        dialogo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int itemIndex) {
                //3 opciones => 0,1,2
                switch (itemIndex){
                    case 0:
                        Intent intent = new Intent (getApplicationContext(), Editar.class);
                        intent.putExtra("idcolaborador", 15);
                        startActivity(intent);
                    case 1:
                        showConfirmDelete(primaryKey);
                    case  2:
                        dialogInterface.dismiss();
                        break;
                }

            }
        });
        dialogo.show();
    }

    private void showConfirmDelete(int pkDelete){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Eliminar");
        dialogo.setMessage("¿Está seguro de eliminar este registro?");
        dialogo.setCancelable(false);

        //Definir los botones
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Se procede a la eliminación
                deleteRegister(pkDelete);

            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialogo.show();
    }

    private void deleteRegister(int pkDelete){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getData();
                Toast.makeText(getApplicationContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String ,String>();
                parametros.put("operacion", "delete");
                parametros.put("idcolaborador", String.valueOf(pkDelete));
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getData(){
        dataID.clear();
        dataList.clear();
        adapter = new CustomAdapter(this, dataList);
        lvColaboradores.setAdapter(adapter);

        //Construir una nueva URL (pasar N valores)
        Uri.Builder URLFull = Uri.parse(URL).buildUpon();
        URLFull.appendQueryParameter("operacion", "list");

        String URLUpdate = URLFull.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLUpdate, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Cuando manipulemos el resultado JSONArray
                try{
                    // Un JsonArray => colección de Json
                    for (int i = 0; i < response.length(); i++){
                        //Crear un JSONOjject por cada elemento recorriendo
                        JSONObject jsonObject = new JSONObject(response.getString(i));
                  //      Log.d("Colaboradores", jsonObject.getString("apellidos") + " " + jsonObject.getString("nombres"));
                        dataList.add(jsonObject.getString("apellidos") + " " + jsonObject.getString("nombres"));
                        dataID.add(jsonObject.getInt("idcolaborador"));
                    }
                    // !Alertaremos al adaptador indicando que hay cambios
                    adapter.notifyDataSetChanged();

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });

        //Enviamos el request(requerimiento)
        Volley.newRequestQueue(this).add(jsonArrayRequest);



    } // Fin

    private void loadUI(){
        lvColaboradores = findViewById(R.id.lvColaboradores);
    }
}