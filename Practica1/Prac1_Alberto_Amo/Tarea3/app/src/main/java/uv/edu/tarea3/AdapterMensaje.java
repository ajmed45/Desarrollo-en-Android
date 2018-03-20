package uv.edu.tarea3;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Alberto on 09/03/2016.
 */
public class AdapterMensaje extends BaseAdapter
{
    private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>(999);
    private Context context;
    int cont = 0;
    static class ViewHolder
    {
        TextView usuario;
        TextView mensaje;
    }

    public AdapterMensaje(Context c)throws IOException
    {
        context = c;
        Mensaje m;
        boolean userEnc = false;
        File file = new File(c.getFilesDir()+Ficheros.fichMensajes);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String aux = "";
            while((line=br.readLine())!=null) {
                //System.out.println(line);
                StringTokenizer st = new StringTokenizer(line,"|");
                String usuario, mensaje;
                usuario = null;
                mensaje = null;
                if(st.hasMoreTokens()) {
                    usuario = st.nextToken();
                }
                if(st.hasMoreTokens()) {
                    mensaje = st.nextToken();
                }
                //System.out.println(usuario);
                //System.out.println(mensaje);
                m = new Mensaje(cont, usuario, mensaje);
                mensajes.add(m);
                cont++;
            }
            /*while ((line = br.readLine()) != null) {

                aux += br.readLine();
                userEnc = false;
                String usuario = "";
                String mensaje="";
                for (int i = 0; i < aux.length(); i++){
                    if (aux.charAt(i) != '|' && userEnc == false) {
                        usuario += aux.charAt(i);

                    }


                else if (aux.charAt(i) == '|') {
                    userEnc= true;

                }

                else if(userEnc==true && aux.charAt(i)!='|')
                    mensaje += aux.charAt(i);

            }


                    m = new Mensaje(cont, usuario, mensaje);

                        //System.out.println(mensaje);
                        mensajes.add(m);
                        //System.out.println("anyado el usuario"+usuario);

                    //System.out.println("el objeto m"+m.getUsuario());
                    cont++;
                    aux = "";


            }*/



        }



    }

    @Override
    public int getCount()
    {
        return mensajes.size();
    }

    public void add(Mensaje m)
    {
        //hacer añadir mensaje al arraylist
        mensajes.add(m);
        System.out.println("anyadido");

        //esto sirve para indicar a la vista que el modelo ha cambiado
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position)
    {
        return mensajes.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        ViewHolder holder = null;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_msg_view, null);
            holder = new ViewHolder();
            holder.usuario = (TextView) v.findViewById(R.id.msg_view_usuario);
            holder.mensaje = (TextView) v.findViewById(R.id.msg_view_mensaje);
            v.setTag(holder);
        }

        else
        {
            v=convertView;
            holder = (ViewHolder)v.getTag();
        }

        //hacer asignar a los elementos del holder los datos que estan en la posicion position del arrayList
        holder.usuario.setText("Usuario: "+mensajes.get(position).getUsuario());
        holder.mensaje.setText("Mensaje: " + mensajes.get(position).getMensaje());
        //System.out.println("el tam de los mensajes es: " + mensajes.size());
        //System.out.print("diiiii"+mensajes.get(5).getUsuario());


        return v;
    }
}
