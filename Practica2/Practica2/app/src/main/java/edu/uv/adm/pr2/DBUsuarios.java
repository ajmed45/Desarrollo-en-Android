package edu.uv.adm.pr2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DBUsuarios extends SQLiteOpenHelper {
    public static DBUsuarios dbusers = null;
    private Context context;
    public int tam;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "DBusuarios";

    // Nombre de la tabla
    public static final String TABLE_NAME = "TUsuarios";

    // Nombres de las columnas de la tabla de usuarios
    // Tipo
    public static final String KEY_ID = "_id";
    // Tipo TEXT
    public static final String KEY_NOMBRE = "nombre";
    // Tipo TEXT
    public static final String KEY_NOMBRE_FOTO = "foto";
    // Tipo REAL
    public static final String KEY_LATITUD = "lat";
    // Tipo REAL
    public static final String KEY_LONGITUD = "lon";
    // Tipo INTEGER
    public static final String KEY_TIMESTAMP = "ts";

    //Sentencia SQL para crear la tabla de Usuarios
    private String sqlCreate = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER, " + KEY_NOMBRE + " TEXT, " +
            KEY_NOMBRE_FOTO +" TEXT, " + KEY_LATITUD + " REAL, " + KEY_LONGITUD + " REAL, " + KEY_TIMESTAMP + " INTEGER)";

    private String sqlInsertInitData = "";

    //Sentencia sql para recoger todos los usuarios
    private String sqlGetAll = "SELECT * FROM " + TABLE_NAME;

    private SQLiteDatabase db = null;

    // Usar este método para obtener una referencia a la base de datos
    // Se conoce como patrón singletón
    public static DBUsuarios getDBUsuarios(Context context){
        if (dbusers==null){
            dbusers = new DBUsuarios(context);
        }
        return dbusers;
    }

    public DBUsuarios(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //System.out.println("entramos en la bd");
        //cargarDatosFichero();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        //Crear tabla
        db.execSQL(sqlCreate);
        cargarDatosFichero();
        //System.out.println("vamos a ir a cargar datos de fichero");
        //db.execSQL(sqlInsertInitData);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }


    // Obtencion de un cursor para todos los resgistros
    public Cursor getUsuarios() {
        // Realizar una consulta para obtener todos los usuarios y devolver el cursor
        String[] s = new String[0];
        Cursor c =  getReadableDatabase().rawQuery(sqlGetAll, s);
        return c;
    }


    // Actualizar la información de localizacion de un usuario
    public void actualizar(int id, double lat, double lon, long ts) {
        // Actualizar la informacion del ususario
        String sqlUpdate = "UPDATE " + TABLE_NAME + " SET " + KEY_LATITUD + " ='" + lat + "', "
                + KEY_LONGITUD + " ='" + lon + "', " + KEY_TIMESTAMP + " ='" + ts + "' WHERE " + KEY_ID + "=" + id;

        db.execSQL(sqlUpdate);
    }

    public void cargarDatosFichero(){

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        File file = new File(path, "personas.txt");

        StringBuffer sb = new StringBuffer();
        //System.out.println("leo datos del fichero");
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int cont = 0;
            tam = cont;
            while((line = br.readLine())!=null){
                System.out.println(line);
                //sb.append("INSERT INTO " + TABLE_NAME + " (" + KEY_ID + ", " + KEY_NOMBRE + ", " +  KEY_NOMBRE_FOTO + ") VALUES (" + cont +", '" + line.split(";")[0] + "', '" + line.split(";")[1]+ "');");
                //db.execSQL("INSERT INTO " + TABLE_NAME + " (" + KEY_ID + ", " + KEY_NOMBRE + ", " +  KEY_NOMBRE_FOTO +", "+ KEY_LATITUD +", "+ KEY_LONGITUD+", "+KEY_TIMESTAMP+") VALUES (" + cont +", '" + line.split(";")[0] + "', '" + line.split(";")[1]+ "',39.512452,-0.4239682,1)");
                ContentValues nuevoreg = new ContentValues();
                nuevoreg.put(KEY_ID,cont);
                nuevoreg.put(KEY_NOMBRE,line.split(";")[0]);
                nuevoreg.put(KEY_NOMBRE_FOTO,line.split(";")[1]);
                nuevoreg.put(KEY_LATITUD,50);
                nuevoreg.put(KEY_LONGITUD, 50);
                nuevoreg.put(KEY_TIMESTAMP,System.currentTimeMillis());

                db.insert(TABLE_NAME, null, nuevoreg);

                //Log.i("INSERT", "INSERT INTO " + TABLE_NAME + " (" + KEY_ID + ", " + KEY_NOMBRE + ", " + KEY_NOMBRE_FOTO +", "+ KEY_LATITUD + ", "+KEY_LONGITUD+", "+KEY_TIMESTAMP+") VALUES (" + cont + ", '" + line.split(";")[0] + "', '" + line.split(";")[1]+ "',39.512452,-0.4239682,1);");
                cont ++;



            }

            br.close();
        }catch(IOException e){}

        sqlInsertInitData = sb.toString();

    }

    public int getTuplas()
    {
        return tam;


    }




}