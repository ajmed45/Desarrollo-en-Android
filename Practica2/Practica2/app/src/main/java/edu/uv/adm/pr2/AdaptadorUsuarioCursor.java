package edu.uv.adm.pr2;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class AdaptadorUsuarioCursor extends CursorAdapter {

    static class ViewHolder {
        TextView nombre;
        ImageView cara;
    }

    public AdaptadorUsuarioCursor(Context context, Cursor c) {
        super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
        System.out.println("entro en el cursor");
        LayoutInflater li = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.item_usuario_layout, null);
        ViewHolder vh = new ViewHolder();
        // TODO Obtener las subvistas de item_usuario_layout
        // TODO Asignar las vistas a los atributos de ViewHolder
        // TODO Asignar el ViewHolder creado a view usando setTag
        vh.nombre = (TextView) view.findViewById(R.id.n_usuario);
        vh.cara = (ImageView) view.findViewById(R.id.i_usuario);
        view.setTag(vh);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.item_usuario_layout, null);
        /*ViewHolder vh = new ViewHolder();
        // TODO Obtener las subvistas de item_usuario_layout
        // TODO Asignar las vistas a los atributos de ViewHolder
        // TODO Asignar el ViewHolder creado a view usando setTag
        vh.nombre = (TextView) view.findViewById(R.id.n_usuario);
        vh.cara = (ImageView) view.findViewById(R.id.i_usuario);
        view.setTag(vh);*/
        asignaInfo(view, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        asignaInfo(view, cursor);
    }

    @Override
    public int getCount() {
        if (getCursor() == null) {
            return 0;
        }
        return super.getCount();
    }


    private void asignaInfo(View view, Cursor cursor) {
        // TODO Obtener la información del cursor (en las columnas nombre y cara)
        // TODO Obtener el ViewHolder asociado a la vista
        // TODO Asignar a los atributos del ViewHolder la información extraída del cursor
        //System.out.println("entro en cursor/asignaInfo ");
        String nombre = cursor.getString(cursor.getColumnIndex(DBUsuarios.KEY_NOMBRE));
        //String nombre= "Asuncion Gallardo Fernandez";
        String cara = cursor.getString(cursor.getColumnIndex(DBUsuarios.KEY_NOMBRE_FOTO));
        //String cara = "crop_103236168_1.jpg";
        //Double lat = cursor.getDouble(cursor.getColumnIndex(DBUsuarios.KEY_LATITUD));
        //Double longi = cursor.getDouble(cursor.getColumnIndex(DBUsuarios.KEY_LONGITUD));

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String nombreDir = path.getAbsolutePath();
        String nuevoDir = nombreDir + File.separator + "caras" + File.separator;
        String uri = nuevoDir + cara;

        //view.getTag();
        TextView name = (TextView) view.findViewById(R.id.n_usuario);
        ImageView face = (ImageView) view.findViewById(R.id.i_usuario);

        name.setText(nombre);
        //System.out.println("el nombre es "+nombre);

        if(new File(cara).exists())
        {
            face.setImageURI(Uri.parse(cara));
            //System.out.println("caras detectadas");
        }

        else
        {
            face.setImageURI(Uri.parse(uri));
            //System.out.println("caras no detectadas");
        }

    }
}

