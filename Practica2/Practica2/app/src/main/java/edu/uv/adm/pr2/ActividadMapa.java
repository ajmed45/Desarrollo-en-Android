package edu.uv.adm.pr2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ActividadMapa extends AppCompatActivity {
    public double lati1, longi1;
    public class ReceiverLocalizacion extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Ver los datos del intent y comprobar si ha cambiado la
            // posición del usuario que estamos visualizando.
            // Si es así hay que modificar su posición en el mapa
            Intent intenti = new Intent(context,ServicioLocalizacion.class);
            context.startService(intenti);
            System.out.println("entro en actividadMapa y veo broadcast");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        MapView map = (MapView) findViewById(R.id.mapa);
        map.setTileSource(TileSourceFactory.MAPNIK);
        // Obtener el id y la posición del intent y mostrar una
        // marca en el mapa


        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(20);

        Bundle aux = getIntent().getExtras();
        int posi = aux.getInt("id");
        double lati = aux.getDouble("latitud");
        double longi = aux.getDouble("longitud");

        if(aux.getBoolean("OK")&& posi==0)
        {
            try {
                recuperar();
            } catch (IOException e) {
                e.printStackTrace();
            }

            GeoPoint startPoint2 = new GeoPoint(lati1, longi1);
            //mapController.setCenter(startPoint);
            mapController.animateTo(startPoint2);

            ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
            items.add(new OverlayItem("Title", "Description", startPoint2));

            ItemizedOverlayWithFocus<OverlayItem> anotherItemizedIconOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, items, null);
            map.getOverlays().add(anotherItemizedIconOverlay);

        }

        if(posi!=0)
        {

            GeoPoint startPoint = new GeoPoint(lati, longi);
            //mapController.setCenter(startPoint);
            mapController.animateTo(startPoint);

            ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
            items.add(new OverlayItem("Title", "Description", startPoint));

            ItemizedOverlayWithFocus<OverlayItem> anotherItemizedIconOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, items, null);
            map.getOverlays().add(anotherItemizedIconOverlay);


            System.out.println("La posicion es: " + posi);
        }



    }

    @Override
    protected void onResume() {
        // Registrar ReceiverLocalizacion para recibir eventos
        // del tipo AppNames.Broadcast.LOCATION_CHANGES
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Quitar el registro de ReceiverLocalizacion
        super.onPause();
    }

    public void recuperar() throws IOException{
        String nomarchivo = "micord.txt";
        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
        try {
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader archivo = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(archivo);
            String linea;
            String todo = "";
            int cont = 0;
            while((linea=br.readLine())!=null) {
                if(cont == 0) {
                    lati1 = Double.parseDouble(linea);
                    System.out.println("la lati1 es " + lati1);
                }

                if(cont == 1) {
                    longi1 = Double.parseDouble(linea);
                    System.out.println("la lati1 es " + longi1);
                }
                cont++;
            }
            br.close();
            archivo.close();


        } catch (IOException e) {
        }
    }




}
