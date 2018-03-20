package edu.uv.adm.pr2;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ServicioLocalizacion extends Service implements LocationListener {
    private Handler handler;
    private HandlerThread thread;
    public double latitud;
    public double longitud;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private static final long DISTANCIA = 50;
    private static final long TIEMPO = 1000 * 60 * 1;
    protected LocationManager locationManager;
    Location location;


        public ServicioLocalizacion() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Entramos en el servicio de localizacion");
        thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Looper looper = thread.getLooper();
        handler = new Handler(looper);
        // TODO Obtener LocationManager
        location = getLocation();
        // TODO Registrar esta instancia como oyente de eventos de localizacion
        System.out.println("La latitud es: " + latitud);
        System.out.println("La longitud es: " + longitud);

        escribirFicheroMemoriaInterna();




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        thread.quit();
        // TODO Quitar esta insatancia como oyente de eventos de localización
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO
        // Si la posicion ha cambiado mas de 50 metros respecto a la posicion
        // anterior o es la primera lectura entonces hay que ejecutar la siguiente
        // Sentencia para que ejecute el código en la HandlerThread
        handler.post(new Runnable() {
            public void run() {
                // Obtener una referencia a la BD usando DBUsuarios.getDBUsuarios
                // Actualizar la BD para el usuario id pasado en el intent e
                // iniciar ServicioComunicacion con la accion
                // AppNames.Actions.LOCATION_CHANGE
            }
        });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {  }

    @Override
    public void onProviderEnabled(String s){  }

    @Override
    public void onProviderDisabled(String s) { }


    public Location getLocation() {
        try {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,TIEMPO, DISTANCIA, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,TIEMPO, DISTANCIA, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public double getLatitude(){
        return latitud;
        }




    public double getLongitude(){

            return longitud;
        }

    private void escribirFicheroMemoriaInterna()
    {
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), "micord.txt");
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(file));
            osw.write(String.valueOf(latitud)+"\n");
            osw.write(String.valueOf(longitud)+"\n");
            osw.flush();
            osw.close();


        } catch (IOException ioe) {
        }
    }



}
