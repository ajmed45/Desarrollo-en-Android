package uv.edu.tarea3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActividadMensajes extends AppCompatActivity {
    private EditText usuario;
    private Button mas;
    private Button actu;
    private Boolean anadir = false;
    HttpURLConnection urlConnection;
    Mensaje[] m;
    File file;
    AdapterMensaje mens;
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences prefs = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String usuario = prefs.getString("et_usuario","valor por defecto");
        Boolean ON = prefs.getBoolean("Encendido",false);

        if(ON)
        {
            System.out.println("Existe el fichero");
            setContentView(R.layout.mensajes_activity);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mostrarMensajes();


        }

        else
        {
            System.out.println("No Existe el fichero");
            Intent intent = new Intent(this, ActividadUsuario.class);
            startActivity(intent);
            finish();
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.accion_enviar_mensaje:
                System.out.println("añadir mensaje");
                addMensaje();
                return true;
            case R.id.accion_actualizar_mensajes:
                System.out.println("actualizar mensajes");
                mostrarMensajes();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mostrarMensajes()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getMensajes();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    lista = (ListView)findViewById(R.id.mensajes_list);
                                    mens = new AdapterMensaje(ActividadMensajes.this);
                                    lista.setAdapter(mens);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(mainActivity);
            };
        };
        thread.start();



    }

    public void addMensaje()
    {
        Intent i = new Intent(this, ActividadEnviar.class);
        startActivity(i);
    }

    public String getMensajes() throws IOException
    {
        String fin="";

        URL url = new URL ("http://147.156.84.49:8080/ServidorMensajes/mensajes?desde=3");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
// Se establecen propiedades de la petición :
        http.setRequestProperty("Accept", "application/json");
        http.setReadTimeout(10000 /* milliseconds */);
        http.setConnectTimeout(15000 /* milliseconds */);
// Indicamos que el metodo de peticion es GET (es la opción por defecto )
        http.setRequestMethod("GET");
// Al llamar a connect se realiza la petición
        http.connect();
        InputStream in = http.getInputStream();
        int dato ;
        while ((dato = in.read())!= -1) {
            //System.out.println((char) dato);
            fin+=(char)dato;
        }
        in.close();
        //System.out.println(fin);
        Gson gson = new Gson();
        m = gson.fromJson(fin, Mensaje[].class);
        //System.out.println(m[1].getUsuario());
        escribirDatos(m);

        return fin;
    }

    void escribirDatos(Mensaje[] m)
    {
        file = new File(getFilesDir()+Ficheros.fichMensajes);
        try{
            //Preparamos el Stream
            OutputStreamWriter bf = new OutputStreamWriter(new FileOutputStream(file));

            for(int i=0;i<m.length; i++) {
                // escribimos linea en el fichero...
                bf.write(m[i].getUsuario());

                // cambiamos de linea
                bf.write("|");

                // escribimos una segunda linea en el fichero...
                bf.write(m[i].getMensaje());

                // cambiamos de linea
                bf.write("\n");
            }
            // Cerramos el Fichero donde guardamos la BD
            bf.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD"+ex.toString());
        }


    }




}
