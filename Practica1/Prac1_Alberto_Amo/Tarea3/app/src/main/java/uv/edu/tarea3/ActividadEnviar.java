package uv.edu.tarea3;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 02/03/2016.
 */
public class ActividadEnviar extends AppCompatActivity {
    private TextView tv;
    private EditText et;
    private Button btn;
    private Bundle savedInstance;
    public int cont = 0;
    private HttpClient httpclient;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.enviar_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String usuario = prefs.getString("et_usuario", "valor por defecto");

        tv = (TextView) findViewById(R.id.tv_user_enviar);
        et = (EditText) findViewById(R.id.tv_msg_enviar);
        btn = (Button) findViewById(R.id.btn_enviar);
        //Intent inten = getIntent();
        tv.setText("Usuario: " + usuario);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont++;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs2 = getSharedPreferences("datos", Context.MODE_PRIVATE);
                        String user = prefs2.getString("et_usuario", "valor por defecto");
                        //System.out.println("el usuario es: " +user);
                        String aux = quitarEspacios(user);
                        try {
                            enviarMensaje(aux, et.getText().toString());
                            //Toast.makeText(this, "Mensaje 1", Toast.LENGTH_SHORT);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }).start();
                Toast.makeText(getApplicationContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void enviarMensaje(String usuario, String mensaje) throws IOException {
        URL url = new URL("http://147.156.84.49:8080/ServidorMensajes/mensajes");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        // Obtener la conexión
        HttpURLConnection con = null;

        try {
            // Construir los datos a enviar
            String data =
                    "user=" + URLEncoder.encode(usuario) +
                            "&mensaje=" + URLEncoder.encode(mensaje);
            System.out.println(data);

            con = (HttpURLConnection) url.openConnection();

            // Activar método POST
            con.setDoOutput(true);

            // Tamaño previamente conocido
            con.setFixedLengthStreamingMode(data.getBytes().length);

            // Establecer application/x-www-form-urlencoded debido a la simplicidad de los datos
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }

        System.out.println("enviado el mensaje");

    }


    public int getCont() {
        return cont;
    }


    String quitarEspacios(String st) {
        String aux = st;
        aux = aux.replace(" ", "-");
        System.out.println(aux);
        return aux;

    }

}




