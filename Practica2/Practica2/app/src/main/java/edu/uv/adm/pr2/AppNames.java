package edu.uv.adm.pr2;

/**
 * Created by juan on 15/03/16.
 */
public interface AppNames {
    public static interface Broadcast {
        public static final String LOCATION_CHANGES = "edu.uv.adm.pr2.event.ACTION_LOCATION_CHANGES";
    }

    // Nombres de acción que usaremos para indicar ServicioComunicacion que accion solicitamos
    public static interface Actions {
        // Usado para indicar a ServicioComunicacion que ha cambiado la posición del dispositivo
        public static final String LOCATION_CHANGE = "edu.uv.adm.pr2.action.LOCATION";
        // Usado para indicar a ServicioComunicacion que se ha iniciado el dispositivo
        public static final String CONNECTED = "edu.uv.adm.pr2.action.CONNECTED";
        // Usado para indicar a ServicioComunication que se apaga el dispositivo
        public static final String DISCONNECTED = "edu.uv.adm.pr2.action.DISCONNECTED";
        // Usado para indicar a ServicioComunicacion que envíe un mensaje para ver si ha
        // cambiado la posicion de otros usuarios. Esta es la peticion que realizara automaticamente
        // la alarma
        public static final String REQUEST_CHANGES = "edu.uv.adm.pr2.action.REQUEST_CHANGES";
    }

    // Codigos a enviar al servidor que identifican la accion que se solicita
    public static interface Codes {
        public static final int CONNECTED = 0;
        public static final int REQUEST_CHANGES = 1;
        public static final int LOCATION_CHANGE = 2;
        public static final int DISCONNECTED = 3;
    }

    // Nombres usados para colocar la información en los Intent
    public static interface Params {
        public static final String ID_PARAM = "id";
        public static final String LAT_PARAM = "latitud";
        public static final String LON_PARAM = "longitud";
    }
}
