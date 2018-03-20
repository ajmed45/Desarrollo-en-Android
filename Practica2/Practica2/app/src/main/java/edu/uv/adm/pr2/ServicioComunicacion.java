package edu.uv.adm.pr2;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ServicioComunicacion extends IntentService {

    public ServicioComunicacion() {
        super("ServicioComunicacion");
    }

    LocEvent evento;
    DatagramSocket ds;
    // Qué hacer en el hilo de trabajo separado cuando llegue un Intent
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(AppNames.Actions.CONNECTED)) {
                final int id = intent.getIntExtra(AppNames.Params.ID_PARAM, 0);
                try {
                    handleActionConnected(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(AppNames.Actions.REQUEST_CHANGES)) {
                final int id = intent.getIntExtra(AppNames.Params.ID_PARAM, 0);
                try {
                    handleActionRequest(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(AppNames.Actions.LOCATION_CHANGE)) {
                final int id = intent.getIntExtra(AppNames.Params.ID_PARAM, 0);
                final double lat = intent.getDoubleExtra(AppNames.Params.LAT_PARAM, 0);
                final double lon = intent.getDoubleExtra(AppNames.Params.LON_PARAM, 0);
                try {
                    handleActionLocationChanged(id, lat, lon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(AppNames.Actions.DISCONNECTED)) {
                final int id = intent.getIntExtra(AppNames.Params.ID_PARAM, 0);
                try {
                    handleActionDisconnected(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleActionConnected(int id) throws IOException {
        // TODO Enviar UDP con AppNames.Codes.CONNECTED e id
        ds = new DatagramSocket(12000);
        ByteBuffer bb = ByteBuffer. allocate(256) ;
        bb.putInt(id) ;
        bb.putInt(AppNames.Codes.CONNECTED);
        byte [] data = bb. array ();
        DatagramPacket dp = new DatagramPacket(data ,256 , InetAddress. getByName("147.156.84.49"), 12000 );
        ds.send(dp);
        System.out.println("entramos en el handleActionConnected");
        ds. close();
        handleActionRequest(id);
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionRequest(int id) throws IOException {
        // TODO Enviar UDP con AppNames.Codes.REQUEST_CHANGES e id
        System.out.println("entramos en el handleActionRequest con la alarma");
        ds = new DatagramSocket(12000);
        ByteBuffer bb = ByteBuffer. allocate(256) ;
        bb.putInt(id) ;
        bb.putInt(AppNames.Codes.CONNECTED);
        byte [] data = bb. array ();
        DatagramPacket dp = new DatagramPacket(data ,256 , InetAddress. getByName("147.156.84.49"), 12000 );
        ds.send(dp);
        // TODO Recibir UDP de respuesta con los cambios de localización de otros usuarios
        dp = new DatagramPacket ( new byte [1024] ,1024) ;
        ds.receive(dp);
        ds. close ();


        // TODO El UDP contiene un byte[] a partir del cual se puede crear un String
        //      que contiene un array de objetos JSON [{id1,lat,lon},{id2,lat,lon},...]
        //      con los id de los usuarios que han cambiado la posicion

        // TODO Si hay cambios actualizar la BD y enviar un broadcast para la actividad de mapas
        DBUsuarios db2 = DBUsuarios.getDBUsuarios(getApplicationContext());
        String fin = new String (dp.getData(),0,dp.getLength());
        Gson gson = new Gson();
        evento = gson.fromJson(fin, LocEvent.class);
        db2.actualizar(evento.getId(), evento.getLat(), evento.getLon(), evento.getTimestamp());
        System.out.println("se han actualizado las posiciones con el server");

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ServicioComunicacion.CONNECTIVITY_SERVICE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("id_actual", evento.getId());
        broadcastIntent.putExtra("lat_actual", evento.getLat());
        broadcastIntent.putExtra("long_actual", evento.getLon());
        sendBroadcast(broadcastIntent);


        //throw new UnsupportedOperationException("Not yet implemented");

    }

    private void handleActionLocationChanged(int id, double lat, double lon) throws IOException {
        // TODO Enviar UDP con AppNames.Codes.LOCATION_CHANGE, id, lat y lon
        DatagramSocket ds = new DatagramSocket(12000);
        ByteBuffer bb = ByteBuffer. allocate(256) ;
        bb.putInt(id) ;
        bb.putDouble(lat);
        bb.putDouble(lon);
        bb.putInt(AppNames.Codes.LOCATION_CHANGE);
        byte [] data = bb. array ();
        DatagramPacket dp = new DatagramPacket(data ,256 , InetAddress. getByName("147.156.84.49"), 12000 );
        ds.send(dp);
        //throw new UnsupportedOperationException("Not yet implemented");
        ds. close ();
    }

    private void handleActionDisconnected(int id) throws IOException {
        // TODO Enviar UDP con AppNames.Codes.DISCONNECTED e id
        DatagramSocket ds = new DatagramSocket(12000);
        ByteBuffer bb = ByteBuffer. allocate(256) ;
        bb.putInt(id) ;
        bb.putInt(AppNames.Codes.DISCONNECTED);
        byte [] data = bb. array ();
        DatagramPacket dp = new DatagramPacket(data ,256 , InetAddress. getByName("147.156.84.49"), 12000 );
        ds.send(dp);
        //throw new UnsupportedOperationException("Not yet implemented");
        ds. close ();
    }
}
