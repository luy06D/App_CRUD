package com.example.crusws;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listar extends AppCompatActivity {

    ListView lvColaboradores;

    //Objetos requeridos para transferir datos (WS > ListView)
    // Guardar apellidos y nombres (obtenido JSONObject)
    private List<String> dataList = new ArrayList<>();
    // El adaptador recibe los datos de lalista y lo enviará al ListView
    private CustomAdapter adapter;

    // Constante que almacena la url
    final String URL = "http://192.168.1.101/wservice/controllers/colaborador.php?operacion=list";

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
                showAlertOptions(posicion);
            }
        });


    }


    private void showAlertOptions(int posicionIndex){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(dataList.get(posicionIndex));
        dialogo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int itemIndex) {
                //3 opciones => 0,1,2
                switch (itemIndex){
                    case 0:
                        break;
                    case 1:
                        break;
                    case  2:
                        break;
                }
            }
        });
        dialogo.show();
    }

    private void getData(){

        dataList.clear();
        adapter = new CustomAdapter(this, dataList);
        lvColaboradores.setAdapter(adapter);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
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