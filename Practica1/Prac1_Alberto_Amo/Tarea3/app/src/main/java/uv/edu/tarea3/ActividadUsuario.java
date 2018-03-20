package uv.edu.tarea3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ActividadUsuario extends AppCompatActivity {
    private ArrayList<String> mensajes;
    private Button boton;
    public static final String fichero = "mensajes.txt";
    public String ruta;
    private EditText usuario;
    private boolean ON = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_activity);
        mensajes = new ArrayList<String>();

        // Asignar el objeto al botón usando findViewById
        boton = (Button) findViewById(R.id.btn_usuario);
        usuario = (EditText) findViewById(R.id.et_usuario);




        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ruta = getExternalFileName();
                //GuardarDatos(ruta);
                SharedPreferences prefs = getSharedPreferences("datos", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("et_usuario", usuario.getText().toString());
                ON = true;
                editor.putBoolean("Encendido",true);
                editor.commit();
                finish();
                //System.out.println("preferencias guardadas");


            }
        });



    }

    private String getExternalFileName() {
        File f = getExternalFilesDir(null);
        return f.getAbsolutePath() + "/" + ActividadUsuario.fichero;
    }






}





