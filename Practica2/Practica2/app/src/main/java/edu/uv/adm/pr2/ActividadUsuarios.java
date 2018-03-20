package edu.uv.adm.pr2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class ActividadUsuarios extends AppCompatActivity {

    private ListView lista;
    private DBUsuarios bbdd;
    private AdaptadorUsuarioCursor cursor;
    private double latitud, longitud;
    public boolean click_loc = false;
    public Bundle aux = new Bundle();
    public double lat1,long1;
    boolean OK = false;
    //public ResponseReceiver receiver;
    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("iniciamos app");
        am = (AlarmManager) getApplicationContext (). getSystemService ( Context.ALARM_SERVICE );


        //el servidor tienen ip 147.156.84.49:12000

        // TODO Obtener el ListView
        lista = (ListView) findViewById(android.R.id.list);
        // TODO Obtener un objeto del tipo DBUsuarios usando DBUsuarios.getDBUsuarios
        // TODO Abrir la base de datos
        bbdd = new DBUsuarios(getApplicationContext());
        // TODO Obtener el Cursor resultante de la consulta de todos los usuarios
        // TODO Crear el adaptador AdaptadorUsuarioCursor
        cursor = new AdaptadorUsuarioCursor(getApplicationContext(), bbdd.getUsuarios());

        // TODO Asociar el cursor a la lista
        lista.setAdapter(cursor);
        // TODO Asociar un oyente de eventos a la pulsación de un elemento de la lista
        //      para crear un intent que lance ActividadMapa pasando el id, la latitud y la longitud
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitud, longitud);
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                //startActivity(intent);
                //Intent inten3 = new Intent (this,ActividadMapa.class);
                //System.out.println("supuestamento abriremos los mapas con los datos del user");

                aux.putInt("pos", position);

                double lat = cursor.getCursor().getDouble(cursor.getCursor().getColumnIndex(DBUsuarios.KEY_LATITUD));
                double longi = cursor.getCursor().getDouble(cursor.getCursor().getColumnIndex(DBUsuarios.KEY_LONGITUD));



                aux.putDouble("latitud", lat);
                aux.putDouble("longitud", longi);
                aux.putInt("id", cursor.getCursor().getInt(cursor.getCursor().getColumnIndex(DBUsuarios.KEY_ID)));
                aux.putBoolean("OK",OK);
                abrirMapas();
            }
        });


        // TODO Iniciar ServicioComunicacion con la accion AppNames.Actions.CONNECTED
        Intent intent;
        //auxi.putString("Conectado",AppNames.Actions.CONNECTED);
        intent = new Intent(this,ServicioComunicacion.class);
        intent.setAction(AppNames.Actions.CONNECTED);
        startService(intent);

        // TODO  Crear la alarma para que llame a ServicioComunicacion de forma periodica
        //        con la accion AppNames.Actions.REQUEST_CHANGES

        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        intent.setAction(AppNames.Actions.REQUEST_CHANGES);
        am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        am.cancel(pintent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),5000, pintent);
        //startService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO Iniciar ServicioComunicacion con la accion AppNames.Actions.DISCONNECTED
        //Intent servicio = new Intent(AppNames.Actions.DISCONNECTED, ServicioComunicacion.class);
        Intent intent;
        //auxi.putString("Conectado",AppNames.Actions.CONNECTED);
        intent = new Intent(this,ServicioComunicacion.class);
        intent.setAction(AppNames.Actions.DISCONNECTED);
        PendingIntent pIntent = PendingIntent.getService(this,0,intent,0);
        stopService(intent);
        // TODO Detener la alarma
        am.cancel(pIntent);
        // TODO Cerrar la base de datos
        bbdd.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Asociar el App Bar a la actividad
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_main, menu);
        System.out.println("cargando menu");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent servicio = new Intent(getApplicationContext(), ServicioLocalizacion.class);

        switch (item.getItemId()) {

            // TODO
            // Si se pulsa sobre compartir ubicacion{
            //    Si no se está compartiendo {
            //       Iniciar ServicioLocalizacion
            //       Poner una notificación para que el usuario sepa que está compartiendo la ubicación
            //       return true;
            //   }
            //}
            case R.id.start_sharing:
                System.out.println("comparto la ubicacion");
                Toast.makeText(getApplicationContext(), "Compartiendo Ubicación", Toast.LENGTH_LONG).show();

                this.startService(servicio);

                OK = true;



                return true;

            // Si se pulsa sobre parar el tracking{
            //    Si se esta compartiendo{
            //       Parar ServicioLocalizacion
            //       Quitar la notificación
            //       return true;
            //    }
            //}
            case R.id.stop_sharing:
                System.out.println("quito la ubicacion");
                Toast.makeText(getApplicationContext(), "Desactivada la Ubicación Compartida", Toast.LENGTH_LONG).show();

                this.stopService(servicio);
                //OK = false;
                return true;

            default:
                return super.onOptionsItemSelected(item);



        }
    }

    public void abrirMapas()
    {
        Intent intent = new Intent(this,ActividadMapa.class);
        intent.putExtras(aux);
        startActivity(intent);

    }
}
